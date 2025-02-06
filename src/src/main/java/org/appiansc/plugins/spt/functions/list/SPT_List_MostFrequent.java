package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianListHelper;
import org.appiansc.plugins.spt.AppianTypeHelper;

import java.util.*;

@SptListCategory
public class SPT_List_MostFrequent {
    @Function
    public TypedValue spt_list_mostfrequent(
            TypeService ts,
            @Parameter TypedValue list
    ) throws Exception {
        if (!AppianTypeHelper.isList(ts, list)) return null;
        AppianList appianList = AppianListHelper.getList(ts, list);
        if (appianList == null || appianList.size() == 0) return null;

        Object[] innerList = (Object[]) list.getValue();

        Map<Object, Integer> map = new HashMap<Object, Integer>();
        for (Object i : innerList) {
            Integer count = map.get(i);
            map.put(i, count != null ? count + 1 : 1);
        }

        Integer topCount = Collections.max(map.entrySet(), Map.Entry.comparingByValue()).getValue();

        List<Object> keys = new ArrayList<>();
        for (Map.Entry<Object, Integer> entry : map.entrySet())
            if (entry.getValue().equals(topCount)) keys.add(entry.getKey());

        DatatypeProperties props = ts.getDatatypeProperties(list.getInstanceType());
        Long typeId = props.getTypeof();

        if (keys.size() == 1)
            return new TypedValue(typeId, keys.get(0));
        else
            return new TypedValue(appianList.getTypeId(), keys.toArray());
    }
}
