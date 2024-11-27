package com.hunghq.librarymanagement.Controller;

import com.hunghq.librarymanagement.Global.AppProperties;
import com.hunghq.librarymanagement.Global.DialogHelper;
import com.hunghq.librarymanagement.Model.Annotation.RolePermissionRequired;
import javafx.fxml.Initializable;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * BaseController class that serves as the base for other controllers, providing
 * functionality for role-based access control using the {@link RolePermissionRequired} annotation.
 * This class checks if the current user's role has access to specific functionalities.
 */
public abstract class BaseController implements Initializable {

    /**
     * Initializes the controller, checking if the {@link RolePermissionRequired} annotation
     * is present and, if so, calling the {@link #checkValid(RolePermissionRequired)} method
     * to verify user access.
     *
     * @param url            the location used to resolve relative paths for the root object, or null if unknown
     * @param resourceBundle the resources used to localize the root object, or null if not needed
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RolePermissionRequired annotation = RolePermissionRequired.class.getAnnotation(RolePermissionRequired.class);
        if (annotation != null) {
            checkValid(annotation);
        }
    }

    /**
     * Verifies if the current user's role matches any of the roles required by the
     * {@link RolePermissionRequired} annotation.
     *
     * @param annotation the RolePermissionRequired annotation that specifies the required roles
     */
    public void checkValid(RolePermissionRequired annotation) {
        String currentRoleTitle = AppProperties.getProperty("user.roleTitle");
        boolean hasAccess = false;

        for (String requiredRole : annotation.roles()) {
            if (requiredRole.equals(currentRoleTitle)) {
                hasAccess = true;
                System.out.println("Role " + requiredRole + " is allowed.");
                break;
            }
        }

        if (!hasAccess) {
            alertAccess("Role " + currentRoleTitle + " is not allowed.");
        }
    }

    /**
     * Displays an access denial notification dialog with a warning message.
     * Throws a {@link SecurityException} if access is denied.
     *
     * @param message the message indicating the reason for access denial
     * @throws SecurityException if the user does not have the required role
     */
    private void alertAccess(String message) {
        DialogHelper.showNotificationDialog("Warning", message);
        throw new SecurityException(message);
    }

    protected void checkMethodAccess(Method method) {
        RolePermissionRequired annotationRole = method.getAnnotation(RolePermissionRequired.class);
        String currentRoleTitle = AppProperties.getProperty("user.roleTitle");
        if (annotationRole != null) {
            boolean hasAccess = false;
            for (String requiredRole : annotationRole.roles()) {
                if (requiredRole.equals(currentRoleTitle)) {
                    hasAccess = true;
                    System.out.println("Role " + requiredRole + " is allowed.");
                    break;
                }
            }
            if (!hasAccess) {
                alertAccess("Role " + currentRoleTitle + " is not allowed.");
            }
        }
    }
}
