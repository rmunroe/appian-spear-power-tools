package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptListCategory
public class SPT_List_Slice {
    @Function
    public TypedValue spt_list_slice(
            TypeService ts,
            @Parameter TypedValue list,
            @Parameter int startIndex,
            @Parameter(required = false) int endIndex
    ) throws Exception {
        if (!AppianTypeHelper.isList(ts, list)) return null; // not a list!

        if (startIndex < 1) throw new Exception("startIndex cannot be less than 1"); // Appian Lists are 1-based
        if (endIndex != 0 && endIndex < startIndex) throw new Exception("endIndex cannot be less than startIndex");

        AppianList appianList = AppianListHelper.getList(ts, list);
        assert appianList != null;

        if (endIndex == 0) endIndex = appianList.size();

        startIndex--;
        endIndex--;

        for (int i = appianList.size() - 1; i >= 0; i--) {
            if (i > endIndex || i < startIndex)
                appianList.remove(i);
        }

        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(appianList);
    }
}
