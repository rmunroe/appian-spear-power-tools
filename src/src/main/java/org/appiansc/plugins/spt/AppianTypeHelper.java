package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.applications.Application;
import com.appiancorp.suiteapi.applications.ApplicationService;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
public class AppianTypeHelper {
    private static AppianTypeFactory typeFactory;

    public static final Map<String, Long> AppianTypeMap = Stream.of(new Object[][]{
            {"application", AppianTypeLong.APPLICATION},
            {"connectedSystem", AppianTypeLong.CONNECTED_SYSTEM},
            {"content", AppianTypeLong.CONTENT_ITEM},
            {"dataSource", AppianTypeLong.DATA_SOURCE},
            {"dataStore", AppianTypeLong.DATA_STORE},
            {"datatype", AppianTypeLong.DATATYPE},
            {"embeddedSailTheme", AppianTypeLong.EMBEDDED_SAIL_THEME_ID},
            {"forum", AppianTypeLong.FORUM},
            {"group", AppianTypeLong.GROUP},
            {"groupType", AppianTypeLong.GROUP_TYPE},
            {"page", AppianTypeLong.PAGE},
            {"processModel", AppianTypeLong.PROCESS_MODEL},
            {"processModelFolder", AppianTypeLong.PROCESS_MODEL_FOLDER},
            {"recordRelationship", AppianTypeLong.RECORD},
            {"recordType", AppianTypeLong.RECORD_TYPE_ID},
            {"site", AppianTypeLong.SITE},
            {"taskReport", AppianTypeLong.TASK_REPORT},
            {"tempoFeed", AppianTypeLong.TEMPO_FEED},
            {"tempoReport", AppianTypeLong.TEMPO_REPORT},
            {"user", AppianTypeLong.USERNAME},
            {"webApi", AppianTypeLong.WEB_API},
//            { "aiSkill", AppianTypeLong. },
//            { "featureFlag", AppianTypeLong.FEAT },
//            { "pluginInfo", AppianTypeLong.PLU },
//            { "portal", AppianTypeLong.PORTAL },
//            { "recordField", AppianTypeLong.FIE },
//            { "thirdPartyCredentials", AppianTypeLong.THIR },

    }).collect(Collectors.toMap(data -> (String) data[0], data -> (Long) data[1]));


