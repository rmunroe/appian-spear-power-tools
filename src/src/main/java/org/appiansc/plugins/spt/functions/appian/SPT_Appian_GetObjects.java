package org.appiansc.plugins.spt.functions.appian;

import com.appiancorp.ix.Type;
import com.appiancorp.object.AppianObjectService;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.applications.Application;
import com.appiancorp.suiteapi.applications.ApplicationNotFoundException;
import com.appiancorp.suiteapi.applications.ApplicationService;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.Datatype;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;
import org.apache.log4j.Logger;
import org.appiansc.plugins.spt.SptPluginCategory;

import javax.xml.namespace.QName;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SptPluginCategory
public class SPT_Appian_GetObjects {
    private static final Logger LOG = Logger.getLogger(SPT_Appian_GetObjects.class);

    @Function
    public TypedValue spt_appian_getobjects(
            ApplicationService appService,    // injected dependency
            TypeService typeService,          // injected dependency
            AppianObjectService objectService,
            @Parameter() String applicationUuid
    ) throws ApplicationNotFoundException, PrivilegeException {
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);
        AppianObject dictionary = (AppianObject) typeFactory.createElement(AppianType.DICTIONARY);

        Application app = appService.getApplicationByUuid(applicationUuid);

        Application.AssociatedObjects assocObjects = app.getAssociatedObjects();

        Iterator<Map.Entry<Type<?, ?, ?>, Set<?>>> iterator = assocObjects.getGlobalIdMap().iteratorOverNonEmptyCollections();
        while (iterator.hasNext()) {
            Map.Entry<Type<?, ?, ?>, Set<?>> element = iterator.next();
            String key = element.getKey().toString();
            if (!element.getValue().isEmpty()) {
                Object[] values = element.getValue().toArray();
                AppianList idList = typeFactory.createList(AppianTypeLong.STRING);
                for (Object value : values) {


                    if (key.equals("datatype")) {
                        Datatype datatype = typeService.getTypeByQualifiedName((QName) value);
                        idList.add(typeFactory.createString(value.toString()));
                    } else {
                        idList.add(typeFactory.createString(value.toString()));

                    }
                }

                dictionary.put(key, idList);
            }
        }

        return typeFactory.toTypedValue(dictionary);
    }
}
