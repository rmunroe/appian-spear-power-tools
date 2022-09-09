package org.appiansc.plugins.spt.functions.text;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;

@TextCategory
public class SPT_Text_GetMetaphone {
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
