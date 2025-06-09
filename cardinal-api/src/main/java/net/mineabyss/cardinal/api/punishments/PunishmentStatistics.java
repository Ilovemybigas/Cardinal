package net.mineabyss.cardinal.api.punishments;

import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Immutable statistical information about a player's punishment history.
 * <p>
 * This class provides comprehensive statistics about punishments for analysis,
 * reporting, and administrative purposes. All instances are immutable and thread-safe.
 * </p>
 *
 * @since 1.0
 * @see PunishmentManager#getPunishmentStatistics(UUID, Instant)
 */
public final class PunishmentStatistics {

    private final @NotNull UUID playerId;
    private final int totalPunishments;
    private final int activePunishments;
    private final int expiredPunishments;
    private final int revokedPunishments;
    private final @NotNull Map<PunishmentType, Integer> punishmentsByType;
    private final @NotNull Map<PunishmentIssuer, Integer> punishmentsByIssuer;
    private final @Nullable Instant firstPunishment;
    private final @Nullable Instant lastPunishment;
    private final @Nullable Instant mostRecentActive;
    private final @NotNull Duration totalTimeServed;
    private final @NotNull Duration averagePunishmentDuration;
    private final int permanentPunishments;
    private final double punishmentsPerDay;
    private final @NotNull Instant statisticsGeneratedAt;

    private PunishmentStatistics(@NotNull Builder builder) {
        this.playerId = Objects.requireNonNull(builder.playerId, "playerId cannot be null");
        this.totalPunishments = builder.totalPunishments;
        this.activePunishments = builder.activePunishments;
        this.expiredPunishments = builder.expiredPunishments;
        this.revokedPunishments = builder.revokedPunishments;
        this.punishmentsByType = Map.copyOf(builder.punishmentsByType);
        this.punishmentsByIssuer = Map.copyOf(builder.punishmentsByIssuer);
        this.firstPunishment = builder.firstPunishment;
        this.lastPunishment = builder.lastPunishment;
        this.mostRecentActive = builder.mostRecentActive;
        this.totalTimeServed = Objects.requireNonNull(builder.totalTimeServed, "totalTimeServed cannot be null");
        this.averagePunishmentDuration = Objects.requireNonNull(builder.averagePunishmentDuration, "averagePunishmentDuration cannot be null");
        this.permanentPunishments = builder.permanentPunishments;
        this.punishmentsPerDay = builder.punishmentsPerDay;
        this.statisticsGeneratedAt = Objects.requireNonNull(builder.statisticsGeneratedAt, "statisticsGeneratedAt cannot be null");
    }

    // Getters
    public @NotNull UUID getPlayerId() { return playerId; }
    public int getTotalPunishments() { return totalPunishments; }
    public int getActivePunishments() { return activePunishments; }
    public int getExpiredPunishments() { return expiredPunishments; }
    public int getRevokedPunishments() { return revokedPunishments; }
    public @NotNull Map<PunishmentType, Integer> getPunishmentsByType() { return punishmentsByType; }
    public @NotNull Map<PunishmentIssuer, Integer> getPunishmentsByIssuer() { return punishmentsByIssuer; }
    public @NotNull Optional<Instant> getFirstPunishment() { return Optional.ofNullable(firstPunishment); }
    public @NotNull Optional<Instant> getLastPunishment() { return Optional.ofNullable(lastPunishment); }
    public @NotNull Optional<Instant> getMostRecentActive() { return Optional.ofNullable(mostRecentActive); }
    public @NotNull Duration getTotalTimeServed() { return totalTimeServed; }
    public @NotNull Duration getAveragePunishmentDuration() { return averagePunishmentDuration; }
    public int getPermanentPunishments() { return permanentPunishments; }
    public double getPunishmentsPerDay() { return punishmentsPerDay; }
    public @NotNull Instant getStatisticsGeneratedAt() { return statisticsGeneratedAt; }

    /**
     * Gets the count of punishments for a specific type.
     *
     * @param type the punishment type to get the count for
     * @return the count, or 0 if no punishments of that type exist
     */
    public int getCountForType(@NotNull PunishmentType type) {
        Validate.notNull(type, "type cannot be null");
        return punishmentsByType.getOrDefault(type, 0);
    }

    /**
     * Gets the count of punishments issued by a specific issuer.
     *
     * @param issuer the punishment issuer to get the count for
     * @return the count, or 0 if no punishments by that issuer exist
     */
    public int getCountForIssuer(@NotNull PunishmentIssuer issuer) {
        Validate.notNull(issuer, "issuer cannot be null");
        return punishmentsByIssuer.getOrDefault(issuer, 0);
    }

