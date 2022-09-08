package org.appiansc.plugins.spt.functions.text;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Text_GetMetaphone {
    private static final Logger LOG = Logger.getLogger(SPT_Text_GetMetaphone.class);

    @Function
    public String spt_text_getmetaphone(
            @Parameter String text,
            @Parameter(required = false) Boolean doubleMetaphone
    ) {
        if (doubleMetaphone != null && doubleMetaphone)
            return (new DoubleMetaphone()).encode(text);
        else
            return (new Metaphone()).encode(text);
    }
}
