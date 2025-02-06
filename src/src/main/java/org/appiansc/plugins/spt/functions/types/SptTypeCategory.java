package org.appiansc.plugins.spt.functions.types;

import com.appiancorp.suiteapi.expression.annotations.Category;

import java.lang.annotation.*;

@Category("SptTypeCategory")
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SptTypeCategory {

}
