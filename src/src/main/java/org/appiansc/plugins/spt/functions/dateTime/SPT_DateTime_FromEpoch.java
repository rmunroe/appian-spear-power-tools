package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;

import java.sql.Timestamp;


@DateTimeCategory
public class SPT_DateTime_FromEpoch {
    @Function
    public Timestamp spt_datetime_fromepoch(
            ContentService cs,
            @Parameter Long seconds
    ) {
        return new Timestamp(seconds * 1000L);
    }
}