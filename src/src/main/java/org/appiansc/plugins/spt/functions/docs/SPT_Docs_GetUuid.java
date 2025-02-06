package org.appiansc.plugins.spt.functions.docs;

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


@SptDocsCategory
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
            return doc.getUuid();
        } catch (InvalidContentException | InvalidVersionException | PrivilegeException e) {
            LOG.error("No Document found in Appian with ID " + document);
            return null;
        }
    }
}