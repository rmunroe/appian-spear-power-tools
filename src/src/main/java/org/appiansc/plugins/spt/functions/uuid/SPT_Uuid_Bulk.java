package org.appiansc.plugins.spt.functions.uuid;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;

import java.util.UUID;

@SptUuidCategory
public class SPT_Uuid_Bulk {
    @Function
    public String[] spt_uuid_bulk(@Parameter Long count) {
        String[] uuids = new String[Math.toIntExact(count)];
        for (int i = 0; i < count; i++) {
            uuids[i] = UUID.randomUUID().toString();
        }
        return uuids;
    }
}
