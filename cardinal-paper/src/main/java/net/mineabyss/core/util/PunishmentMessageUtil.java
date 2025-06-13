package net.mineabyss.core.util;

import com.mineabyss.lib.util.TimeUtil;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.mineabyss.cardinal.api.punishments.Punishment;
import net.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import net.mineabyss.core.CardinalPermissions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;

@UtilityClass
public final class PunishmentMessageUtil {

    public static @NotNull String getBanKickMessageMiniMessage(Punishment<?> punishment) {
        if (punishment.getType() == StandardPunishmentType.WARN || punishment.getType() == StandardPunishmentType.MUTE) {
            return "";
        }

        StringBuilder kickMsg = new StringBuilder();

        // Header with gradient
        kickMsg.append("<gradient:#ff4444:#cc0000><bold>⚠ SERVER BAN NOTICE ⚠</bold></gradient>\n");
        kickMsg.append("<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n\n");

        // Main message
        kickMsg.append("<red><bold>You have been banned from this server!</bold></red>\n\n");

        // Punishment details with professional styling
        kickMsg.append("<gray>📋 </gray><white>Punishment ID:</white> <yellow>#")
                .append(punishment.getId().getRepresentation())
                .append("</yellow>\n");

        kickMsg.append("<gray>📝 </gray><white>Reason:</white> <#ffa500>")
                .append(punishment.getReason().orElse("Breaking the server rules"))
                .append("</#ffa500>\n");

        kickMsg.append("<gray>📅 </gray><white>Issued:</white> <aqua>")
                .append(TimeUtil.formatDate(punishment.getIssuedAt()))
                .append("</aqua>\n");

        // Expiry information
        if (!punishment.isPermanent()) {
            kickMsg.append("<gray>⏰ </gray><white>Expires:</white> <green>")
                    .append(TimeUtil.formatDate(punishment.getExpiresAt()))
                    .append("</green>\n");
        } else {
            kickMsg.append("<gray>♾️ </gray><gradient:#ff6b6b:#cc0000>This ban will never expire!</gradient>\n");
        }

        // Footer
        kickMsg.append("\n<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n");
        kickMsg.append("<gray><italic>Contact staff if you believe this is an error</italic></gray>");

        return kickMsg.toString();
    }

    public static @NotNull Component getBanKickMessage(Punishment<?> punishment) {
        String miniMessage = getBanKickMessageMiniMessage(punishment);
        if (miniMessage.isEmpty()) {
            return Component.empty();
        }
        return MiniMessage.miniMessage().deserialize(miniMessage);
    }

    public static @NotNull String getMuteMessageMiniMessage(Punishment<?> punishment) {
        if (punishment.getType() != StandardPunishmentType.MUTE) {
            return "";
        }

        StringBuilder muteMsg = new StringBuilder();

        // Header with gradient (orange/yellow theme for mute)
        muteMsg.append("<gradient:#ffaa00:#ff6600><bold>🔇 SERVER MUTE NOTICE 🔇</bold></gradient>\n");
        muteMsg.append("<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n\n");

        // Main message
        muteMsg.append("<gold><bold>You have been muted on this server!</bold></gold>\n");
        muteMsg.append("<gray><italic>You cannot send messages in chat</italic></gray>\n\n");

        // Punishment details with professional styling
        muteMsg.append("<gray>📋 </gray><white>Punishment ID:</white> <yellow>#")
                .append(punishment.getId().getRepresentation())
                .append("</yellow>\n");

        muteMsg.append("<gray>📝 </gray><white>Reason:</white> <#ffa500>")
                .append(punishment.getReason().orElse("Inappropriate chat behavior"))
                .append("</#ffa500>\n");

        muteMsg.append("<gray>📅 </gray><white>Issued:</white> <aqua>")
                .append(TimeUtil.formatDate(punishment.getIssuedAt()))
                .append("</aqua>\n");

        // Expiry information
        if (!punishment.isPermanent()) {
            muteMsg.append("<gray>⏰ </gray><white>Expires:</white> <green>")
                    .append(TimeUtil.formatDate(punishment.getExpiresAt()))
                    .append("</green>\n");
            muteMsg.append("<gray>💬 </gray><white>You can speak again in:</white> <yellow>")
                    .append(TimeUtil.format(Duration.between(Instant.now(), punishment.getExpiresAt()) ) )
                    .append("</yellow>\n");
        } else {
            muteMsg.append("<gray>♾️ </gray><gradient:#ff9900:#cc6600>This mute will never expire!</gradient>\n");
        }

        // Footer
        muteMsg.append("\n<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n");
        muteMsg.append("<gray><italic>Contact staff if you believe this is an error</italic></gray>");

        return muteMsg.toString();
    }

    public static @NotNull String getNormalKickMessageMiniMessage(Punishment<?> punishment) {
        return "<gradient:#ffaa00:#ff5555><bold>⚠ You were kicked ⚠</bold></gradient>\n"
                + "<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n\n"

                // Reason
                + "<gray>📝 </gray><white>Reason:</white> <#ffa500>"
                + punishment.getReason().orElse("No reason provided")
                + "</#ffa500>\n"

                // Issuer info (optional, but helpful in multiplayer contexts)
                + "<gray>👮 </gray><white>By:</white> <yellow>"
                + punishment.getIssuer().getName()
                + "</yellow>\n"

                // Issued time
                + "<gray>📅 </gray><white>Issued:</white> <aqua>"
                + TimeUtil.formatDate(punishment.getIssuedAt())
                + "</aqua>\n"

                // Footer
                + "\n<dark_gray>━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━</dark_gray>\n"
                + "<gray><italic>You may rejoin unless otherwise restricted.</italic></gray>";
    }

    public static @NotNull Component getNormalKickMessage(Punishment<?> punishment) {
        String miniMessage = getNormalKickMessageMiniMessage(punishment);
        if (miniMessage.isEmpty()) {
            return Component.empty();
        }
        return MiniMessage.miniMessage().deserialize(miniMessage);
    }


    public static @NotNull String getPunishmentBroadcast(final Punishment<?> punishment, final boolean silent) {
        StringBuilder broadcast = new StringBuilder();

        // Add silent prefix with styling
        if (silent) {
            broadcast.append("<dark_gray>[<gray>SILENT<dark_gray>]</gray> ");
        }

        // Main punishment message with fancy styling
        broadcast.append("<red>⚡ <bold>")
                .append(punishment.getTarget().getTargetName())
                .append("</bold> <gray>has been <red><bold>")
                .append(punishment.getType().toString().toLowerCase())
                .append("ed</bold></red>");

        // Add reason with styling
        if (punishment.getReason().isPresent() && punishment.getReason().isPresent()) {
            broadcast.append(" <dark_gray>» <yellow>")
                    .append(punishment.getReason())
                    .append("</yellow>");
        }

        // Add duration for temporary punishments
        if (!punishment.isPermanent()) {
            broadcast.append(" <dark_gray>(<gold>")
                    .append(TimeUtil.parse(punishment.getDuration()))
                    .append("</gold>)");
        }

        // Add executor with styling

        broadcast.append(" <dark_gray>by <aqua>")
                .append(punishment.getIssuer().getName())
                .append("</aqua>");


        return broadcast.toString();
    }

    public static void broadcastPunishment(final Punishment<?> punishment, final boolean silent) {
        final String msg = getPunishmentBroadcast(punishment, silent);
        if(silent) {
            punishment.getIssuer().sendMsg(msg);
        }else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!player.hasPermission(CardinalPermissions.STAFF_NOTIFY)) continue;
                player.sendRichMessage(msg);
            }
        }
    }
}
