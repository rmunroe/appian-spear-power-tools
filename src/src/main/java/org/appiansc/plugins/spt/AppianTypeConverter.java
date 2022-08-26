package org.appiansc.plugins.spt;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Converts Maps, Dictionaries, or CDTs to Maps or Dictionaries, recursively
 */
@SuppressWarnings("unchecked")
public class AppianTypeConverter {
    private static final Logger LOG = Logger.getLogger(AppianTypeConverter.class);

    private static final List<Long> MAP_OR_DICTIONARY = Arrays.asList((long) AppianType.DICTIONARY, (long) AppianType.MAP);


    /**
     * Converts Maps, Dictionaries, or CDTs to Maps or Dictionaries, recursively
     *
     * @param typeService an injected Appian TypeService instance
     * @param object      the Map(s), Dictionarie(s), or CDT(s) to convert
     * @param toType      the Long type ID of the type to change them to (Map or Dictionary)
     * @return the converted object
     * @throws UnsupportedTypeException If an unsupported type is provided
     */
    public static TypedValue convert(TypeService typeService, TypedValue object, Long toType) throws UnsupportedTypeException {
        DatatypeProperties props = typeService.getDatatypeProperties(object.getInstanceType());

        if (props.isListType()) {
            // TODO: HANDLE LISTS
            LOG.debug("List");
            return object;
        } else {
            if (props.isRecordType()) {  // Is a CDT
                TypedValue dict = convertCdtToDict(typeService, object);
                if (toType != AppianType.DICTIONARY)
                    return convertMapOrDict(typeService, dict, toType);

            } else if (MAP_OR_DICTIONARY.contains(object.getInstanceType())) {
                return convertMapOrDict(typeService, object, toType);
            }
        }

        throw new UnsupportedTypeException("Unsupported types");
    }


    /**
     * Converts a CDT to a Dictionary (which can then be converted to a Map)
     *
     * @param typeService an injected Appian TypeService instance
     * @param object      the CDT(s) to convert
     * @return the converted object
     */
    private static TypedValue convertCdtToDict(TypeService typeService, TypedValue object) {
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);

        // Cast to toType and create AppianObject from the result
        AppianObject element = (AppianObject) typeFactory.toAppianElement(typeService.cast((long) AppianType.DICTIONARY, object));

        List<String> keys = Arrays.asList(element.keySet().toArray(new String[0]));

        // Step through the input CDTs properties looking for nested CDTs
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            DatatypeProperties properties = typeService.getDatatypeProperties(element.get(key).getTypeId());
            if (properties.isRecordType() || (properties.isListType() && typeService.getDatatypeProperties(properties.getTypeof()).isRecordType())) {
                // This one is a CDT -or- a List of CDTs

                // Get raw child CDT from input object
                Object[] rawValue = (Object[]) ((Object[]) object.getValue())[i];

                if (properties.isListType()) {
                    // List of CDT
                    if (rawValue == null || Arrays.stream(rawValue).noneMatch(Objects::nonNull))
                        // Nested List of CDT was NULL; put an empty List of Dictionary to avoid a blank CDT instances
                        element.put(key, typeFactory.createElement(AppianType.LIST_OF_DICTIONARY));
                    else {
                        // Was not null, foreach and recurse
                        AppianList list = (AppianList) element.get(key);
                        AppianList newList = typeFactory.createList(AppianType.DICTIONARY);

                        for (int j = 0; j < list.size(); j++) {
                            AppianObject listItem = (AppianObject) list.get(j);
                            AppianElement updated = handleChildCdt(typeService, typeFactory, (Object[]) rawValue[j], listItem);
                            newList.add(updated);
                        }

                        element.put(key, newList);
                    }
                } else {
                    // Single, nested CDT
                    element.put(key, handleChildCdt(typeService, typeFactory, rawValue, element.get(key)));
                }
            }
        }

        return typeFactory.toTypedValue(element);
    }

    /**
     * Gracefully handles child nested CDTs, checking for nulls
     *
     * @param typeService an injected Appian TypeService instance
     * @param typeFactory an instance of TypeTransformer's AppianTypeFactory
     * @param rawValue    the Object[] found inside the TypedValue
     * @param cdtElement  an AppianElement representation of the TypedValue
     * @return the converted child CDT
     */
    private static AppianElement handleChildCdt(TypeService typeService, AppianTypeFactory typeFactory, Object[] rawValue, AppianElement cdtElement) {
        if (rawValue == null || Arrays.stream(rawValue).noneMatch(Objects::nonNull)) {
            // Nested CDT was NULL; put an empty toType to avoid a blank CDT instance
            return typeFactory.createElement(AppianType.DICTIONARY);
        } else {
            // Was not null, create a new TypedValue and recurse
            TypedValue typedValue = new TypedValue();
            typedValue.setInstanceType(cdtElement.getTypeId());
            typedValue.setValue(rawValue);

            TypedValue updated = convertCdtToDict(typeService, typedValue);
            return typeFactory.toAppianElement(updated);
        }
    }


    /**
     * Converts a Map or a Dictionary to a Map or a Dictionary, recursively
     *
     * @param typeService an injected Appian TypeService instance
     * @param object      the CDT(s) to convert
     * @param toType      the Long type ID of the type to change them to (Map or Dictionary)
     * @return the converted Map or Dictionary
     */
    private static TypedValue convertMapOrDict(TypeService typeService, TypedValue object, Long toType) {
        HashMap<TypedValue, TypedValue> objectValue = (HashMap<TypedValue, TypedValue>) object.getValue();

        convertHashMap(typeService, objectValue, toType);

        object.setInstanceType(toType);
        object.setValue(objectValue);

        return object;
    }


    /**
     * Does the conversion of the underlying HashMap<TypedValue, TypedValue> that represents a Map or a Dictionary
     *
     * @param typeService an injected Appian TypeService instance
     * @param objectValue the HashMap<TypedValue, TypedValue> instance
     * @param toType      the Long type ID of the type to change them to (Map or Dictionary)
     */
    private static void convertHashMap(TypeService typeService, HashMap<TypedValue, TypedValue> objectValue, Long toType) {
        // For each key/value in the object
        for (TypedValue key : objectValue.keySet().toArray(new TypedValue[0])) {
            TypedValue value = objectValue.get(key);
            DatatypeProperties valueProps = typeService.getDatatypeProperties(value.getInstanceType());

            if (valueProps.isListType()) {
                Object rawValue = value.getValue();

                // check for empty lists
                if (rawValue instanceof Object[] && ((Object[]) rawValue).length == 0) {
                    AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);
                    TypedValue emptyList = typeFactory.toTypedValue(typeFactory.createElement(toType));
                    objectValue.put(key, emptyList);
                    continue;
                }

                Long listType = typeService.getType(toType).getList();
                Object[] list = ((Object[]) value.getValue());
                for (Object listItem : list) {
                    convertHashMap(typeService, (HashMap<TypedValue, TypedValue>) listItem, toType); // child hashmap; recurse
                }
                value.setInstanceType(listType);
                value.setValue(list);
            } else if (MAP_OR_DICTIONARY.contains(value.getInstanceType())) {
                TypedValue cValue = convert(typeService, value, toType); // child object; recurse
                objectValue.put(key, cValue);
            }
        }
    }
}
