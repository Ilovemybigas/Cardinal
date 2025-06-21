package com.mineabyss.cardinal.commands.punishments;

import com.mineabyss.cardinal.api.punishments.Punishment;
import com.mineabyss.cardinal.util.Pair;
import com.mineabyss.lib.commands.BukkitSource;
import com.mineabyss.lib.commands.annotations.Command;
import com.mineabyss.lib.commands.annotations.ContextResolved;
import com.mineabyss.lib.commands.annotations.Dependency;
import com.mineabyss.lib.commands.annotations.Greedy;
import com.mineabyss.lib.commands.annotations.Named;
import com.mineabyss.lib.commands.annotations.Optional;
import com.mineabyss.lib.commands.annotations.Switch;
import com.mineabyss.lib.commands.annotations.Usage;
import com.mineabyss.lib.commands.context.ExecutionContext;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import com.mineabyss.cardinal.api.config.MessageConfig;
import com.mineabyss.cardinal.api.config.MessageKey;
import com.mineabyss.cardinal.api.punishments.Punishable;
import com.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import com.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import com.mineabyss.cardinal.Cardinal;
import com.mineabyss.cardinal.commands.api.CardinalSource;
import com.mineabyss.cardinal.config.MessageKeys;
import com.mineabyss.cardinal.util.PunishmentMessageUtil;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Command("warn")
public class WarnCommand {

    @Dependency
    private MessageConfig config;

    @Usage
    public void def(CardinalSource source, @ContextResolved ExecutionContext<BukkitSource> context) {
        source.sendMsg("<red>/" + context.label() + " " + context.getDetectedUsage().formatted());
    }

    @Usage
    public void exec(
            PunishmentIssuer issuer,
            @Named("user") CompletableFuture<Punishable<?>> targetFuture,
            @Switch({"silent", "s"}) boolean silent,
            @Optional @Greedy String reason
    ) {

        String warnReason;
        if (reason == null) {
            assert Cardinal.getInstance().getConfigYaml() != null;
            warnReason = Cardinal.getInstance().getConfigYaml().getString("default-reason");
        } else {
            warnReason = reason;
        }

        targetFuture.thenApplyAsync((target)-> {

            Punishment<?> punishment = Cardinal.getInstance().getPunishmentManager()
                    .applyPunishment(StandardPunishmentType.WARN, issuer, target, Duration.ZERO, warnReason).join();

            return new Pair<>(target, punishment);
        })
        .whenComplete((data, ex)-> {
            if(ex != null) {ex.printStackTrace();}
            var target = data.left();
            var punishment = data.right();
            TagResolver resolver = punishment.asTagResolver();
            //we notify the victim
            target.sendMsg(config.getMessage(MessageKeys.Punishments.Warn.NOTIFICATION, resolver));

            //we broadcast the message
            MessageKey normalKey = MessageKeys.Punishments.Warn.BROADCAST;
            MessageKey silentKey = MessageKeys.Punishments.Warn.BROADCAST_SILENT;
            PunishmentMessageUtil.broadcastPunishment(normalKey, silentKey, punishment, silent);

            // success
            issuer.sendMsg(config.getMessage(MessageKeys.Punishments.Warn.SUCCESS, resolver));
        });



    }

}
