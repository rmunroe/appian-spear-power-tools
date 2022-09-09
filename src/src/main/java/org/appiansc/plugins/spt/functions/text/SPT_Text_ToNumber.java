package org.appiansc.plugins.spt.functions.text;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Text_ToNumber {
    private static final Logger LOG = Logger.getLogger(SPT_Text_ToNumber.class);

    @Function
    public TypedValue spt_text_tonumber(
            @Parameter String text
    ) {
        try {
            // Try to parse as Integer; if the value is a double ("1.23") it will fail
            long parseLong = Long.parseLong(text);
            return new TypedValue((long) AppianType.INTEGER, parseLong);
        } catch (Exception ignored) {
            try {
                // Try to parse as Double
                double parseDouble = Double.parseDouble(text);
                return new TypedValue((long) AppianType.DOUBLE, parseDouble);
            } catch (Exception ignored2) {
            }
        }
        return null;
    }
}
