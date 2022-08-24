package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SptPluginCategory
public class SPT_List_RemoveNulls {
    private static final Logger LOG = Logger.getLogger(SPT_List_RemoveNulls.class);

    @Function
    public TypedValue spt_list_removenulls(
            TypeService typeService,          // injected dependency
            @Parameter TypedValue list
    ) {
        if (!ListHelper.isList(typeService, list)) return list;
        AppianList appianList = ListHelper.getList(typeService, list, false);
        if (appianList == null || appianList.size() == 0) return null;

        return ListHelper.removeNulls(list);
    }
}
