package com.mochafund.workspaceservice.common.events;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EventType {
    public static final String WORKSPACE_PROVISIONING = "workspace.provisioning";
    public static final String WORKSPACE_CREATED = "workspace.created";
    public static final String WORKSPACE_UPDATED = "workspace.updated";
    public static final String WORKSPACE_DELETED = "workspace.deleted";
    public static final String WORKSPACE_DELETED_INITIALIZED = "workspace.deleted.initialized";

    public static final String TAG_CREATED = "tag.created";
    public static final String TAG_UPDATED = "tag.updated";
    public static final String TAG_DELETED = "tag.deleted";

    public static final String ACCOUNT_CREATED = "account.created";
    public static final String ACCOUNT_UPDATED = "account.updated";
    public static final String ACCOUNT_DELETED = "account.deleted";

    public static final String CATEGORY_CREATED = "category.created";
    public static final String CATEGORY_UPDATED = "category.updated";
    public static final String CATEGORY_DELETED = "category.deleted";
}
