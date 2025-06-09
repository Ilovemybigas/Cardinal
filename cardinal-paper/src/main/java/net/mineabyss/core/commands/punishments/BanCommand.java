package net.mineabyss.core.commands.punishments;

import com.mineabyss.lib.commands.annotations.Command;
import com.mineabyss.lib.commands.annotations.Description;
import com.mineabyss.lib.commands.annotations.Optional;
import com.mineabyss.lib.commands.annotations.Permission;
import com.mineabyss.lib.commands.annotations.Switch;
import com.mineabyss.lib.commands.annotations.Usage;
import net.mineabyss.core.commands.api.CardinalSource;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

@Command("ban")
@Permission("cardinal.command.ban")
@Description("Bans a player from the server.")
public class BanCommand {

    @Usage
    public void defaultUsage(CardinalSource sender) {
        sender.sendMsg("Usage: /ban <player> [reason]");
    }

    @Usage
    public void banPlayer(
            CardinalSource sender,
            String playerName,
            @Switch({"silent", "s"}) boolean silent,
            @Optional @Nullable Duration duration,
            @Optional @Nullable String reason
    ) {

        //TODO implement ban logic here

    }

}
