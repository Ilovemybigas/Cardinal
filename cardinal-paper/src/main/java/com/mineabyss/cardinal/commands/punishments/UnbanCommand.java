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
import com.mineabyss.cardinal.api.punishments.Punishable;
import com.mineabyss.cardinal.api.punishments.Punishment;
import com.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import com.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import com.mineabyss.cardinal.Cardinal;
import com.mineabyss.cardinal.CardinalPermissions;
import com.mineabyss.cardinal.commands.api.AllowsPunishmentID;
import com.mineabyss.cardinal.commands.api.CardinalSource;
import com.mineabyss.cardinal.config.MessageKeys;

import java.util.concurrent.CompletableFuture;

@Command("unban")
@Permission(CardinalPermissions.UNBAN_COMMAND_PERMISSION)
@Description("Unbans a player from the server.")
public class UnbanCommand {

    @Dependency
    private MessageConfig config;

    @Usage
    public void def(CardinalSource source) {
        // /unban
        source.sendMsg("<red>/Unban <user> [reason]");
    }

    @Usage
    public void exec(
            PunishmentIssuer issuer,
            @Named("user") @AllowsPunishmentID Punishable<?> target,
            @Optional @Greedy @Named("reason") String reason) {

        target.fetchPunishment(StandardPunishmentType.BAN)
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
                        issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unban.NOT_BANNED, Placeholder.unparsed("target", target.getTargetName())));
                        //issuer.sendMsg("<gray>Unbanned player <green>" + user.getName());
                    }
                    else {
                        issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unban.SUCCESS, Placeholder.unparsed("target", target.getTargetName())));
                    }
                });

    }


}
