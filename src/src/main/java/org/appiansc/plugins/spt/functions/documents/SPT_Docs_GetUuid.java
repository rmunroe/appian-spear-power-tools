package org.appiansc.plugins.spt.functions.documents;

import com.appiancorp.suiteapi.common.exceptions.InvalidVersionException;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.content.ContentConstants;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.content.exceptions.InvalidContentException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.Document;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;


@SptPluginCategory
public class SPT_Docs_GetUuid {
    private static final Logger LOG = Logger.getLogger(SPT_Docs_GetUuid.class);

    @Function
    public String spt_docs_getuuid(
            ContentService cs,
            @Parameter @DocumentDataType Long document
    ) {
        Document doc;

        try {
            doc = (Document) cs.getVersion(document, ContentConstants.VERSION_CURRENT);
        } catch (InvalidContentException | InvalidVersionException | PrivilegeException e) {
            throw new RuntimeException(e);
        }

        return doc.getUuid();
    }
}