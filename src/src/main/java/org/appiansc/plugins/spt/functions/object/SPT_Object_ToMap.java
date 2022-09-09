package org.appiansc.plugins.spt.functions.object;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianTypeConverter;

@ObjectCategory
public class SPT_Object_ToMap {
    @Function
    public TypedValue spt_object_tomap(
            TypeService ts,
            @Parameter TypedValue object
    ) {
        DatatypeProperties props = ts.getDatatypeProperties(object.getInstanceType());
        if (!props.isRecordType()   // CDT
                && !(props.isListType() && ts.getDatatypeProperties(props.getTypeof()).isRecordType()) // List of CDT
                && object.getInstanceType() != (long) AppianType.MAP
                && object.getInstanceType() != (long) AppianType.LIST_OF_MAP
                && object.getInstanceType() != (long) AppianType.DICTIONARY
                && object.getInstanceType() != (long) AppianType.LIST_OF_DICTIONARY)
            throw new UnsupportedTypeException("Only Maps, Dictionaries, and CDTs are supported");

        return AppianTypeConverter.convert(ts, object, (long) AppianType.MAP);
    }
}
