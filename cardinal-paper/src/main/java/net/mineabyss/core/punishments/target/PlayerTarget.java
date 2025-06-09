package net.mineabyss.core.punishments.target;

import net.mineabyss.cardinal.api.punishments.Punishable;
import net.mineabyss.cardinal.api.punishments.PunishableType;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.UUID;

final class PlayerTarget implements Punishable<UUID> {

    private final UUID playerUUID;
    private final String playerName;

    private Instant lastSeen;

    /**
     * Constructs a PlayerTarget with the specified UUID and name.
     *
     * @param playerUUID the UUID of the player
     * @param playerName the name of the player
     */
    PlayerTarget(@NotNull UUID playerUUID, @NotNull String playerName) {
        this.playerUUID = playerUUID;
        this.playerName = playerName;
        this.lastSeen = Instant.now(); // Initialize last seen to current time
    }

    /**
     * Returns the unique identifier for this punishable entity.
     *
     * @return the unique identifier
     */
    @Override
    public @NotNull PunishableType getType() {
        return PunishableType.PLAYER;
    }

    /**
     * Returns the name of the target entity.
     *
     * @return the name of the target entity
     */
    @NotNull
    @Override
    public String getTargetName() {
        return playerName;
    }

    /**
     * Returns the UUID of the target entity.
     *
     * @return the UUID of the target entity
     */
    @NotNull
    @Override
    public UUID getTargetUUID() {
        return playerUUID;
    }

    /**
     * Returns the target entity itself.
     *
     * @return the target entity
     */
    @NotNull
    @Override
    public UUID getTarget() {
        return playerUUID;
    }

    /**
     * Returns the last seen time of the target entity.
     *
     * @return the last seen time as an Instant
     */
    @Override
    public Instant getLastSeen() {
        return lastSeen;
    }

    /**
     * Sets last seen time of the target entity to the current time.
     */
    @Override
    public void refreshLastSeen() {
        this.lastSeen = Instant.now();
    }
}
