package eg.mqzen.cardinal.commands.api;

import lombok.RequiredArgsConstructor;
import studio.mevera.imperat.BukkitSource;

@RequiredArgsConstructor
public final class CardinalSource {

    private final BukkitSource source;

    public void sendMsg(String message) {
        source.origin().sendRichMessage(message);
    }
}
