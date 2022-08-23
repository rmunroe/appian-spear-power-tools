package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.util.*;

@SptPluginCategory
public class SPT_List_Unique {
    private static final Logger LOG = Logger.getLogger(SPT_List_Unique.class);


    @Function
    public TypedValue spt_list_unique(
            TypeService typeService,          // injected dependency
            @Parameter TypedValue list
    ) {
        if (!ListHelper.isList(typeService, list)) return null;
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);

        AppianList appianList = ListHelper.getList(typeService, list, false);

        LinkedHashMap<String, AppianElement> map = new LinkedHashMap<>();
        Gson gson = new Gson();
        assert appianList != null;

        // Handle lists of Maps
        if (list.getInstanceType() == AppianType.LIST_OF_MAP) {
            appianList = AppianTypeHelper.mapListToDictList(typeFactory, list);
        }

        for (AppianElement element : appianList) {
            String json = gson.toJson(element);
            if (!map.containsKey(json)) {
                map.put(json, element);
            }
        }

        AppianList newList = typeFactory.createList(list.getInstanceType());
        newList.addAll(map.values());
        return typeFactory.toTypedValue(newList);
    }
}