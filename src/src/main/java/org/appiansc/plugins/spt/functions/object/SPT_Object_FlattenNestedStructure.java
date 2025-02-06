package org.appiansc.plugins.spt.functions.object;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;
import org.appiansc.plugins.spt.AppianTypeConverter;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptObjectCategory
public class SPT_Object_FlattenNestedStructure {
    /*
    @Function
    public TypedValue spt_object_flattennestedstructure(
            TypeService ts,
            @Parameter TypedValue object
    ) {
        DatatypeProperties props = ts.getDatatypeProperties(object.getInstanceType());
        if (!props.isRecordType()   // CDT
                && !(props.isListType() && ts.getDatatypeProperties(props.getTypeof()).isRecordType()) // List of CDT
                && object.getInstanceType() != AppianType.MAP
                && object.getInstanceType() != AppianType.LIST_OF_MAP
                && object.getInstanceType() != AppianType.DICTIONARY
                && object.getInstanceType() != AppianType.LIST_OF_DICTIONARY)
            throw new UnsupportedTypeException("Only Maps, Dictionaries, and CDTs are supported");

        AppianObject flattened = AppianTypeHelper.createDictionary(ts);

        flattenObject(ts, flattened, object, "");

        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(flattened);
    }


    public static void flattenObject(TypeService ts, AppianObject flattened, TypedValue object, String prefix) {
        if (AppianTypeHelper.isListOfObjects(ts, object) && object.getValue() instanceof TypedValue) {
            TypedValue[] arr = (TypedValue[]) object.getValue();
            for (int i = 0; i < arr.length; i++) {
                String newKey = prefix.isEmpty() ? String.valueOf(i + 1) : prefix + "_" + (i + 1);
                flattenObject(ts, flattened, arr[i], newKey);
            }
        } else if (AppianTypeHelper.isList(ts, object)) {
            Object[] arr = (Object[]) object.getValue();
            for (int i = 0; i < arr.length; i++) {
                String newKey = prefix.isEmpty() ? String.valueOf(i + 1) : prefix + "_" + (i + 1);
                flattened.put(
                        newKey,
                        AppianTypeHelper.getTypeFactory(ts).toAppianElement(
                                AppianTypeHelper.primitiveToTypedValue(arr[i])
                        )
                );
            }
        } else {
            if (AppianTypeHelper.isObject(ts, object) && object.getInstanceType().equals(Appl))
                object = AppianTypeConverter.convert(ts, object, AppianTypeLong.DICTIONARY);

            AppianObject dict = (AppianObject) AppianTypeHelper.getTypeFactory(ts).toAppianElement(object);
            for (String key : dict.keySet()) {
                String newKey = prefix.isEmpty() ? key : prefix + "_" + key;
                AppianElement value = dict.get(key);
                if (AppianTypeHelper.isObject(ts, value) || AppianTypeHelper.isList(ts, value)) {
                    flattenObject(ts, flattened, AppianTypeHelper.getTypeFactory(ts).toTypedValue(value), newKey);
                } else {
                    flattened.put(newKey, value);
                }
            }
        }
    }
     */
}