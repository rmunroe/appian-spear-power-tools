package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptListCategory
public class SPT_List_Count {
    @Function
    public Long spt_list_count(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        if (!AppianTypeHelper.isList(ts, list)) return 0L;
        AppianList appianList = AppianListHelper.getList(ts, list);
        if (appianList == null || appianList.size() == 0) return 0L;
        return (long) appianList.size();
    }
}
