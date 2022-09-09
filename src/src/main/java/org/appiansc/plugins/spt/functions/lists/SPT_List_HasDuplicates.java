package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_List_HasDuplicates {
    private static final Logger LOG = Logger.getLogger(SPT_List_HasDuplicates.class);

    @Function
    public boolean spt_list_hasduplicates(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        if (!AppianTypeHelper.isList(ts, list) || list.getValue() == null) return false;

        AppianList inputList = AppianListHelper.getList(ts, list);
        if (inputList.size() == 0) return false;

        AppianList uniqueList = AppianListHelper.getList(ts, AppianListHelper.getUniqueListValues(ts, list));

        return inputList.size() != uniqueList.size();
    }
}