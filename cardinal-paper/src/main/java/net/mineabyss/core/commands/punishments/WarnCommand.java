package net.mineabyss.core.commands.punishments;

import com.mineabyss.lib.commands.BukkitSource;
import com.mineabyss.lib.commands.annotations.Command;
import com.mineabyss.lib.commands.annotations.Dependency;
import com.mineabyss.lib.commands.annotations.Greedy;
import com.mineabyss.lib.commands.annotations.Named;
import com.mineabyss.lib.commands.annotations.Optional;
import com.mineabyss.lib.commands.annotations.Switch;
import com.mineabyss.lib.commands.annotations.Usage;
import com.mineabyss.lib.commands.context.ExecutionContext;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.mineabyss.cardinal.api.config.MessageConfig;
import net.mineabyss.cardinal.api.config.MessageKey;
import net.mineabyss.cardinal.api.punishments.Punishable;
import net.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import net.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import net.mineabyss.core.Cardinal;
import net.mineabyss.core.commands.api.CardinalSource;
import net.mineabyss.core.config.MessageKeys;
import net.mineabyss.core.util.PunishmentMessageUtil;

import java.time.Duration;

@Command("warn")
public class WarnCommand {

    @Dependency
    private MessageConfig config;

    @Usage
    public void def(CardinalSource source, ExecutionContext<BukkitSource> context) {
        source.sendMsg("<red>/" + context.label() + " " + context.getDetectedUsage().formatted());
    }

    @Usage
    public void exec(
            PunishmentIssuer issuer,
            @Named("user")Punishable<?> target,
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

        Cardinal.getInstance().getPunishmentManager()
                .applyPunishment(StandardPunishmentType.WARN, issuer, target, Duration.ZERO, warnReason)
                .onSuccess((punishment)-> {
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
