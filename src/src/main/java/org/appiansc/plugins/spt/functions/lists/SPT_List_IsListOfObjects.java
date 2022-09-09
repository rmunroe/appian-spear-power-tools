package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_List_IsListOfObjects {
    private static final Logger LOG = Logger.getLogger(SPT_List_IsListOfObjects.class);

    @Function
    public boolean spt_list_islistofobjects(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        return AppianListHelper.isListOfObjects(ts, list);
    }
}
