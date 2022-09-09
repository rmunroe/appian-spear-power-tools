package org.appiansc.plugins.spt.functions.types;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeHelper;


@TypeCategory
public class SPT_Type_IsNumeric {
    private static final Logger LOG = Logger.getLogger(SPT_Type_IsNumeric.class);

    @Function
    public Boolean spt_type_isnumeric(
            @Parameter TypedValue value
    ) {
        return AppianTypeHelper.isNumeric(value);
    }
}