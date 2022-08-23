package org.appiansc.plugins.spt.functions.utilities;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.enums.UuidNamespace;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Uuid_FromStrings {
    private static final Logger LOG = Logger.getLogger(SPT_Uuid_FromStrings.class);

    @Function
    public String[] spt_uuid_fromstrings(@Parameter String... strings) {
        String[] uuids = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            uuids[i] = UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_OID, strings[i]).toString();
        }
        return uuids;
    }
}
