package com.mineabyss.cardinal.api;

import com.mineabyss.cardinal.api.config.MessageConfig;
import com.mineabyss.cardinal.api.punishments.PunishmentManager;
import org.jetbrains.annotations.NotNull;

public interface CardinalAPI {

    @NotNull PunishmentManager getPunishmentManager();

    @NotNull MessageConfig getMessagesConfig();

}
