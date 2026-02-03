package eg.mqzen.cardinal.commands.punishments;

import eg.mqzen.cardinal.Cardinal;
import eg.mqzen.cardinal.CardinalPermissions;
import eg.mqzen.cardinal.api.config.MessageConfig;
import eg.mqzen.cardinal.api.punishments.Punishable;
import eg.mqzen.cardinal.api.punishments.Punishment;
import eg.mqzen.cardinal.api.punishments.PunishmentIssuer;
import eg.mqzen.cardinal.api.punishments.StandardPunishmentType;
import eg.mqzen.cardinal.commands.api.AllowsPunishmentID;
import eg.mqzen.cardinal.commands.api.CardinalSource;
import eg.mqzen.cardinal.config.MessageKeys;
import eg.mqzen.cardinal.util.Pair;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import studio.mevera.imperat.annotations.Command;
import studio.mevera.imperat.annotations.Default;
import studio.mevera.imperat.annotations.Dependency;
import studio.mevera.imperat.annotations.Description;
import studio.mevera.imperat.annotations.Greedy;
import studio.mevera.imperat.annotations.Named;
import studio.mevera.imperat.annotations.Permission;
import studio.mevera.imperat.annotations.Usage;

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
            @Named("user") @AllowsPunishmentID CompletableFuture<Punishable<?>> targetFuture,
            @Default("Appealed") @Greedy @Named("reason") String reason) {

        targetFuture.thenApplyAsync((target)-> {
            java.util.Optional<Punishment<?>> punishmentContainer =  target.fetchPunishment(StandardPunishmentType.BAN).unwrap().join();
            return new Pair<>(target, punishmentContainer);
        }).thenApplyAsync((data)-> {

            var punishmentContainer = data.right();
            Punishable<?> target = data.left();

            if(punishmentContainer.isEmpty()) {
                return new Pair<>(false, target);
            }else {

                Punishment<?> punishment = punishmentContainer.get();
                return Cardinal.getInstance().getPunishmentManager()
                        .revokePunishment(punishment.getId(), issuer, reason)
                        .map((revoked)-> new Pair<>(revoked, target))
                        .unwrap()
                        .join();
            }

        }).whenComplete((data, ex)-> {
            if(ex != null) {ex.printStackTrace();}

            var revoked = data.left();
            var target = data.right();
            if(revoked) {
                issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unban.SUCCESS, Placeholder.unparsed("target", target.getTargetName())));
            }
            else {
                issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Unban.NOT_BANNED, Placeholder.unparsed("target", target.getTargetName())));
            }
        });


    }


}
