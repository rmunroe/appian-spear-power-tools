package org.appiansc.plugins.spt.functions.objects;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.util.LinkedHashMap;
import java.util.Map;

@SptPluginCategory
public class SPT_Object_RemoveNulls {
    private static final Logger LOG = Logger.getLogger(SPT_Object_RemoveNulls.class);

    @Function
    public TypedValue spt_object_removenulls(
            TypeService typeService,          // injected dependency
            @Parameter TypedValue object,
            @Parameter(required = false) Boolean recursive
    ) {
        if (object.getInstanceType() != (long) AppianType.MAP && object.getInstanceType() != (long) AppianType.DICTIONARY)
            throw new UnsupportedTypeException("Only Maps and Dictionaries are supported");

        if (recursive == null) recursive = true;

        return removeNulls(typeService, object, recursive);
    }

    private TypedValue removeNulls(TypeService typeService, TypedValue object, Boolean recursive) {
        LinkedHashMap<TypedValue, TypedValue> objectValue = (LinkedHashMap<TypedValue, TypedValue>) object.getValue();
        LinkedHashMap<TypedValue, TypedValue> newValue = new LinkedHashMap<>();

        TypedValue[] keys = objectValue.keySet().toArray(new TypedValue[0]);
        for (TypedValue key : keys) {
            TypedValue propertyValue = objectValue.get(key);

            // Here is the actual null check that covers non-lists
            if (propertyValue.getValue() == null
                    || (propertyValue.getValue() instanceof LinkedHashMap<?, ?> && ((LinkedHashMap<?, ?>)propertyValue.getValue()).size() == 0) // empty child object
            ) continue;

            // Is this a list of child HashMaps?
            if (recursive && propertyValue.getValue() instanceof Map[]) {
                // get the Lists base type ID
                Long baseType = typeService.getType(propertyValue.getInstanceType()).getTypeof();

                for (int i = 0; i < ((Object[])propertyValue.getValue()).length; i++) {
                    Object childObj = ((Object[])propertyValue.getValue())[i];
                    LinkedHashMap<TypedValue, TypedValue> child = (LinkedHashMap<TypedValue, TypedValue>)childObj;

                    // We will use a temporary TypedValue to recurse
                    TypedValue newTv = new TypedValue();
                    newTv.setInstanceType(baseType);
                    newTv.setValue(child);

                    removeNulls(typeService, newTv, recursive);

                    // then retrieve the HashMap and replace the value
                    child = (LinkedHashMap<TypedValue, TypedValue>) newTv.getValue();
                    ((Object[])propertyValue.getValue())[i] = child;
                }
            }

            // Recurse?
            if (recursive && propertyValue.getValue() instanceof LinkedHashMap<?, ?>) {
                removeNulls(typeService, propertyValue, true);
            }

            // Store the non-null value on the new TypedValue
            newValue.put(key, propertyValue);
        }

        object.setValue(newValue);

        return object;
    }
}
