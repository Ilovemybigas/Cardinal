package eg.mqzen.cardinal;

import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.bootstrap.PluginProviderContext;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("all")
public class MPluginBoot implements PluginBootstrap {

    private @Nullable Consumer<BootstrapContext> beforeInstanceLoading;
    private @Nullable Function<PluginProviderContext, MPlugin> pluginSupplier;

    public MPluginBoot(@Nullable  Consumer<BootstrapContext> beforeInstanceLoading, @Nullable Function<PluginProviderContext, MPlugin> pluginSupplier) {
        this.beforeInstanceLoading = beforeInstanceLoading;
        this.pluginSupplier = pluginSupplier;
    }

    //more constructors for all possibilies of the null fields

    public MPluginBoot(@Nullable Consumer<BootstrapContext> beforeInstanceLoading) {
        this(beforeInstanceLoading, null);
    }
    public MPluginBoot(@Nullable Function<PluginProviderContext, MPlugin>  pluginSupplier) {
        this(null, pluginSupplier);
    }

    public MPluginBoot() {
        this(null, null);
    }

    /**
     * Called by the server, allowing you to bootstrap the plugin with a context that provides things like a logger and your shared plugin configuration file.
     *
     * @param context the server provided context
     */
    @Override
    public void bootstrap(BootstrapContext context) {
        if(beforeInstanceLoading != null) {
            beforeInstanceLoading.accept(context);
        }
    }

    /**
     * Called by the server to instantiate your main class.
     * Plugins may override this logic to define custom creation logic for said instance, like passing addition
     * constructor arguments.
     *
     * @param context the server created bootstrap object
     * @return the server requested instance of the plugins main class.
     */
    @Override
    public JavaPlugin createPlugin(PluginProviderContext context) {
        if(pluginSupplier != null) {
            return pluginSupplier.apply(context);
        } else {
            return PluginBootstrap.super.createPlugin(context);
        }
    }
}