    public static TypedValue primitiveToTypedValue(Object o) {
        if (o instanceof TypedValue)
            return (TypedValue) o;
        else if (o instanceof String)
            return new TypedValue(AppianTypeLong.STRING, o);
        else if (o instanceof Long)
            return new TypedValue(AppianTypeLong.INTEGER, o);
        else if (o instanceof Double)
            return new TypedValue(AppianTypeLong.DOUBLE, o);
        else if (o instanceof Boolean)
            return new TypedValue(AppianTypeLong.BOOLEAN, o);
        else if (o instanceof Timestamp)
            return new TypedValue(AppianTypeLong.TIMESTAMP, o);
        return null;
    }

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
                || Objects.equals(tv.getInstanceType(), AppianTypeLong.MAP)
                || Objects.equals(tv.getInstanceType(), AppianTypeLong.DICTIONARY)
        );
    }

    /**
     * Determines whether a TypedValue is a CDT, Dictionary, or Map
     *
     * @param ts     an injected TypeService instance
     * @param object the AppianElement to test
     * @return true if the TypedValue was a CDT, Dictionary, or Map
     */
    public static boolean isObject(TypeService ts, AppianElement object) {
        DatatypeProperties props = ts.getDatatypeProperties(object.getTypeId());
        return (props.isRecordType() // is CDT
                || Objects.equals(object.getTypeId(), AppianTypeLong.MAP)
                || Objects.equals(object.getTypeId(), AppianTypeLong.DICTIONARY)
        );
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
     * Returns true if the AppianElement passed in is a List type.
     *
     * @param ts     TypeService instance (usually injected)
     * @param object the AppianElement to test
     * @return true is type is a List
     */
    public static boolean isList(TypeService ts, AppianElement object) {
        return ts.getDatatypeProperties(object.getTypeId()).isListType();
    }


    /**
     * Returns true if the AppianElement passed in is a List of Dictionaries, Maps, or CDTs.
     *
     * @param ts     TypeService instance (usually injected)
     * @param object the AppianElement to test
     * @return true is type is a List of Dictionaries, Maps, or CDTs
     */
    public static boolean isListOfObjects(TypeService ts, AppianElement object) {
        DatatypeProperties props = ts.getDatatypeProperties(object.getTypeId());
        return ((props.isListType() && ts.getDatatypeProperties(props.getTypeof()).isRecordType()) // List of CDT
                || Objects.equals(object.getTypeId(), AppianTypeLong.LIST_OF_MAP)
                || Objects.equals(object.getTypeId(), AppianTypeLong.LIST_OF_DICTIONARY)
        );
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


    /**
     * Creates an empty Dictionary
     *
     * @return an empty Dictionary
     */
    public static AppianObject createDictionary(TypeService ts) {
        getTypeFactory(ts);
        return (AppianObject) typeFactory.createElement(AppianType.DICTIONARY);
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
     * @param ts           an injected TypeService instance
     * @param object       the Dictionary or Map to remove the property from
     * @param propertyName the name of the property to remove
     * @return the object with the property removed
     */
    public static TypedValue removeProperty(TypeService ts, TypedValue object, String propertyName) {
        TypedValue propKey = new TypedValue((long) AppianType.STRING);
        propKey.setValue(propertyName);

        ((LinkedHashMap<TypedValue, TypedValue>) object.getValue()).remove(propKey);

        return object;
    }


    /**
     * Renamed an object property by moving it to a new entry
     *
     * @param object  A Map or Dictionary
     * @param oldName The key name to change
     * @param newName The new key name
     * @return The object
     */
    public static TypedValue renameProperty(TypedValue object, String oldName, String newName) {
        TypedValue oldKey = new TypedValue((long) AppianType.STRING);
        oldKey.setValue(oldName);
        TypedValue newKey = new TypedValue((long) AppianType.STRING);
        newKey.setValue(newName);

        TypedValue value = ((LinkedHashMap<TypedValue, TypedValue>) object.getValue()).get(oldKey);

        ((LinkedHashMap<TypedValue, TypedValue>) object.getValue()).put(newKey, value);

        return object;
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
     * Creates a Dictionary from an Application object
     *
     * @param as          ApplicationService instance
     * @param ts          TypeService instance
     * @param application Application instance
     * @return Dictionary
     */
    public static AppianObject appToDictionary(ApplicationService as, TypeService ts, Application application) {
        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);

        AppianObject app = (AppianObject) typeFactory.createElement(AppianType.DICTIONARY);
        app.put("id", typeFactory.createLong(application.getId()));
        app.put("name", typeFactory.createString(application.getName()));
        app.put("displayName", typeFactory.createString(application.getDisplayName()));
        app.put("description", typeFactory.createString(application.getDescription()));
        app.put("companyName", typeFactory.createString(application.getCompanyName()));
        app.put("companyUrl", typeFactory.createString(application.getCompanyUrl()));
        app.put("creator", typeFactory.createString(application.getCreator()));
        app.put("prefix", typeFactory.createString(application.getPrefix()));
        app.put("typeId", typeFactory.createLong((long) application.getType()));
        app.put("uuid", typeFactory.createString(application.getUuid()));
        app.put("urlIdentifier", typeFactory.createString(application.getUrlIdentifier()));
        app.put("lastModifiedBy", typeFactory.createString(application.getLastModifiedBy()));

        Application parent;
        try {
            parent = as.getApplication(application.getParent());
            app.put("parentId", typeFactory.createLong(application.getParent()));
            app.put("parentName", typeFactory.createString(application.getParentName()));
            app.put("parentUuid", typeFactory.createString(parent.getUuid()));
        } catch (Exception ignored) {
        }

        AppianList relatedApps = typeFactory.createList(AppianTypeLong.STRING);
        for (String appUuid : application.getAssociatedApplications().getApplications()) {
            relatedApps.add(typeFactory.createString(appUuid));
        }
        app.put("relatedApplications", relatedApps);

        return app;
    }
}

