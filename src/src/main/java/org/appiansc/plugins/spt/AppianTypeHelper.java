package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class AppianTypeHelper {
    private static final Logger LOG = Logger.getLogger(AppianTypeHelper.class);

    private static AppianTypeFactory typeFactory;


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

    public static AppianObject createDictionary(TypeService ts) {
        return (AppianObject) getTypeFactory(ts).createElement(AppianType.DICTIONARY);
    }

    public static TypedValue toMap(TypeService ts, TypedValue dictionary) {
        return AppianTypeConverter.convert(ts, dictionary, (long) AppianType.MAP);
    }

    public static TypedValue toMap(TypeService ts, AppianObject dictionary) {
        return toMap(ts, getTypeFactory(ts).toTypedValue(dictionary));
    }



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

                    removeNullProperties(ts, newTv, recursive);

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
}

