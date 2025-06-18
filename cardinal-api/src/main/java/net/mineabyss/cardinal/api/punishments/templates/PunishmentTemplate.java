package net.mineabyss.cardinal.api.punishments.templates;

import net.mineabyss.cardinal.api.punishments.Punishable;
import net.mineabyss.cardinal.api.punishments.PunishmentType;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface PunishmentTemplate {
    
    /**
     * Get the unique identifier for this template
     */
    TemplateId getId();
    
    /**
     * Get the punishment type this template handles
     */
    PunishmentType getType();
    
    /**
     * Check if the executor has permission to use this template
     */
    boolean hasPermission(String executor, PermissionChecker permissionChecker);
    
    /**
     * Execute this template against a player
     */
    CompletableFuture<TemplateExecutionResult> execute(PunishmentContext context, TemplateExecutor executor);
    
    /**
     * Calculate the current ladder position for a player
     */
    CompletableFuture<Double> calculateLadderPosition(Punishable<?> target);
    
    /**
     * Get the effective ladder step for the next punishment
     */
    CompletableFuture<Optional<LadderStep>> getEffectiveLadderStep(Punishable<?> target);
    
    /**
     * Validate this template configuration
     */
    ValidationResult validate();
    
    /**
     * Check if this template affects IP addresses
     */
    boolean isIpTemplate();
    
    /**
     * Get all child templates (for groups, returns constituent templates; for single templates, returns itself)
     */
    List<PunishmentTemplate> getChildTemplates();
    
    /**
     * Check if this is a template group
     */
    default boolean isGroup() {
        return this instanceof PunishmentTemplateGroup;
    }
}