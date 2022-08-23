package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.suiteapi.type.exceptions.InvalidTypeException;
import com.appiancorp.type.AppianTypeLong;

import java.util.*;

@SuppressWarnings("unchecked")
public class AppianTypeHelper {
    public static AppianList mapListToDictList(AppianTypeFactory typeFactory, TypedValue list) {
        Map<TypedValue, TypedValue>[] mapList = (Map<TypedValue, TypedValue>[]) list.getValue();
        AppianList listOfDict = typeFactory.createList(AppianType.LIST_OF_DICTIONARY);

        for (Map<TypedValue, TypedValue> map : mapList) {
            listOfDict.add(mapToDict(typeFactory, (LinkedHashMap<TypedValue, TypedValue>) map));
        }

        return listOfDict;
    }


    private static AppianObject mapToDict(AppianTypeFactory typeFactory, LinkedHashMap<TypedValue, TypedValue> map) {
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


    /**
     * Takes the first object (Dictionary or CDT) in the sourceObjects and returns the field names, useful for
     *
     * @param typeService Injected TypeService instance
     * @param typedValue  the value
     * @return the list of field names
     */
    public static String[] getFieldNames(TypeService typeService, TypedValue typedValue) throws Exception {
        Map<TypedValue, TypedValue> map = (HashMap<TypedValue, TypedValue>) typeService.cast(AppianTypeLong.LIST_OF_DICTIONARY, typedValue).getValue();

        Set<TypedValue> keySet = map.keySet();
        ArrayList<String> fieldNames = new ArrayList<>(keySet.size());
        for (TypedValue key : keySet) {
            String fieldName = key.getValue().toString();
            fieldName = fieldName.replaceAll("[_]", " ");
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
     * @param typeService Injected TypeService instance
     * @param toCast The TypedValue instance to cast
     * @return a TypedValue instance
     * @throws InvalidTypeException when toCast could not be cast to a list of dictionary
     */
    public static TypedValue toDictionaryList(TypeService typeService, ArrayList<HashMap<TypedValue, TypedValue>> toCast) throws InvalidTypeException {
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
        HashMap<TypedValue, TypedValue> returnMap = new HashMap<TypedValue, TypedValue>();
        try {
            returnMap.putAll((HashMap<TypedValue, TypedValue>) fieldsAndValues.getValue());
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

