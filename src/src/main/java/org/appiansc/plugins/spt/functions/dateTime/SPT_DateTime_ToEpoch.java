package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import org.apache.log4j.Logger;

import java.sql.Timestamp;


@DateTimeCategory
public class SPT_DateTime_ToEpoch {
    private static final Logger LOG = Logger.getLogger(SPT_DateTime_ToEpoch.class);

    @Function
    public Long spt_datetime_toepoch(
            TypeService ts,
            @Parameter Timestamp dateTime
    ) {
        long epoch = dateTime.getTime();
        return epoch / 1000L;
    }
}