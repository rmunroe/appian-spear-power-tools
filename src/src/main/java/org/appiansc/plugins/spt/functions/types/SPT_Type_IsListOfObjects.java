package org.appiansc.plugins.spt.functions.types;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Type_IsListOfObjects {
    private static final Logger LOG = Logger.getLogger(SPT_Type_IsListOfObjects.class);

    @Function
    public boolean spt_type_islistofobjects(
            TypeService ts,
            @Parameter TypedValue value
    ) {
        return AppianTypeHelper.isListOfObjects(ts, value);
    }
}
