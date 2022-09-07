package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;


@SptPluginCategory
public class SPT_DateTime_FromEpoch {
    private static final Logger LOG = Logger.getLogger(SPT_DateTime_FromEpoch.class);

    @Function
    public Timestamp spt_datetime_fromepoch(
            ContentService cs,
            @Parameter Long seconds
    ) {
        return new Timestamp(seconds * 1000L);
    }
}