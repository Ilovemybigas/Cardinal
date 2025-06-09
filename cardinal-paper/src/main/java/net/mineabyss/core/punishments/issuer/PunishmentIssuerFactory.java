package net.mineabyss.core.punishments.issuer;

import net.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import org.bukkit.entity.Player;


public final class PunishmentIssuerFactory {

    public static PunishmentIssuer fromPlayer(Player player) {
        return new PlayerIssuer(player);
    }

    public static PunishmentIssuer fromConsole() {
        return ConsoleIssuer.get();
    }

}
