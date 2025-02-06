package org.appiansc.plugins.spt.functions.object;

import com.appiancorp.ix.UnsupportedTypeException;
import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.AppianType;
import com.appiancorp.suiteapi.type.TypeService;
import com.appiancorp.suiteapi.type.TypedValue;
import org.appiansc.plugins.spt.AppianTypeHelper;

@SptObjectCategory
public class SPT_Object_RenameProperty {
    @Function
    public TypedValue spt_object_renameproperty(
            TypeService ts,
            @Parameter TypedValue object,
            @Parameter String oldName,
            @Parameter String newName
    ) {
        if (object.getInstanceType() != (long) AppianType.MAP && object.getInstanceType() != (long) AppianType.DICTIONARY)
            throw new UnsupportedTypeException("Only Maps and Dictionaries are supported");


        return AppianTypeHelper.renameProperty(object, oldName, newName);
    }
}
