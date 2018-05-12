package com.sharelib.consent_dialog;

import java.io.Serializable;

public class PermissionItem implements Serializable {

    private String[] permissions;
    public String[] getPermissions() {
        return permissions;
    }
    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private int drawable;
    public int getDrawableResource() {
        return drawable;
    }
    public void setDrawableResource(int drawable) {
        this.drawable = drawable;
    }

}
