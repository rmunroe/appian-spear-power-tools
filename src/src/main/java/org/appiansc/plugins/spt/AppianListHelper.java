package org.appiansc.plugins.spt;

import com.appiancorp.ps.plugins.typetransformer.AppianElement;
import com.appiancorp.ps.plugins.typetransformer.AppianList;
import com.appiancorp.ps.plugins.typetransformer.AppianTypeFactory;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.DatatypeProperties;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;

import java.util.*;

public class AppianListHelper {
    /**
     * Removes empty strings "" from a List of String
     *
     * @param list a List of String
     */
    public static void removeEmptyStrings(TypedValue list) {
        if (list.getInstanceType() != AppianType.LIST_OF_STRING) return;

        List<String> collection = new ArrayList<>(Arrays.asList((String[]) list.getValue()));
        collection.removeAll(Arrays.asList("", null));

        list.setValue(collection.toArray(new String[0]));
    }


    /**
     * @param list Any List type in Appian
     * @return the list with null values removed
     */
    public static TypedValue removeNulls(TypedValue list) {
        if (list.getInstanceType() == AppianType.LIST_OF_STRING) {
            removeEmptyStrings(list);
        }

        list.setValue(
                Arrays.stream((Object[]) list.getValue())
                        .filter(Objects::nonNull)
                        .toArray()
        );

        return list;
    }


    /**
     * Returns an AppianList given a TypedValue. If the TV is a List type, the TV is converted to AppianList.
     * If it is not a List type, a new List is created with the TV as its single element.
     * If an empty string is passed in, null is returned.
     *
     * @param ts TypeService instance (usually injected)
     * @param tv any Appian TypedValue (any type)
     * @return A List (array)
     */
    public static AppianList getList(TypeService ts, TypedValue tv) {
        return getList(ts, tv, false);
    }


    /**
     * Returns an AppianList given a TypedValue. If the TV is a List type, the TV is converted to AppianList.
     * If it is not a List type, a new List is created with the TV as its single element.
     * If an empty string is passed in, null is returned.
     *
     * @param ts             TypeService instance (usually injected)
     * @param list           any Appian TypedValue (any type)
     * @param createIfSingle if true and list is not a List, create a List with one element
     * @return A List (array)
     */
    public static AppianList getList(TypeService ts, TypedValue list, boolean createIfSingle) {
        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);

        AppianElement element = typeFactory.toAppianElement(list);
        long typeId = element.getTypeId();
        DatatypeProperties props = ts.getDatatypeProperties(typeId);

        if (typeId == AppianType.STRING && list.getValue().equals("")) {
            // null or empty string passed in, return null
            return null;

        } else if (!props.isListType()) {
            if (createIfSingle) {
                // If not a List, create one and return it
                AppianList newList = typeFactory.createList(props.getList());
                newList.add(typeFactory.toAppianElement(list));
                return newList;
            } else {
                return null;
            }

        } else {
            return (AppianList) element;
        }
    }

    /**
     * Removes duplicate values from a List
     *
     * @param ts   TypeService instance (usually injected)
     * @param list the List to remove duplicates from
     * @return the List, sans duplicates
     */
    public static TypedValue getUniqueListValues(TypeService ts, TypedValue list) {
        AppianTypeFactory typeFactory = AppianTypeHelper.getTypeFactory(ts);

        AppianList appianList = getList(ts, list);

        LinkedHashMap<TypedValue, AppianElement> map = new LinkedHashMap<>();
        assert appianList != null;

        // Handle lists of Maps by turning into Dictionaries
        if (list.getInstanceType() == AppianType.LIST_OF_MAP) {
            list = AppianTypeConverter.convert(ts, list, (long) AppianType.DICTIONARY);
            appianList = getList(ts, list);
        }

        assert appianList != null;
        for (AppianElement element : appianList) {
            map.put(typeFactory.toTypedValue(element), element); // TypedValue has proper equals() so the map will contain unique values
        }

        AppianList newList = typeFactory.createList(list.getInstanceType());
        newList.addAll(map.values());
        return typeFactory.toTypedValue(newList);
    }
}
