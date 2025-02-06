package org.appiansc.plugins.spt;

import com.appiancorp.core.data.Dictionary;
import com.appiancorp.expr.server.fn.object.ObjectPropertyName;
import com.appiancorp.ix.CoreTypeIxTypeMapping;
import com.appiancorp.ix.Type;
import com.appiancorp.object.AppianObjectSelection;
import com.appiancorp.object.AppianObjectSelectionResult;
import com.appiancorp.object.AppianObjectService;
import com.appiancorp.object.selector.Select;
import com.appiancorp.object.selector.SelectId;
import com.appiancorp.object.selector.SelectUnion;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianObject;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.applications.Application;
import com.appiancorp.suiteapi.applications.ApplicationNotFoundException;
import com.appiancorp.suiteapi.applications.ApplicationService;
import com.appiancorp.suiteapi.common.exceptions.PrivilegeException;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;

import java.util.*;
import java.util.stream.Collectors;


public class AppianObjectHelper {
    private static final ObjectPropertyName[] objectPropertyNames = new ObjectPropertyName[]{
            ObjectPropertyName.TYPE,
            ObjectPropertyName.TYPE_ID,
            ObjectPropertyName.ANNOTATED_NAME,
            ObjectPropertyName.NAME,
            ObjectPropertyName.UUID
    };


    public static TypedValue getSingleObjectTypedValue(TypeService ts, AppianObjectService aos, Long typeId, String uuid) {
        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(getSingleObject(ts, aos, typeId, uuid));
    }


    public static AppianObject getSingleObject(TypeService ts, AppianObjectService aos, Long typeId, String uuid) {
        Select select = SelectId.buildUuidReference(typeId, uuid);
        AppianObjectSelection selection = aos.select(select);
        AppianObjectSelectionResult result = selection.getAll(objectPropertyNames);
        Dictionary detailsDict = result.getListFacade().getUuidToPropertiesMap().get(uuid);

        return getAppianObject(ts, detailsDict);
    }


    /**
     * Return a List of Dictionary (as TypedValue) containing the details for all Objects in the given Application UUID
     *
     * @param ts      TypeService instance
     * @param as      ApplicationService instance
     * @param aos     AppianObjectService instance
     * @param appUuid Application UUID
     * @return List of Dictionary (as TypedValue)
     * @throws ApplicationNotFoundException if the App UUID doesn't resolve
     * @throws PrivilegeException           if the user doesn't have access to the Application
     */
    public static TypedValue getApplicationObjects(TypeService ts, ApplicationService as, AppianObjectService aos, String appUuid, boolean byType) throws ApplicationNotFoundException, PrivilegeException {
        Application app = as.getApplicationByUuid(appUuid);

        Map<Type<?, ?, ?>, List<String>> typeUuidMap = getTypeUuidMap(app);

        if (byType)
            return getDictionary(aos, ts, typeUuidMap);
        else
            return getSingleList(aos, ts, typeUuidMap);
    }

