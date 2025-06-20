package com.mineabyss.cardinal.api.punishments.templates;

import org.jetbrains.annotations.Nullable;

public interface PermissionChecker {
    boolean hasPermission(String executor, @Nullable String permission);
}