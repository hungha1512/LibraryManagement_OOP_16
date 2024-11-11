package com.hunghq.librarymanagement.Model.Annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to specify required roles and permissions for accessing
 * certain methods or classes in the application.
 * Applied at runtime to enforce role-based access control.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RolePermissionRequired {

    /**
     * Specifies the roles that are permitted to access the annotated
     * method or class.
     *
     * @return an array of allowed roles
     */
    String[] roles() default {};

    /**
     * Specifies the permissions that are required to access the annotated
     * method or class.
     *
     * @return an array of required permissions
     */
    String[] permissions() default {};
}
