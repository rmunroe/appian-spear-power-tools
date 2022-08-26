package org.appiansc.plugins.spt;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.suiteapi.type.exceptions.InvalidTypeException;
import com.appiancorp.type.AppianTypeLong;
import org.apache.log4j.Logger;

import java.util.*;

@SuppressWarnings("unchecked")
public class AppianTypeHelper {
    private static final Logger LOG = Logger.getLogger(AppianTypeHelper.class);

    /**
     * @param typeFactory an AppianTypeFactory instance
     * @param list        the list of Map (as TypedValue)
     * @return a list of Dictionaries (as AppianList)
     */
    public static AppianList mapListToDictList(AppianTypeFactory typeFactory, TypedValue list) throws UnsupportedTypeException {
        Map<TypedValue, TypedValue>[] mapList = (Map<TypedValue, TypedValue>[]) list.getValue();
        AppianList listOfDict = typeFactory.createList(AppianType.LIST_OF_DICTIONARY);

        for (Map<TypedValue, TypedValue> map : mapList) {
            listOfDict.add(mapToDict(typeFactory, (LinkedHashMap<TypedValue, TypedValue>) map));
        }

        return listOfDict;
    }


    /**
     * @param typeFactory an AppianTypeFactory instance
     * @param map         a Map (as LinkedHashMap<TypedValue, TypedValue>)
     * @return a Dictionary (as AppianObject)
     */
    private static AppianObject mapToDict(AppianTypeFactory typeFactory, LinkedHashMap<TypedValue, TypedValue> map) throws UnsupportedTypeException {
        AppianObject dict = (AppianObject) typeFactory.createElement(AppianType.DICTIONARY);
        List<TypedValue> keySet = Arrays.asList(map.keySet().toArray(new TypedValue[0]));
        List<String> keys = new ArrayList<>();
        keySet.forEach(k -> keys.add(k.getValue().toString()));

        for (int i = 0; i < keySet.size(); i++) {
            String key = keys.get(i);
            TypedValue value = map.get(keySet.get(i));
            // Check if it is a Map

            if (value.getInstanceType() == AppianType.MAP) {
                LinkedHashMap<TypedValue, TypedValue> entry = (LinkedHashMap<TypedValue, TypedValue>) value.getValue();
                dict.put(key, mapToDict(typeFactory, entry));
            } else {
                dict.put(key, typeFactory.toAppianElement(value));
            }
        }

        return dict;
    }


    public static List<String> getKeys(AppianObject object) {
        List<TypedValue> keySet = Arrays.asList(object.keySet().toArray(new TypedValue[0]));
        List<String> keys = new ArrayList<>();
        keySet.forEach(k -> keys.add(k.getValue().toString()));
        return keys;
    }


//    private static AppianObject dictToMap(AppianTypeFactory typeFactory, TypedValue dict) {
//        innerDict = dict.getValue();
//        AppianObject temp = (AppianObject) typeFactory.createElement(AppianType.MAP);
//        List<TypedValue> keySet = Arrays.asList(dict.keySet().toArray(new TypedValue[0]));
//        List<String> keys = new ArrayList<>();
//        keySet.forEach(k -> keys.add(k.getValue().toString()));
//
//        for (int i = 0; i < keySet.size(); i++) {
//            String key = keys.get(i);
//            TypedValue value = dict.get(keySet.get(i));
//            // Check if it is a Map
//
//            if (value.getInstanceType() == AppianType.MAP) {
//                LinkedHashMap<TypedValue, TypedValue> entry = (LinkedHashMap<TypedValue, TypedValue>) value.getValue();
//                temp.put(key, mapToDict(typeFactory, entry));
//            } else {
//                temp.put(key, typeFactory.toAppianElement(value));
//            }
//        }
//
//        return temp;
//    }


