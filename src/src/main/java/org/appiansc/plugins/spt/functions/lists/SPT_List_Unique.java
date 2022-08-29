package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.AppianTypeConverter;
import org.appiansc.plugins.spt.AppianTypeHelper;
import org.appiansc.plugins.spt.SptPluginCategory;

import java.util.LinkedHashMap;

@SptPluginCategory
public class SPT_List_Unique {
    private static final Logger LOG = Logger.getLogger(SPT_List_Unique.class);

    @Function
    public TypedValue spt_list_unique(
            TypeService typeService,          // injected dependency
            @Parameter TypedValue list,
            @Parameter(required = false) boolean keepNulls
    ) {
        if (!ListHelper.isList(typeService, list)) return null;
        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(typeService);

        AppianList appianList = ListHelper.getList(typeService, list);

        LinkedHashMap<TypedValue, AppianElement> map = new LinkedHashMap<>();
        assert appianList != null;

        // Handle lists of Maps by turning into Dictionaries
        if (list.getInstanceType() == AppianType.LIST_OF_MAP) {
            list = AppianTypeConverter.convert(typeService, list, (long) AppianType.DICTIONARY);
            appianList = ListHelper.getList(typeService, list);
        }

        assert appianList != null;
        for (AppianElement element : appianList) {
            map.put(typeFactory.toTypedValue(element), element); // TypedValue has proper equals() so the map will contain unique values
        }

        AppianList newList = typeFactory.createList(list.getInstanceType());
        newList.addAll(map.values());
        return typeFactory.toTypedValue(newList);
    }
}