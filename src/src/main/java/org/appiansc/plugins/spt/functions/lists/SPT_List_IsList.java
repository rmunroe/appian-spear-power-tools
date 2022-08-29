package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_List_IsList {
    private static final Logger LOG = Logger.getLogger(SPT_List_IsList.class);

    @Function
    public boolean spt_list_islist(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        return AppianListHelper.isList(ts, list);
    }
}
