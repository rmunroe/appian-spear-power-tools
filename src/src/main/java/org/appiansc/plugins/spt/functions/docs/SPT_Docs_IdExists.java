package org.appiansc.plugins.spt.functions.docs;

import com.appiancorp.suiteapi.common.exceptions.InvalidVersionException;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.content.Content;
import com.appiancorp.suiteapi.content.ContentConstants;
import com.appiancorp.suiteapi.content.ContentService;
import com.appiancorp.suiteapi.content.exceptions.InvalidContentException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import org.apache.log4j.Logger;


@SptDocsCategory
public class SPT_Docs_IdExists {
    private static final Logger LOG = Logger.getLogger(SPT_Docs_IdExists.class);

    @Function
    public Boolean spt_docs_idexists(
            ContentService cs,
            @Parameter Long documentId
    ) {
        Content doc;
        try {
            doc = cs.getVersion(documentId, ContentConstants.VERSION_CURRENT);
            return true;
        } catch (InvalidContentException | InvalidVersionException | PrivilegeException ignored) {
            return false;
        }
    }
}