package org.appiansc.plugins.spt.functions.utilities;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;

import java.util.UUID;

@UuidCategory
public class SPT_Uuid_Bulk {
    private static final Logger LOG = Logger.getLogger(SPT_Uuid_Bulk.class);

    @Function
    public String[] spt_uuid_bulk(@Parameter Long count) {
        String[] uuids = new String[Math.toIntExact(count)];
        for (int i = 0; i < count; i++) {
            uuids[i] = UUID.randomUUID().toString();
        }
        return uuids;
    }
}
