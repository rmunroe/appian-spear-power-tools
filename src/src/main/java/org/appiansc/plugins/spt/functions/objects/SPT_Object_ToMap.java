package org.appiansc.plugins.spt.functions.objects;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeConverter;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Object_ToMap {
    private static final Logger LOG = Logger.getLogger(SPT_Object_ToMap.class);

    @Function
    public TypedValue spt_object_tomap(
            TypeService typeService,          // injected dependency
            @Parameter TypedValue object
    ) throws Exception {
        DatatypeProperties props = typeService.getDatatypeProperties(object.getInstanceType());
        if (!props.isRecordType()   // CDT
                && !(props.isListType() && typeService.getDatatypeProperties(props.getTypeof()).isRecordType()) // List of CDT
                && object.getInstanceType() != (long) AppianType.MAP
                && object.getInstanceType() != (long) AppianType.LIST_OF_MAP
                && object.getInstanceType() != (long) AppianType.DICTIONARY
                && object.getInstanceType() != (long) AppianType.LIST_OF_DICTIONARY)
            throw new UnsupportedTypeException("Only Maps, Dictionaries, and CDTs are supported");

        return AppianTypeConverter.convert(typeService, object, (long) AppianType.MAP);
    }
}
