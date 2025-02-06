package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptListCategory
public class SPT_List_Last {
    @Function
    public TypedValue spt_list_last(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        if (!AppianTypeHelper.isList(ts, list)) return list;

        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);
        AppianList appianList = AppianListHelper.getList(ts, list, true);
        if (appianList == null || appianList.size() == 0) return null;
        return typeFactory.toTypedValue(appianList.get(appianList.size() - 1));
    }
}
