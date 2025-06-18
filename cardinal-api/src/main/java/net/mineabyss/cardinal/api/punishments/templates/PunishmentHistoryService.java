package net.mineabyss.cardinal.api.punishments.templates;

import net.mineabyss.cardinal.api.punishments.Punishable;
import net.mineabyss.cardinal.api.punishments.Punishment;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PunishmentHistoryService {
    CompletableFuture<List<Punishment<?>>> getPunishmentHistory(Punishable<?> target, TemplateId templateId);
    CompletableFuture<List<Punishment<?>>> getAllPunishmentHistory(Punishable<?> target);
}