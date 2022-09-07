package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeConverter;
import org.appiansc.plugins.spt.AppianTypeHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.sql.Timestamp;


@SptPluginCategory
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