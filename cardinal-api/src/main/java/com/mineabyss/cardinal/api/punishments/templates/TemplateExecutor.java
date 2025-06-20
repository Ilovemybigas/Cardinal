package com.mineabyss.cardinal.api.punishments.templates;

import com.mineabyss.cardinal.api.punishments.Punishable;
import com.mineabyss.cardinal.api.punishments.Punishment;
import com.mineabyss.cardinal.api.punishments.PunishmentType;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface TemplateExecutor {
    CompletableFuture<Punishment<?>> createPunishment(
        Punishable<?> playerId,
        TemplateId templateId,
        PunishmentType type,
        String reason,
        String message,
        Duration duration,
        String issuedBy,
        boolean ipPunishment
    );
    
    CompletableFuture<Void> executeActions(List<TemplateAction> actions, PunishmentContext context);
}