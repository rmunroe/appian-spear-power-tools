package org.appiansc.plugins.spt.functions.types;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianTypeHelper;


@SptTypeCategory
public class SPT_Type_IsDecimal {
    @Function
    public Boolean spt_type_isdecimal(
            @Parameter TypedValue value
    ) {
        return AppianTypeHelper.isDecimal(value);
    }
}