    private static HashSet<Type<?, ?, ?>> getValidTypes() {
        return Type.ALL_TYPES.stream().filter(Type::isDesignObject).collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Returns a map of Type to the list of UUIDs for that type, in an Application
     *
     * @param app An Application instance
     * @return Map
     */
    public static Map<Type<?, ?, ?>, List<String>> getTypeUuidMap(Application app) {
        Map<Type<?, ?, ?>, List<String>> objects = new HashMap<>();
        app.getAssociatedObjects().getGlobalIdMap().iteratorOverNonEmptyCollections().forEachRemaining(a -> {
            Type<?, ?, ?> type = a.getKey();
            List<String> uuids = new ArrayList<>();

            for (Object value : a.getValue().toArray()) uuids.add(value.toString());

            objects.put(type, uuids);
        });
        return objects;
    }

    public static List<String> getUuidList(Application app) {
        List<String> list = new ArrayList<>();
        app.getAssociatedObjects().getGlobalIdMap().iteratorOverNonEmptyCollections().forEachRemaining(a -> {
            Type<?, ?, ?> type = a.getKey();
            List<String> uuids = new ArrayList<>();

            for (Object value : a.getValue().toArray()) list.add(value.toString());
        });
        return list;
    }

    /**
     * Returns a Map of UUID to Dictionary, where the Dictionary contains details (such as name) of the Object UUIDs passed in.
     *
     * @param aos    AppianObjectService instance
     * @param typeId the Appian type the Object UUIDs are of
     * @param uuids  the list of Object UUIDs
     * @return Map
     */
    private static Map<String, Dictionary> getDetailsForUuids(AppianObjectService aos, Long typeId, List<String> uuids) {
        List<Select> selects = new ArrayList<>();

        for (String uuid : uuids) selects.add(SelectId.buildUuidReference(typeId, uuid));

        AppianObjectSelection selection = aos.select(new SelectUnion(selects));
        AppianObjectSelectionResult result = selection.getAll(objectPropertyNames);
        return result.getListFacade().getUuidToPropertiesMap();
    }


    /**
     * Returns the results as a Dictionary (as TypedValue) where the keys are the Appian types and the values are the List of Dictionaries for each Object in the App
     *
     * @param aos         AppianObjectService instance
     * @param ts          TypeService instance
     * @param typeUuidMap results of getTypeUuidMap()
     * @return Dictionary (as TypedValue)
     */
    private static TypedValue getDictionary(AppianObjectService aos, TypeService ts, Map<Type<?, ?, ?>, List<String>> typeUuidMap) {
        AppianObject typeDict = (AppianObject) AppianTypeHelper.getTypeFactory(ts).createElement(AppianTypeLong.DICTIONARY);

        for (Type<?, ?, ?> type : typeUuidMap.keySet()) {
            if (!getValidTypes().contains(type)) continue;
            AppianList objects = AppianTypeHelper.getTypeFactory(ts).createList(AppianTypeLong.DICTIONARY);

            List<String> uuids = typeUuidMap.get(type);
            Long typeId = CoreTypeIxTypeMapping.getInverseTypeMapping().get(type).getTypeId();

            Map<String, Dictionary> details = getDetailsForUuids(aos, typeId, uuids);

            for (String uuid : uuids) {
                objects.add(getAppianObject(ts, details.get(uuid)));
            }

            typeDict.put(type.getKey(), objects);
        }

        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(typeDict);
    }


    /**
     * The List of Dictionaries for each Object in the App
     *
     * @param aos         AppianObjectService instance
     * @param ts          TypeService instance
     * @param typeUuidMap results of getTypeUuidMap()
     * @return Dictionary (as TypedValue)
     */
    private static TypedValue getSingleList(AppianObjectService aos, TypeService ts, Map<Type<?, ?, ?>, List<String>> typeUuidMap) {
        AppianList objects = AppianTypeHelper.getTypeFactory(ts).createList(AppianTypeLong.DICTIONARY);

        for (Type<?, ?, ?> type : typeUuidMap.keySet()) {
            if (!getValidTypes().contains(type)) continue;

            List<String> uuids = typeUuidMap.get(type);
            Long typeId = CoreTypeIxTypeMapping.getInverseTypeMapping().get(type).getTypeId();

            Map<String, Dictionary> details = getDetailsForUuids(aos, typeId, uuids);

            for (String uuid : uuids) {
                objects.add(getAppianObject(ts, details.get(uuid)));
            }
        }

        return AppianTypeHelper.getTypeFactory(ts).toTypedValue(objects);
    }


    /**
     * Returns a Dictionary (as AppianObject) for the given object UUID
     *
     * @param detailsDict The retrieved Object details
     * @return Dictionary (as AppianObject)
     */
    private static AppianObject getAppianObject(
            TypeService ts,
            Dictionary detailsDict
    ) {
        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);
        AppianObject objectInfo = (AppianObject) typeFactory.createElement(AppianTypeLong.DICTIONARY);

        objectInfo.put("typeId", typeFactory.createLong(((Integer) detailsDict.getValue("typeId").getValue()).longValue()));
        objectInfo.put("typeName", typeFactory.createString(detailsDict.getValue("type").getValue().toString()));
        objectInfo.put("typeNameLong", typeFactory.createString(detailsDict.getValue("type").toString()));
        objectInfo.put("uuid", typeFactory.createString(detailsDict.getValue("uuid").getValue().toString()));
        objectInfo.put("name", typeFactory.createString(detailsDict.getValue("name").getValue().toString()));
        objectInfo.put("annotatedName", typeFactory.createString(detailsDict.getValue("annotatedName").getValue().toString()));

        return objectInfo;
    }
}
