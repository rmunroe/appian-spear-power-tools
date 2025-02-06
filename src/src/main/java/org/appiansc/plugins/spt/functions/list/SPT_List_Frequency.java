package org.appiansc.plugins.spt.functions.list;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
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
public class SPT_List_Frequency {
    @Function
    public TypedValue spt_list_frequency(
            TypeService ts,
            @Parameter TypedValue list
    ) throws Exception {
        if (!AppianTypeHelper.isList(ts, list)) throw new Exception("The value provided is not a List.");
        AppianList appianList = AppianListHelper.getList(ts, list);
        if (appianList == null || appianList.size() == 0) throw new Exception("The List provided must not be empty.");

        if (appianList.getTypeId() != AppianType.LIST_OF_DOUBLE &&
                appianList.getTypeId() != AppianType.LIST_OF_INTEGER &&
                appianList.getTypeId() != AppianType.LIST_OF_STRING &&
                appianList.getTypeId() != AppianType.LIST_OF_BOOLEAN &&
                appianList.getTypeId() != AppianType.LIST_OF_DATE &&
                appianList.getTypeId() != AppianType.LIST_OF_TIME &&
                appianList.getTypeId() != AppianType.LIST_OF_TIMESTAMP
        )
            throw new Exception("Unsupported List Type. Must be a List of Decimal, Integer, Text, Boolean, Date, Time, or Date and Time.");

        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);
        AppianObject dict = AppianTypeHelper.createDictionary(ts);

        Object[] innerList = (Object[]) list.getValue();

        for (int i = 0; i < innerList.length; i++) {
            String key = innerList[i].toString();
            if (list.getInstanceType() == AppianType.LIST_OF_BOOLEAN)
                key = (Objects.equals(key, "1")) ? "true" : "false";

            if (dict.containsKey(key))
                dict.put(key, typeFactory.createLong((Long) typeFactory.toTypedValue(dict.get(key)).getValue() + 1L));
            else
                dict.put(key, typeFactory.createLong(1L));
        }

        return typeFactory.toTypedValue(dict);
    }
}
