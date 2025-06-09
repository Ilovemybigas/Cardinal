package net.mineabyss.core.commands.api;

import com.mineabyss.lib.commands.BukkitSource;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class CardinalSource {

    private final BukkitSource source;

    public void sendMsg(String message) {
        source.origin().sendRichMessage(message);
    }
}
