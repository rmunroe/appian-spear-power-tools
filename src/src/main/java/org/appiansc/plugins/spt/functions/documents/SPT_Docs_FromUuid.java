package org.appiansc.plugins.spt.functions.documents;

import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;


@SptPluginCategory
public class SPT_Docs_FromUuid {
    private static final Logger LOG = Logger.getLogger(SPT_Docs_FromUuid.class);

    @Function
    public @DocumentDataType Long spt_docs_fromuuid(
            ContentService cs,
            @Parameter String uuid
    ) {
        Long id = cs.getIdByUuid(uuid);
        if (id == 0) {
            LOG.error("No Document found in Appian with UUID " + uuid);
            return null;
        }
        return id;
    }
}