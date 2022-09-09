package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

import java.util.Collections;

@ListCategory
public class SPT_List_Randomize {
    @Function
    public TypedValue spt_list_randomize(
            TypeService ts,
            @Parameter TypedValue list
    ) {
        if (!AppianTypeHelper.isList(ts, list)) return list;

        AppianList inputList = AppianListHelper.getList(ts, list);

        assert inputList != null;
        Collections.shuffle(inputList); // randomize the array

        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(inputList);
    }
}
