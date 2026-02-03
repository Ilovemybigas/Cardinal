package eg.mqzen.cardinal.commands.api.exceptions;

import lombok.Getter;
import studio.mevera.imperat.context.Context;
import studio.mevera.imperat.exception.ImperatException;

public final class CardinalSourceException extends ImperatException {

    @Getter
    private final String msg;

    public  CardinalSourceException(String msg, Context<?> ctx, Object... args) {
        super(ctx);
        this.msg = String.format(msg, args);
    }

}
