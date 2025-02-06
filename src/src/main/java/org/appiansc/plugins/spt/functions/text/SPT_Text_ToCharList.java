package org.appiansc.plugins.spt.functions.text;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;

@SptTextCategory
public class SPT_Text_ToCharList {
    @Function
    public TypedValue spt_text_tocharlist(
            @Parameter String text
    ) {
        return new TypedValue(AppianTypeLong.LIST_OF_STRING, text.split(""));
    }
}
