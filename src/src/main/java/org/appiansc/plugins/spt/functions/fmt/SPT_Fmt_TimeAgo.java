package org.appiansc.plugins.spt.functions.fmt;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;
import org.ocpsoft.prettytime.PrettyTime;

import java.sql.Timestamp;
import java.util.Locale;


@FmtCategory
public class SPT_Fmt_TimeAgo {
    private static final Logger LOG = Logger.getLogger(SPT_Fmt_TimeAgo.class);

    @Function
    public String spt_fmt_timeago(
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