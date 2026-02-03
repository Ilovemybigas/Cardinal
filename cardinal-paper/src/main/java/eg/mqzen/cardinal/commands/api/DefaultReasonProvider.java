package eg.mqzen.cardinal.commands.api;

import dev.dejvokep.boostedyaml.YamlDocument;
import eg.mqzen.cardinal.Cardinal;

import org.jetbrains.annotations.Nullable;
import studio.mevera.imperat.command.parameters.CommandParameter;
import studio.mevera.imperat.command.parameters.OptionalValueSupplier;
import studio.mevera.imperat.context.ExecutionContext;
import studio.mevera.imperat.context.Source;

public final class DefaultReasonProvider implements OptionalValueSupplier {

    @Nullable
    @Override
    public <S extends Source> String supply(ExecutionContext<S> context, CommandParameter<S> parameter) {
        YamlDocument configYaml = Cardinal.getInstance().getConfigYaml();
        if(configYaml == null) {
            return "Breaking Server Rules";
        }
        return configYaml.getString("default-reason");
    }
}
