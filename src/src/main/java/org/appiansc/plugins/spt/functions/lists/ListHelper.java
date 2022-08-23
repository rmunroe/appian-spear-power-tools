package org.appiansc.plugins.spt.functions.lists;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;

public class ListHelper {
    /**
     * Returns true if the TypedValue passed in is a List type.
     * @param typeService TypeService instance (usually injected)
     * @param tv any Appian TypedValue (any type)
     * @return true is type is a List
     */
    public static boolean isList(TypeService typeService, TypedValue tv) {
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);

        AppianElement element = typeFactory.toAppianElement(tv);
        long typeId = element.getTypeId();
        DatatypeProperties props = typeService.getDatatypeProperties(typeId);

        return props.isListType();
    }

    /**
     * Returns an AppianList given a TypedValue. If the TV is a List type, the TV is converted to AppianList.
     * If it is not a List type, a new List is created with the TV as its single element.
     * If an empty string is passed in, null is returned.
     * @param typeService TypeService instance (usually injected)
     * @param tv any Appian TypedValue (any type)
     * @return A List (array)
     */
    public static AppianList getList(TypeService typeService, TypedValue tv, boolean createIfSingle) {
        AppianTypeFactory typeFactory = AppianTypeFactory.newInstance(typeService);

        AppianElement element = typeFactory.toAppianElement(tv);
        long typeId = element.getTypeId();
        DatatypeProperties props = typeService.getDatatypeProperties(typeId);

        if (typeId == 3 && tv.getValue().equals("")) {
            // null or empty string passed in, return null
            return null;

        } else if (!props.isListType()) {
            if (createIfSingle) {
                // If not a List, create one and return it
                AppianList newList = typeFactory.createList(props.getList());
                newList.add(typeFactory.toAppianElement(tv));
                return newList;
            } else {
                return null;
            }

        } else {
            return (AppianList) element;
        }
    }
}
