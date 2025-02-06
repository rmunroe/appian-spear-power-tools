package org.appiansc.plugins.spt.functions.bool;

import com.appiancorp.suiteapi.expression.annotations.Function;
import com.appiancorp.suiteapi.expression.annotations.Parameter;
import com.appiancorp.suiteapi.type.TypedValue;
import com.appiancorp.type.AppianTypeLong;

import java.util.Objects;


@SptBooleanCategory
public class SPT_Bool_TrueOrNull {
    @Function
    public Boolean spt_bool_trueornull(@Parameter(required = false) TypedValue value) {
        // Return null if null
        if (value == null || value.getValue() == null) return null;

        // If a boolean value of true is passed in, return true
        if (Objects.equals(value.getInstanceType(), AppianTypeLong.BOOLEAN) && value.getValue().equals(1L)) return true;

        // If an int value of 1L is passed in, return true
        if (Objects.equals(value.getInstanceType(), AppianTypeLong.INTEGER) && value.getValue().equals(1L)) return true;

        // If a string value of "1" is passed in, return true
        if (Objects.equals(value.getInstanceType(), AppianTypeLong.STRING) && value.getValue().equals("1")) return true;

        // If a string value of "true" is passed in (caps insensitive), return true
        if (Boolean.parseBoolean(value.getValue().toString())) return true;

        // otherwise return null
        return null;
    }
}