    /**
     * Checks if the player has any active punishments.
     *
     * @return true if there are active punishments, false otherwise
     */
    public boolean hasActivePunishments() {
        return activePunishments > 0;
    }

    /**
     * Checks if the player has ever been punished.
     *
     * @return true if there are any punishments in history, false otherwise
     */
    public boolean hasAnyPunishments() {
        return totalPunishments > 0;
    }

    /**
     * Gets the most common punishment type for this player.
     *
     * @return an Optional containing the most common punishment type, or empty if no punishments exist
     */
    public @NotNull Optional<PunishmentType> getMostCommonPunishmentType() {
        return punishmentsByType.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    /**
     * Gets the issuer who has punished this player the most.
     *
     * @return an Optional containing the most active issuer, or empty if no punishments exist
     */
    public @NotNull Optional<PunishmentIssuer> getMostActiveIssuer() {
        return punishmentsByIssuer.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
    }

    /**
     * Creates a new builder instance.
     *
     * @param playerId the UUID of the player these statistics are for
     * @return a new builder for creating punishment statistics
     */
    public static @NotNull Builder builder(@NotNull UUID playerId) {
        return new Builder(playerId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        
        PunishmentStatistics that = (PunishmentStatistics) obj;
        return totalPunishments == that.totalPunishments &&
               activePunishments == that.activePunishments &&
               expiredPunishments == that.expiredPunishments &&
               revokedPunishments == that.revokedPunishments &&
               permanentPunishments == that.permanentPunishments &&
               Double.compare(that.punishmentsPerDay, punishmentsPerDay) == 0 &&
               Objects.equals(playerId, that.playerId) &&
               Objects.equals(punishmentsByType, that.punishmentsByType) &&
               Objects.equals(punishmentsByIssuer, that.punishmentsByIssuer) &&
               Objects.equals(firstPunishment, that.firstPunishment) &&
               Objects.equals(lastPunishment, that.lastPunishment) &&
               Objects.equals(mostRecentActive, that.mostRecentActive) &&
               Objects.equals(totalTimeServed, that.totalTimeServed) &&
               Objects.equals(averagePunishmentDuration, that.averagePunishmentDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, totalPunishments, activePunishments, expiredPunishments,
                          revokedPunishments, punishmentsByType, punishmentsByIssuer, firstPunishment,
                          lastPunishment, mostRecentActive, totalTimeServed, averagePunishmentDuration,
                          permanentPunishments, punishmentsPerDay);
    }

    @Override
    public String toString() {
        return "PunishmentStatistics{" +
               "playerId=" + playerId +
               ", totalPunishments=" + totalPunishments +
               ", activePunishments=" + activePunishments +
               ", expiredPunishments=" + expiredPunishments +
               ", revokedPunishments=" + revokedPunishments +
               ", permanentPunishments=" + permanentPunishments +
               ", punishmentsPerDay=" + String.format("%.2f", punishmentsPerDay) +
               ", firstPunishment=" + firstPunishment +
               ", lastPunishment=" + lastPunishment +
               ", totalTimeServed=" + totalTimeServed +
               ", averagePunishmentDuration=" + averagePunishmentDuration +
               ", statisticsGeneratedAt=" + statisticsGeneratedAt +
               '}';
    }

    /**
     * Builder class for constructing {@link PunishmentStatistics} instances.
     */
    public static final class Builder {
        private final @NotNull UUID playerId;
        private int totalPunishments = 0;
        private int activePunishments = 0;
        private int expiredPunishments = 0;
        private int revokedPunishments = 0;
        private final @NotNull Map<PunishmentType, Integer> punishmentsByType = new HashMap<>();
        private final @NotNull Map<PunishmentIssuer, Integer> punishmentsByIssuer = new HashMap<>();
        private @Nullable Instant firstPunishment;
        private @Nullable Instant lastPunishment;
        private @Nullable Instant mostRecentActive;
        private @NotNull Duration totalTimeServed = Duration.ZERO;
        private @NotNull Duration averagePunishmentDuration = Duration.ZERO;
        private int permanentPunishments = 0;
        private double punishmentsPerDay = 0.0;
        private @NotNull Instant statisticsGeneratedAt = Instant.now();

        private Builder(@NotNull UUID playerId) {
            this.playerId = Objects.requireNonNull(playerId, "playerId cannot be null");
        }

        public @NotNull Builder totalPunishments(int totalPunishments) {
            Validate.isTrue(totalPunishments >= 0, "totalPunishments must be non-negative");
            this.totalPunishments = totalPunishments;
            return this;
        }

        public @NotNull Builder activePunishments(int activePunishments) {
            Validate.isTrue(activePunishments >= 0, "activePunishments must be non-negative");
            this.activePunishments = activePunishments;
            return this;
        }

        public @NotNull Builder expiredPunishments(int expiredPunishments) {
            Validate.isTrue(expiredPunishments >= 0, "expiredPunishments must be non-negative");
            this.expiredPunishments = expiredPunishments;
            return this;
        }

        public @NotNull Builder revokedPunishments(int revokedPunishments) {
            Validate.isTrue(revokedPunishments >= 0, "revokedPunishments must be non-negative");
            this.revokedPunishments = revokedPunishments;
            return this;
        }

        public @NotNull Builder addPunishmentTypeCount(@NotNull PunishmentType type, int count) {
            Validate.notNull(type, "type cannot be null");
            Validate.isTrue(count >= 0, "count must be non-negative");
            this.punishmentsByType.put(type, count);
            return this;
        }

        public @NotNull Builder addIssuerCount(@NotNull PunishmentIssuer issuer, int count) {
            Validate.notNull(issuer, "issuer cannot be null");
            Validate.isTrue(count >= 0, "count must be non-negative");
            this.punishmentsByIssuer.put(issuer, count);
            return this;
        }

        public @NotNull Builder firstPunishment(@Nullable Instant firstPunishment) {
            this.firstPunishment = firstPunishment;
            return this;
        }

        public @NotNull Builder lastPunishment(@Nullable Instant lastPunishment) {
            this.lastPunishment = lastPunishment;
            return this;
        }

        public @NotNull Builder mostRecentActive(@Nullable Instant mostRecentActive) {
            this.mostRecentActive = mostRecentActive;
            return this;
        }

        public @NotNull Builder totalTimeServed(@NotNull Duration totalTimeServed) {
            this.totalTimeServed = Objects.requireNonNull(totalTimeServed, "totalTimeServed cannot be null");
            Validate.isTrue(!totalTimeServed.isNegative(), "totalTimeServed cannot be negative");
            return this;
        }

        public @NotNull Builder averagePunishmentDuration(@NotNull Duration averagePunishmentDuration) {
            this.averagePunishmentDuration = Objects.requireNonNull(averagePunishmentDuration, "averagePunishmentDuration cannot be null");
            Validate.isTrue(!averagePunishmentDuration.isNegative(), "averagePunishmentDuration cannot be negative");
            return this;
        }

        public @NotNull Builder permanentPunishments(int permanentPunishments) {
            Validate.isTrue(permanentPunishments >= 0, "permanentPunishments must be non-negative");
            this.permanentPunishments = permanentPunishments;
            return this;
        }

        public @NotNull Builder punishmentsPerDay(double punishmentsPerDay) {
            Validate.isTrue(punishmentsPerDay >= 0.0, "punishmentsPerDay must be non-negative");
            this.punishmentsPerDay = punishmentsPerDay;
            return this;
        }

        public @NotNull Builder statisticsGeneratedAt(@NotNull Instant statisticsGeneratedAt) {
            this.statisticsGeneratedAt = Objects.requireNonNull(statisticsGeneratedAt, "statisticsGeneratedAt cannot be null");
            return this;
        }

        /**
         * Builds the statistics with validation.
         *
         * @return an immutable {@link PunishmentStatistics} instance
         * @throws IllegalArgumentException if any validation constraints are violated
         */
        public @NotNull PunishmentStatistics build() {
            validateConstraints();
            return new PunishmentStatistics(this);
        }

        private void validateConstraints() {
            Validate.isTrue(activePunishments + expiredPunishments + revokedPunishments <= totalPunishments,
                "Sum of active, expired, and revoked punishments cannot exceed total punishments");
            
            if (firstPunishment != null && lastPunishment != null) {
                Validate.isTrue(!firstPunishment.isAfter(lastPunishment),
                    "firstPunishment cannot be after lastPunishment");
            }
            
            int typeSum = punishmentsByType.values().stream().mapToInt(Integer::intValue).sum();
            if (typeSum > 0 && totalPunishments > 0) {
                Validate.isTrue(typeSum <= totalPunishments,
                    "Sum of punishments by type cannot exceed total punishments");
            }
        }
    }
}