package com.mineabyss.cardinal.commands.punishments;

import com.mineabyss.lib.commands.annotations.Command;
import com.mineabyss.lib.commands.annotations.Dependency;
import com.mineabyss.lib.commands.annotations.Description;
import com.mineabyss.lib.commands.annotations.Greedy;
import com.mineabyss.lib.commands.annotations.Named;
import com.mineabyss.lib.commands.annotations.Optional;
import com.mineabyss.lib.commands.annotations.Permission;
import com.mineabyss.lib.commands.annotations.Usage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import com.mineabyss.cardinal.api.config.MessageConfig;
import com.mineabyss.cardinal.api.punishments.Punishment;
import com.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import com.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import com.mineabyss.cardinal.Cardinal;
import com.mineabyss.cardinal.CardinalPermissions;
import com.mineabyss.cardinal.commands.api.CardinalSource;
import com.mineabyss.cardinal.config.MessageKeys;
import org.bukkit.OfflinePlayer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Command("unmute")
@Permission(CardinalPermissions.UNMUTE_COMMAND_PERMISSION)
@Description("Unmutes a player on the server.")
public final class UnMuteCommand {

    @Dependency
    private MessageConfig config;

    @Usage
    public void def(CardinalSource source) {
        source.sendMsg("<red>Unmute <user> [reason...]");
    }

    @Usage
    public void unmute(
            PunishmentIssuer issuer,
            @Named("user")OfflinePlayer user,
            @Named("reason") @Greedy @Optional String reason
    ) {

        if(user.getName() == null) {
            issuer.sendMsg("<red>User doesn't exist !");
            return;
        }

        UUID userUUID = user.getUniqueId();
        Cardinal.getInstance().getPunishmentManager()
                .getActivePunishment(userUUID, StandardPunishmentType.MUTE)
                .thenCompose((punishmentContainer)-> {

                    if(punishmentContainer.isEmpty()) {
                        return CompletableFuture.completedFuture(false);
                    }else {

                        Punishment<?> punishment = punishmentContainer.get();
                        return Cardinal.getInstance().getPunishmentManager()
                                .revokePunishment(punishment.getId(), issuer, reason).unwrap();
                    }

                })
                .onSuccess((revoked)-> {

                    if(revoked) {
                        //send success to user
                        issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unmute.NOT_MUTED, Placeholder.unparsed("target", user.getName()),
                                Placeholder.unparsed("reason", reason)));
                    }
                    else {
                        issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unmute.SUCCESS, Placeholder.unparsed("target", user.getName()), Placeholder.unparsed("reason", reason)));
                    }
                });

    }



}