    /**
     * Takes the first object (Dictionary or CDT) in the sourceObjects and returns the field names, useful for
     *
     * @param typeService Injected TypeService instance
     * @param typedValue  the value
     * @return the list of field names
     */
    public static String[] getFieldNames(TypeService typeService, TypedValue typedValue) {
        Map<TypedValue, TypedValue> map = (HashMap<TypedValue, TypedValue>) typeService.cast(AppianTypeLong.LIST_OF_DICTIONARY, typedValue).getValue();

        Set<TypedValue> keySet = map.keySet();
        ArrayList<String> fieldNames = new ArrayList<>(keySet.size());
        for (TypedValue key : keySet) {
            String fieldName = key.getValue().toString();
            fieldName = fieldName.replaceAll("_", " ");
            fieldNames.add(fieldName);
        }

        return fieldNames.toArray(new String[0]);
    }

    /**
     * @param typeService Injected TypeService instance
     * @param typedValue  the value
     * @return true if cast to Dictionary was successful, otherwise false
     */
    public static Boolean isListDictOrCdt(TypeService typeService, TypedValue typedValue) {
        if (typedValue == null) return false;
        try {
            new ArrayList<>(Arrays.asList((HashMap<TypedValue, TypedValue>[]) typeService.cast(AppianTypeLong.LIST_OF_DICTIONARY, typedValue).getValue()));
        } catch (Exception e1) {
            try {
                typeService.cast(AppianTypeLong.DICTIONARY, typedValue).getValue();
            } catch (Exception e2) {
                return false;
            }
        }
        return true;
    }


    /**
     * @param typeService Injected TypeService instance
     * @param typedValue  the value
     * @return the typedValue cast as an ArrayList&gt;HashMap&gt;TypedValue, TypedValue&lt;&lt;
     * @throws Exception typedValue was not a CDT or Dictionary
     */
    public static ArrayList<HashMap<TypedValue, TypedValue>> toMapList(TypeService typeService, TypedValue typedValue) throws Exception {
        try {
            return new ArrayList<>(Arrays.asList((HashMap<TypedValue, TypedValue>[]) typeService.cast(AppianTypeLong.LIST_OF_DICTIONARY, typedValue).getValue()));
        } catch (Exception e1) {
            try {
                HashMap<TypedValue, TypedValue> returnMap = (HashMap<TypedValue, TypedValue>) typeService.cast(AppianTypeLong.DICTIONARY, typedValue).getValue();
                ArrayList<HashMap<TypedValue, TypedValue>> returnList = new ArrayList<>();
                returnList.add(returnMap);
                return (ArrayList<HashMap<TypedValue, TypedValue>>) returnList.clone();
            } catch (Exception e2) {
                throw new Exception("Invalid CDT");
            }
        }

    }


    /**
     * @param toCast The TypedValue instance to cast
     * @return a TypedValue instance
     * @throws InvalidTypeException when toCast could not be cast to a list of dictionary
     */
    public static TypedValue toDictionaryList(ArrayList<HashMap<TypedValue, TypedValue>> toCast) throws InvalidTypeException {
        try {
            return new TypedValue(AppianTypeLong.LIST_OF_DICTIONARY, toCast.toArray(new HashMap[0]));
        } catch (Exception e) {
            throw new InvalidTypeException("Could not cast to list of dictionary");
        }
    }


    /**
     * @param fieldsAndValues
     * @return
     * @throws Exception
     */
    public static HashMap<TypedValue, TypedValue> toHashMap(TypedValue fieldsAndValues) throws Exception {
        try {
            HashMap<TypedValue, TypedValue> returnMap = new HashMap<>((HashMap<TypedValue, TypedValue>) fieldsAndValues.getValue());
            return (HashMap<TypedValue, TypedValue>) returnMap.clone();
        } catch (Exception e) {
            throw new Exception("Invalid dictionary");
        }
    }


    /**
     * Converts a Set of TypedValue to an ArrayList of TypedValue
     *
     * @param set
     * @return
     */
    public static ArrayList<TypedValue> setToArrayList(Set<TypedValue> set) {
        return new ArrayList<>(Arrays.asList(set.toArray(new TypedValue[0])));
    }


    /**
     * Attempts to cast an Object to an Object[]
     *
     * @param obj
     * @return
     */
    public static Object[] toObjectArr(Object obj) {
        Object[] objArr;
        try {
            objArr = (Object[]) obj;
        } catch (Exception e) {
            objArr = new Object[1];
            objArr[0] = obj;
        }
        return objArr;
    }
}

