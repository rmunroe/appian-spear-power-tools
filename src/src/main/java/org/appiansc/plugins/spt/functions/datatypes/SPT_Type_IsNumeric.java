package org.appiansc.plugins.spt.functions.datatypes;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;


@SptPluginCategory
public class SPT_Type_IsNumeric {
    private static final Logger LOG = Logger.getLogger(SPT_Type_IsNumeric.class);

    @Function
    public Boolean spt_type_isnumeric(
            @Parameter TypedValue value
    ) {
        if (value.getInstanceType() == AppianType.STRING) {
            // Can we turn it into a Double or Integer and are those values the same as the input when stringified?
            double parseDouble = 0;
            try {
                parseDouble = Double.parseDouble(value.getValue().toString());
            } catch (Exception ignored) {
            }
            long parseLong = 0;
            try {
                parseLong = Long.parseLong(value.getValue().toString());
            } catch (Exception ignored) {
            }
            return Double.toString(parseDouble).equals(value.getValue().toString())
                    || Long.toString(parseLong).equals(value.getValue().toString());
        } else {
            return value.getInstanceType() == AppianType.INTEGER
                    || value.getInstanceType() == AppianType.DOUBLE
                    || value.getInstanceType() == AppianType.LIST_OF_INTEGER
                    || value.getInstanceType() == AppianType.LIST_OF_DOUBLE;
        }
    }
}