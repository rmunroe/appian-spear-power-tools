package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptListCategory
public class SPT_List_Unique {
    @Function
    public TypedValue spt_list_unique(
            TypeService ts,
            @Parameter TypedValue list,
            @Parameter(required = false) boolean keepNulls
    ) {
        if (!AppianTypeHelper.isList(ts, list)) return list;

        return AppianListHelper.getUniqueListValues(ts, list);
    }
}