package eg.mqzen.cardinal;

import dev.dejvokep.boostedyaml.YamlDocument;
import eg.mqzen.cardinal.util.ConfigLoader;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import studio.mevera.imperat.BukkitImperat;

import java.io.IOException;

/**
 * Base plugin class for all MineAbyss plugins.
 * <p>
 * This abstract class provides a standardized structure and lifecycle management
 * for plugins in the MineAbyss ecosystem. It handles configuration loading,
 * command registration, event listening, and proper plugin shutdown procedures.
 *
 * @author MineAbyss
 * @since 1.0
 */
public abstract class MPlugin extends JavaPlugin {

    /** The Imperat command framework instance used by this plugin */
    protected @MonotonicNonNull BukkitImperat imperat;

    /** The loaded YAML configuration document for this plugin */
    @Getter
    protected @Nullable YamlDocument configYaml;

    /** The configuration loader used by this plugin */
    @Getter
    protected @Nullable ConfigLoader configLoader;

    /**
     * Creates a new MineAbyss plugin with the specified command framework and optional configuration loader.
     *
     * @param configLoader the configuration loader, or null if no configuration is needed
     */
    public MPlugin(@Nullable ConfigLoader configLoader) {
        this.configLoader = configLoader;

        if (configLoader != null) {
            loadConfiguration(configLoader);
        }
        loadLibs();
    }

    /**
     * Loads the plugin's configuration using the provided configuration loader.
     * This method can be called to reload the configuration at runtime.
     *
     * @return true if configuration was loaded successfully, false otherwise
     * @throws IllegalStateException if no configuration loader was set
     */
    public boolean loadConfiguration() {
        if (configLoader == null) {
            throw new IllegalStateException("Cannot load configuration: no configuration loader has been set");
        }

        try {
            this.configYaml = configLoader.loadAsYaml(this);
            return true;
        } catch (Exception e) {
            getLogger().severe("Failed to load configuration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets or updates the configuration loader for this plugin.
     * If a loader is provided, the configuration will be loaded immediately.
     *
     * @param configLoader the new configuration loader
     * @return true if configuration was loaded successfully, false otherwise
     */
    public boolean setConfigurationLoader(@Nullable ConfigLoader configLoader) {
        this.configLoader = configLoader;

        if (configLoader != null) {
            return loadConfiguration();
        }

        this.configYaml = null;
        return true;
    }

    /**
     * Called when the plugin is enabled. This implementation follows a standard
     * startup procedure and should not be overridden directly. Instead, override
     * the lifecycle hook methods like {@link #onStart()}, {@link #onPostStart()}, etc.
     */
    @Override
    public final synchronized void onEnable() {
        try {
            getLogger().info("Initializing " + getName() + "...");

            // Pre-initialization phase
            onPreStart();

            // Main initialization phase
            getLogger().info("Starting " + getName() + "...");
            onStart();

            // Register commands
            getLogger().info("Registering commands...");
            this.imperat = loadImperat();
            if(imperat != null) {
                registerPluginCommands(imperat);
            }else {
                getLogger().warning("Imperat is null, commands will not be registered.");
            }
            // Register event listeners
            getLogger().info("Registering event listeners...");
            registerPluginListeners();

            // Post-initialization phase
            getLogger().info("Finalizing startup...");
            onPostStart();

            getLogger().info(getName() + " has been enabled successfully!");
        } catch (Exception e) {
            getLogger().severe("Failed to enable " + getName() + ": " + e.getMessage());
            // Disable the plugin to prevent partial initialization
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    /**
     * Called when the plugin is disabled. This implementation follows a standard
     * shutdown procedure and should not be overridden directly. Instead, override
     * the {@link #onStop()} method.
     */
    @Override
    public final void onDisable() {
        synchronized (this) {
            try {
                getLogger().info("Shutting down " + getName() + "...");

                // Call plugin's stop method
                onStop();

                // Save configuration if it exists
                if (configYaml != null) {
                    getLogger().info("Saving configuration...");
                    configYaml.save();
                }

                // Unregister all commands
                getLogger().info("Unregistering commands...");
                if(imperat != null) {
                    imperat.unregisterAllCommands();
                }
                getLogger().info(getName() + " has been disabled successfully!");
            } catch (Exception e) {
                getLogger().severe("Error during plugin shutdown: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public BukkitImperat loadImperat() {
        return BukkitImperat.builder(this)
                .build();
    }

    public void loadLibs() {

    }

    /**
     * Called before the main initialization phase.
     * Override this method to perform early setup tasks.
     */
    protected void onPreStart() {
        // Default implementation does nothing
    }

    /**
     * Called during the main initialization phase.
     * Override this method to perform the core initialization logic.
     */
    protected abstract void onStart();

    /**
     * Called after commands and event listeners have been registered.
     * Override this method to perform finalization tasks after plugin initialization.
     */
    protected void onPostStart() {
        // Default implementation does nothing
    }

    /**
     * Called when the plugin is being disabled.
     * Override this method to perform cleanup tasks before the plugin is shut down.
     */
    protected void onStop() {
        // Default implementation does nothing
    }

    /**
     * Registers all commands for this plugin with the Imperat command framework.
     * Override this method to register your plugin's commands.
     *
     * @param imperat the command framework instance
     */
    protected abstract void registerPluginCommands(@NotNull BukkitImperat imperat);

    /**
     * Registers all event listeners for this plugin.
     * Override this method to register your plugin's event listeners.
     */
    protected abstract void registerPluginListeners();

    /**
     * Checks if the plugin has a configuration loaded.
     *
     * @return true if a configuration has been loaded, false otherwise
     */
    public final boolean hasConfigurationLoaded() {
        return configYaml != null;
    }

    public @Nullable YamlDocument loadConfiguration(ConfigLoader configLoader) {
        if (configLoader == null) {
            throw new IllegalStateException("Cannot load configuration: no configuration loader has been set");
        } else {
            try {
                return configLoader.loadAsYaml(this);
            } catch (Exception e) {
                this.getLogger().severe("Failed to load configuration: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Reloads the plugin's configuration if a configuration loader is present.
     *
     * @return true if configuration was reloaded successfully, false otherwise
     */
    public boolean reloadConfiguration() {

        if (configYaml == null) {
            return false;
        }

        try {
            configYaml.reload();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
}
