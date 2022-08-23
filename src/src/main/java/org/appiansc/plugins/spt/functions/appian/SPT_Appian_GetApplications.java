package org.appiansc.plugins.spt.functions.appian;

import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.applications.Application;
import com.appiancorp.suiteapi.applications.ApplicationService;
import com.appiancorp.suiteapi.common.Constants;
import com.appiancorp.suiteapi.common.ResultPage;
import com.appiancorp.suiteapi.content.ContentConstants;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;


@SptPluginCategory
public class SPT_Appian_GetApplications {
    private static final Logger LOG = Logger.getLogger(SPT_Appian_GetApplications.class);

    @Function
    public TypedValue spt_appian_getapplications(
            ApplicationService appService,    // injected dependency
            TypeService typeService           // injected dependency
    ) {
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);

        ResultPage result = appService.getApplicationsPaging(
                1,
                1000,
                ContentConstants.TYPE_APPLICATION,
                Constants.SORT_ORDER_ASCENDING,
                true
        );

        Application[] apps = (Application[])result.getResults();
        AppianList dictionaries = typeFactory.createList(AppianTypeLong.LIST_OF_DICTIONARY);

        for (Application app : apps) {
            AppianObject dictionary = (AppianObject) typeFactory.createElement(AppianType.DICTIONARY);

            dictionary.put("id", typeFactory.createLong(app.getId()));
            dictionary.put("name", typeFactory.createString(app.getName()));
            dictionary.put("description", typeFactory.createString(app.getDescription()));
            dictionary.put("displayName", typeFactory.createString(app.getDisplayName()));
            dictionary.put("uuid", typeFactory.createString(app.getUuid()));
            dictionary.put("prefix", typeFactory.createString(app.getPrefix()));
            dictionary.put("urlIdentifier", typeFactory.createString(app.getUrlIdentifier()));

            dictionaries.add(dictionary);
        }

        return typeFactory.toTypedValue(dictionaries);
    }
}
