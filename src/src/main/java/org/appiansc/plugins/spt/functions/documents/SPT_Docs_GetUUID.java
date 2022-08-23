package org.appiansc.plugins.spt.functions.documents;

import com.appiancorp.suiteapi.common.exceptions.InvalidVersionException;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.content.ContentConstants;
import com.appiancorp.suiteapi.content.exceptions.InvalidContentException;
import com.appiancorp.suiteapi.knowledge.Document;
import org.appiansc.plugins.spt.SptPluginCategory;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.knowledge.DocumentDataType;
import org.apache.log4j.Logger;


@SptPluginCategory
public class SPT_Docs_GetUUID {
    private static final Logger LOG = Logger.getLogger(SPT_Docs_GetUUID.class);

    @Function
    public String spt_docs_getuuid(
            ContentService contentService,     // injected dependency
            @Parameter @DocumentDataType Long document
    ) {
        Document doc = null;

        try {
            doc = (Document) contentService.getVersion(document, ContentConstants.VERSION_CURRENT);
        } catch (InvalidContentException | InvalidVersionException | PrivilegeException e) {
            throw new RuntimeException(e);
        }

        return doc.getUuid();
    }
}