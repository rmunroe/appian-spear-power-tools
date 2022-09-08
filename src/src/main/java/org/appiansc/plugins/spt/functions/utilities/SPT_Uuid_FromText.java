package org.appiansc.plugins.spt.functions.utilities;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.enums.UuidNamespace;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

@SptPluginCategory
public class SPT_Uuid_FromText {
    private static final Logger LOG = Logger.getLogger(SPT_Uuid_FromText.class);

    @Function
    public String spt_uuid_fromtext(@Parameter String text) {
        return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_OID, text).toString();
    }
}
