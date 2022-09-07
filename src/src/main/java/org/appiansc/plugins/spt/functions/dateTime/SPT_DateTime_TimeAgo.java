package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;
import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.util.Locale;


@SptPluginCategory
public class SPT_DateTime_TimeAgo {
    private static final Logger LOG = Logger.getLogger(SPT_DateTime_TimeAgo.class);

    @Function
    public String spt_datetime_timeago(
            ContentService cs,
            @Parameter Timestamp dateTime,
            @Parameter(required = false) String locale
    ) {
        PrettyTime p;
        if (locale != null && !locale.isEmpty()) p = new PrettyTime(new Locale(locale));
        else p = new PrettyTime();
        return p.format(dateTime);
    }
}