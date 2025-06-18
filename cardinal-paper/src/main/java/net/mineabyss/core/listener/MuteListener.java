package net.mineabyss.core.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.mineabyss.cardinal.api.CardinalProvider;
import net.mineabyss.cardinal.api.punishments.Punishment;
import net.mineabyss.cardinal.api.punishments.PunishmentScanResult;
import net.mineabyss.cardinal.api.punishments.StandardPunishmentType;
import net.mineabyss.core.util.PunishmentMessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import java.util.Objects;

public class MuteListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onChat(AsyncChatEvent event) {

        Player player = event.getPlayer();

        String ipAddress = Objects.requireNonNull(player.getAddress()).getAddress().getHostAddress();

        PunishmentScanResult result = CardinalProvider.provide().getPunishmentManager()
                .scan(player.getUniqueId(), ipAddress, StandardPunishmentType.MUTE)
                .join();

        if(result.failed()) {
            result.log();
        }

        var punishmentContainer = result.getFoundPunishment();

        if(punishmentContainer.isPresent()) {
            Punishment<?> punishment = punishmentContainer.get();
            player.sendMessage(PunishmentMessageUtil.getMuteChatBlock(punishment));
            event.setCancelled(true);
        }


    }

}
