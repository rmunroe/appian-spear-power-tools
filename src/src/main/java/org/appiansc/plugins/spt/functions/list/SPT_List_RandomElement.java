package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

import java.util.Collections;

@ListCategory
public class SPT_List_RandomElement {
    @Function
    public TypedValue spt_list_randomelement(
            TypeService ts,
            @Parameter TypedValue list,
            @Parameter(required = false) int count,
            @Parameter(required = false) boolean unique
    ) throws Exception {
        if (!AppianTypeHelper.isList(ts, list)) return list;

        AppianList inputList = AppianListHelper.getList(ts, list);
        if (inputList == null || inputList.size() == 0) return null;
        if (count == 0) count = 1;

        if (unique && count > inputList.size())
            throw new Exception("Cannot return " + count + " unique elements when the list count is less (" + inputList.size() + ")");

        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);

        if (count == 1) {
            Collections.shuffle(inputList);                    // randomize the array
            return typeFactory.toTypedValue(inputList.get(0)); // get first element
        }

        AppianList newList = typeFactory.createList(inputList.getTypeId());

        if (unique) {
            Collections.shuffle(inputList);     // randomize the array
            for (int i = 0; i < count; i++) {
                newList.add(inputList.get(i));
            }
        } else {
            for (int i = 0; i < count; i++) {
                Collections.shuffle(inputList); // randomize the array
                newList.add(inputList.get(0));  // grab first
            }
        }

        return typeFactory.toTypedValue(newList);
    }
}
