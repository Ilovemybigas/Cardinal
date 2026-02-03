package eg.mqzen.cardinal.commands.api;

import eg.mqzen.cardinal.commands.api.exceptions.CardinalSourceException;

import eg.mqzen.cardinal.util.TimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.imperat.BukkitSource;
import studio.mevera.imperat.command.parameters.OptionalValueSupplier;
import studio.mevera.imperat.command.parameters.type.BaseParameterType;
import studio.mevera.imperat.context.ExecutionContext;
import studio.mevera.imperat.context.internal.CommandInputStream;
import studio.mevera.imperat.exception.ImperatException;

import java.time.Duration;

public class DurationParameterType extends BaseParameterType<BukkitSource, Duration> {

    public DurationParameterType() {
        super();
    }

    @Nullable @Override
    public Duration resolve(
            @NotNull ExecutionContext<BukkitSource> context,
            @NotNull CommandInputStream<BukkitSource> inputStream,
            @NotNull String input
    ) throws ImperatException {
        try{
            return TimeUtil.parse(input);
        }catch (Exception exception) {
            throw new CardinalSourceException("<red>Invalid duration '%s'", context, input);
        }
    }

    @Override
    public OptionalValueSupplier supplyDefaultValue() {
        return OptionalValueSupplier.of("permanent");
    }
}
