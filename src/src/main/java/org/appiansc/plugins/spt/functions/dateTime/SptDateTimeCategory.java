package org.appiansc.plugins.spt.functions.dateTime;

import com.appiancorp.suiteapi.expression.annotations.Category;

import java.lang.annotation.*;

@Category("SptDateTimeCategory")
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SptDateTimeCategory {

}
