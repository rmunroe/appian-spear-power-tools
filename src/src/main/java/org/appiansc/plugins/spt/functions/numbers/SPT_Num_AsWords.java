package org.appiansc.plugins.spt.functions.numbers;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;
import pl.allegro.finance.tradukisto.MoneyConverters;
import pl.allegro.finance.tradukisto.ValueConverters;

import java.math.BigDecimal;

@SptPluginCategory
public class SPT_Num_AsWords {
    private static final Logger LOG = Logger.getLogger(SPT_Num_AsWords.class);

    @Function
    public String spt_num_aswords(
            @Parameter TypedValue number,
            @Parameter String converterName
    ) throws Exception {
        if (converterName == null || converterName.isEmpty())
            throw new Exception("converterName cannot be empty. Please see full documentation for more details.");
        if (!(number.getValue() instanceof Long) && !(number.getValue() instanceof Double))
            throw new Exception("Only Integer and Decimal values can be used for number. Please see full documentation for more details.");

        if (converterName.endsWith("_INTEGER")) {
            if (number.getValue() instanceof Double) {
                return getFromLong(((Double) number.getValue()).longValue(), converterName);
            } else {
                return getFromLong((Long) number.getValue(), converterName);
            }
        } else if (converterName.endsWith("_BANKING_MONEY_VALUE")) {
            if (number.getValue() instanceof Long) {
                return getFromDouble(((Long) number.getValue()).doubleValue(), converterName);
            } else {
                return getFromDouble((Double) number.getValue(), converterName);
            }
        }

        throw new Exception("Unable to determine which converter to use. Please see full documentation for more details.");
    }

    private String getFromLong(Long number, String converterName) {
        ValueConverters converter = ValueConverters.valueOf(converterName);
        return converter.asWords(Math.toIntExact(number));
    }

    private String getFromDouble(Double number, String converterName) {
        MoneyConverters converter = MoneyConverters.valueOf(converterName);
        return converter.asWords(BigDecimal.valueOf(number));
    }
}
