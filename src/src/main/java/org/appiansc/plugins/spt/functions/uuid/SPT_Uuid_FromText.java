package org.appiansc.plugins.spt.functions.uuid;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.enums.UuidNamespace;

@SptUuidCategory
public class SPT_Uuid_FromText {
    @Function
    public String spt_uuid_fromtext(@Parameter String text) {
        return UuidCreator.getNameBasedMd5(UuidNamespace.NAMESPACE_OID, text).toString();
    }
}
