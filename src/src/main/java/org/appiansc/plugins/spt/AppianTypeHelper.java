package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class AppianTypeHelper {
    private static AppianTypeFactory typeFactory;


    /**
     * Determines whether the given value is an Integer
     *
     * @param value the TypedValue to test
     * @return true if the value is considered an Integer
     */
    public static boolean isInteger(TypedValue value) {
        if (value.getInstanceType() == AppianType.STRING) {
            // Can we turn it into an Integer and is the value the same as the input when stringified?
            try {
                long parseLong = Long.parseLong(value.getValue().toString());
                return Long.toString(parseLong).equals(value.getValue().toString());
            } catch (Exception ignored) {
            }
            return false;
        } else {
            return value.getInstanceType() == AppianType.INTEGER
                    || value.getInstanceType() == AppianType.LIST_OF_INTEGER;
        }
    }


    /**
     * Determines whether the given value is a Decimal
     *
     * @param value the TypedValue to test
     * @return true if the value is considered a Decimal
     */
    public static boolean isDecimal(TypedValue value) {
        if (value.getInstanceType() == AppianType.STRING) {
            // Can we turn it into a Decimal and is the value the same as the input when stringified?
            try {
                double parseDouble = Double.parseDouble(value.getValue().toString());
                return Double.toString(parseDouble).equals(value.getValue().toString());
            } catch (Exception ignored) {
            }
            return false;
        } else {
            return value.getInstanceType() == AppianType.DOUBLE
                    || value.getInstanceType() == AppianType.LIST_OF_DOUBLE;
        }
    }

    /**
     * Determines whether the given value is numeric
     *
     * @param value the TypedValue to test
     * @return true if the value is considered numeric
     */
    public static boolean isNumeric(TypedValue value) {
        return isInteger(value) || isDecimal(value);
    }


    /**
     * Determines whether a TypedValue is a CDT, Dictionary, or Map
     *
     * @param ts an injected TypeService instance
     * @param tv the TypedValue to test
     * @return true if the TypedValue was a CDT, Dictionary, or Map
     */
    public static boolean isObject(TypeService ts, TypedValue tv) {
        DatatypeProperties props = ts.getDatatypeProperties(tv.getInstanceType());
        return (props.isRecordType() // is CDT
                || tv.getInstanceType() == (long) AppianType.MAP
                || tv.getInstanceType() == (long) AppianType.DICTIONARY
        );
    }


    /**
     * Converts an empty string to null
     *
     * @param value any TypedValue
     */
    public static void fixNull(TypedValue value) {
        if (value.getInstanceType() == AppianType.STRING && value.getValue().equals(""))
            value.setInstanceType((long) AppianType.NULL);
    }


    /**
     * Static singleton AppianTypeFactory
     *
     * @param ts an injected TypeService instance
     * @return a AppianTypeFactory instance
     */
    public static AppianTypeFactory getTypeFactory(TypeService ts) {
        if (typeFactory == null) typeFactory = AppianTypeFactory.newInstance(ts);
        return typeFactory;
    }


    /**
     * Removes all properties with null values (optionally recursively into nested objects)
     *
     * @param ts        an injected TypeService instance
     * @param object    the Dictionary or Map to remove null properties from
     * @param recursive whether to process nested objects
     * @return the object without null properties
     */
    public static TypedValue removeNullProperties(TypeService ts, TypedValue object, Boolean recursive) {
        LinkedHashMap<TypedValue, TypedValue> objectValue = (LinkedHashMap<TypedValue, TypedValue>) object.getValue();
        LinkedHashMap<TypedValue, TypedValue> newValue = new LinkedHashMap<>();

        TypedValue[] keys = objectValue.keySet().toArray(new TypedValue[0]);
        for (TypedValue key : keys) {
            TypedValue propertyValue = objectValue.get(key);

            // Here is the actual null check that covers non-lists
            if (propertyValue.getValue() == null
                    || (propertyValue.getValue() instanceof LinkedHashMap<?, ?> && ((LinkedHashMap<?, ?>) propertyValue.getValue()).size() == 0) // empty child object
            ) continue;

            // Is this a list of child HashMaps?
            if (recursive && propertyValue.getValue() instanceof Map[]) {
                // get the Lists base type ID
                Long baseType = ts.getType(propertyValue.getInstanceType()).getTypeof();

                for (int i = 0; i < ((Object[]) propertyValue.getValue()).length; i++) {
                    Object childObj = ((Object[]) propertyValue.getValue())[i];
                    LinkedHashMap<TypedValue, TypedValue> child = (LinkedHashMap<TypedValue, TypedValue>) childObj;

                    // We will use a temporary TypedValue to recurse
                    TypedValue newTv = new TypedValue();
                    newTv.setInstanceType(baseType);
                    newTv.setValue(child);

                    removeNullProperties(ts, newTv, true);

                    // then retrieve the HashMap and replace the value
                    child = (LinkedHashMap<TypedValue, TypedValue>) newTv.getValue();
                    ((Object[]) propertyValue.getValue())[i] = child;
                }
            }

            // Recurse?
            if (recursive && propertyValue.getValue() instanceof LinkedHashMap<?, ?>) {
                removeNullProperties(ts, propertyValue, true);
            }

            // Store the non-null value on the new TypedValue
            newValue.put(key, propertyValue);
        }

        object.setValue(newValue);

        return object;
    }

    /**
     * Returns true if the TypedValue passed in is a List type.
     *
     * @param ts TypeService instance (usually injected)
     * @param tv any Appian TypedValue (any type)
     * @return true is type is a List
     */
    public static boolean isList(TypeService ts, TypedValue tv) {
        return ts.getDatatypeProperties(tv.getInstanceType()).isListType();
    }

    /**
     * Returns true if the TypedValue passed in is a List of Dictionaries, Maps, or CDTs.
     *
     * @param ts TypeService instance (usually injected)
     * @param tv any Appian TypedValue (any type)
     * @return true is type is a List of Dictionaries, Maps, or CDTs
     */
    public static boolean isListOfObjects(TypeService ts, TypedValue tv) {
        DatatypeProperties props = ts.getDatatypeProperties(tv.getInstanceType());
        return ((props.isListType() && ts.getDatatypeProperties(props.getTypeof()).isRecordType()) // List of CDT
                || tv.getInstanceType() == (long) AppianType.LIST_OF_MAP
                || tv.getInstanceType() == (long) AppianType.LIST_OF_DICTIONARY
        );
    }
}

