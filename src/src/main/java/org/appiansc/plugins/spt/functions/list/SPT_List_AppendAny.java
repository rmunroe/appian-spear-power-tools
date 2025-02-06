package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

import java.util.Objects;

@SptListCategory
public class SPT_List_AppendAny {
    @Function
    public TypedValue spt_list_appendany(
            TypeService ts,
            @Parameter TypedValue list,
            @Parameter TypedValue value
    ) {
        if (!AppianTypeHelper.isList(ts, list)) return value;
        AppianTypeHelper.fixNull(value);

        // Check if the type of 'value' is the same as the base type of the list
        if (!Objects.equals(ts.getType(list.getInstanceType()).getTypeof(), value.getInstanceType())) {
            // They don't match; cast to List of Variant and append
            list = ts.cast((long) AppianType.LIST_OF_VARIANT, list);
        }

        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);
        AppianList appianList = AppianListHelper.getList(ts, list);
        appianList.add(typeFactory.toAppianElement(value));

        return typeFactory.toTypedValue(appianList);
    }
}
