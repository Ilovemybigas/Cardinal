package eg.mqzen.cardinal.util;

import com.google.common.base.Preconditions;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.Settings;
import eg.mqzen.cardinal.MPlugin;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Configuration loader interface for MineAbyss plugin configurations.
 * <p>
 * Handles loading configuration files with various settings and options.
 * This interface is part of the MineAbyss configuration management system.
 *
 * @since 1.0
 * @author MineAbyss
 */
public sealed interface ConfigLoader {

    /**
     * Gets the name of the configuration file.
     *
     * @return the configuration file name
     */
    @NotNull String fileName();

    /**
     * Gets the parent directory where the configuration file is located.
     *
     * @return the parent directory, or null if using default directory
     */
    @Nullable File parentDirectory();

    /**
     * Determines whether default configuration values should be copied.
     *
     * @return true if defaults should be copied, false otherwise
     */
    boolean copyDefaults();

    /**
     * Gets the array of settings for this configuration.
     *
     * @return the configuration settings
     */
    Settings[] settings();

    /**
     * Creates a new builder to construct a ConfigurationLoader instance.
     *
     * @param fileName the name of the configuration file
     * @return a new builder instance
     * @throws NullPointerException if fileName is null
     */
    static Builder builder(String fileName) {
        return new Builder(fileName);
    }

    /**
     * Builder class for creating ConfigurationLoader instances.
     * <p>
     * Provides a fluent API for configuring and constructing ConfigurationLoader objects.
     */
    class Builder {

        private final String fileName;
        private File parentDirectory;
        private boolean copyDefaults;
        private final List<Settings> settings = new ArrayList<>();

        /**
         * Creates a new Builder with the specified file name.
         *
         * @param fileName the name of the configuration file
         * @throws NullPointerException if fileName is null
         */
        private Builder(String fileName) {
            this.fileName = Objects.requireNonNull(fileName, "fileName cannot be null");
        }

        /**
         * Sets the parent directory for the configuration file.
         *
         * @param parentDirectory the parent directory
         * @return this builder for chaining
         */
        public Builder parentDirectory(File parentDirectory) {
            this.parentDirectory = parentDirectory;
            return this;
        }

        /**
         * Sets whether default configuration values should be copied.
         *
         * @param copyDefaults true to copy defaults, false otherwise
         * @return this builder for chaining
         */
        public Builder copyDefaults(boolean copyDefaults) {
            this.copyDefaults = copyDefaults;
            return this;
        }

        /**
         * Adds multiple settings to the configuration.
         *
         * @param settings the array of settings to add
         * @return this builder for chaining
         */
        public Builder settings(Settings... settings) {
            if (settings != null) {
                this.settings.addAll(Arrays.asList(settings));
            }
            return this;
        }

        /**
         * Adds a single setting to the configuration.
         *
         * @param setting the setting to add
         * @return this builder for chaining
         */
        public Builder addSetting(Settings setting) {
            if (setting != null) {
                this.settings.add(setting);
            }
            return this;
        }

        /**
         * Builds a new ConfigurationLoader instance with the configured parameters.
         *
         * @return a new ConfigurationLoader instance
         */
        public ConfigLoader build() {
            return new ConfigLoaderImpl(
                    fileName,
                    parentDirectory,
                    copyDefaults,
                    settings.toArray(new Settings[0])
            );
        }
    }

    @SneakyThrows
    default YamlDocument loadAsYaml(MPlugin plugin) {
        String fileName = fileName();
        if (fileName.isBlank() || !fileName.endsWith(".yml")) {
            throw new IllegalArgumentException("Invalid fileName '%s'".formatted(fileName));
        }
        File parent = parentDirectory();
        if(parent != null && !parent.exists()) {
            if(parent.mkdirs()) {
               plugin.getLogger().info("Created directory: '" + parent.getCanonicalPath() + "'");
            }
        }
        File file = parent != null ? new File(parent, fileName()) : new File(fileName());

        if (copyDefaults()) {
            var fileDefaults = plugin.getResource(fileName());
            Preconditions.checkNotNull(fileDefaults);
            return YamlDocument.create(file, fileDefaults, settings());
        }

        return YamlDocument.create(file, settings());
    }

    /**
     * Implementation of the ConfigurationLoader interface.
     * <p>
     * This record provides an immutable implementation of the ConfigurationLoader
     * interface for use within MineAbyss plugins.
     */
    record ConfigLoaderImpl(
            String fileName, @Nullable File parentDirectory,
            boolean copyDefaults, Settings... settings
    ) implements ConfigLoader {

    }
}