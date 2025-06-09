package net.mineabyss.core.punishments.issuer;

import net.mineabyss.cardinal.api.punishments.IssuerType;
import net.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public final class PlayerIssuer implements PunishmentIssuer {

    private final String name;
    private final UUID uniqueId;

    PlayerIssuer (@NotNull Player player) {
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
    }


    /**
     * Returns the name of the issuer.
     *
     * @return the name of the issuer
     */
    @NotNull @Override
    public String getName() {
        return name;
    }

    /**
     * Returns the unique identifier for this issuer.
     *
     * @return the unique identifier as a UUID
     */
    @Override
    public @NotNull UUID getUniqueId() {
        return uniqueId;
    }

    /**
     * Returns the type of this issuer.
     *
     * @return the type of the issuer
     */
    @Override
    public @NotNull IssuerType getType() {
        return IssuerType.PLAYER;
    }
}
