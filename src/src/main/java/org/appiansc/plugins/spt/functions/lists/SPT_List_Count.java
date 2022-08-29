package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_List_Count {
    private static final Logger LOG = Logger.getLogger(SPT_List_Count.class);

    @Function
    public Long spt_list_count(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        if (!AppianListHelper.isList(ts, list)) return 0L;
        AppianList appianList = AppianListHelper.getList(ts, list);
        if (appianList == null || appianList.size() == 0) return 0L;
        return (long) appianList.size();
    }
}
