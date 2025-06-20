package com.mineabyss.cardinal;

import com.mineabyss.lib.ConfigLoader;
import com.mineabyss.lib.Events;
import com.mineabyss.lib.bootstrap.MineAbyssPlugin;
import com.mineabyss.lib.commands.BukkitImperat;
import com.mineabyss.lib.commands.BukkitSource;
import com.mineabyss.lib.commands.context.ExecutionContext;
import com.mineabyss.lib.commands.util.TypeWrap;
import com.mineabyss.lib.gui.Lotus;
import lombok.Getter;
import com.mineabyss.cardinal.api.CardinalAPI;
import com.mineabyss.cardinal.api.CardinalProvider;
import com.mineabyss.cardinal.api.config.MessageConfig;
import com.mineabyss.cardinal.api.punishments.Punishable;
import com.mineabyss.cardinal.api.punishments.PunishmentIssuer;
import com.mineabyss.cardinal.api.punishments.PunishmentManager;
import com.mineabyss.cardinal.api.storage.StorageException;
import com.mineabyss.cardinal.commands.api.PunishableParameterType;
import com.mineabyss.cardinal.commands.punishments.HistoryCommand;
import com.mineabyss.cardinal.commands.punishments.KickCommand;
import com.mineabyss.cardinal.commands.punishments.UnMuteCommand;
import com.mineabyss.cardinal.commands.api.CardinalSource;
import com.mineabyss.cardinal.commands.api.DurationParameterType;
import com.mineabyss.cardinal.commands.api.exceptions.CardinalSourceException;
import com.mineabyss.cardinal.commands.punishments.BanCommand;
import com.mineabyss.cardinal.commands.punishments.MuteCommand;
import com.mineabyss.cardinal.commands.punishments.UnbanCommand;
import com.mineabyss.cardinal.commands.punishments.WarnCommand;
import com.mineabyss.cardinal.config.YamlMessageConfig;
import com.mineabyss.cardinal.listener.BanListener;
import com.mineabyss.cardinal.listener.MuteListener;
import com.mineabyss.cardinal.punishments.StandardPunishmentManager;
import com.mineabyss.cardinal.punishments.issuer.PunishmentIssuerFactory;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class Cardinal extends MineAbyssPlugin implements CardinalAPI {

    @Getter private static Cardinal instance;

    private MessageConfig config;
    private PunishmentManager punishmentManager;

    @Getter private Lotus lotus;

    public Cardinal(
    ) {
        super(null);
    }


    @Override
    public BukkitImperat loadImperat() {
        return BukkitImperat.builder(this)
                .dependencyResolver(MessageConfig.class, ()-> config)
                .contextResolver(new TypeWrap<ExecutionContext<BukkitSource>>(){}.getType(), (ctx, param)-> ctx)
                .sourceResolver(CardinalSource.class, CardinalSource::new)
                .sourceResolver(PunishmentIssuer.class,(source -> {
                    if(source.isConsole()) {
                        return PunishmentIssuerFactory.fromConsole();
                    }
                    return PunishmentIssuerFactory.fromPlayer(source.asPlayer());
                }))
                .throwableResolver(CardinalSourceException.class, (ex, imperat, context)-> {
                    context.source().origin().sendRichMessage(ex.getMsg());
                })
                .parameterType(Duration.class, new DurationParameterType())
                .parameterType(new TypeWrap<Punishable<?>>(){}.getType(), new PunishableParameterType())
                .build();
    }

    @Override
    protected void registerPluginCommands(@NotNull BukkitImperat bukkitImperat) {
        bukkitImperat.registerCommands(
                new KickCommand(),
                new BanCommand(),
                new UnbanCommand(),
                new MuteCommand(),
                new UnMuteCommand(),
                new WarnCommand(),
                new HistoryCommand()
        );
    }

    @Override
    protected void registerPluginListeners() {
        Events.listen(this,
                new BanListener(),
                new MuteListener()
        );
    }

    @Override
    protected void onPreStart() {
        instance = this;
        CardinalProvider.load(instance);

        this.configLoader = ConfigLoader.builder("config.yml")
                .parentDirectory(getDataFolder())
                .copyDefaults(true)
                .build();
        this.loadConfiguration();

        config = new YamlMessageConfig(
                loadConfiguration(ConfigLoader.builder("language_en.yml")
                .parentDirectory(getDataFolder())
                .copyDefaults(true)
                .build())
        );
    }

    @Override
    protected void onStart() {
        this.lotus = Lotus.load(this);
        try {
            punishmentManager = StandardPunishmentManager.createNew(this.configYaml);
        } catch (StorageException e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

    }

    @Override
    protected void onStop() {
        //TODO stop core
    }

    public static void log(String msg, Object... args) {
        instance.getLogger().info(String.format(msg, args));
    }

    public static void warn(String msg, Object... args) {
        instance.getLogger().warning(String.format(msg, args));
    }

    public static void severe(String msg, Object... args) {
        instance.getLogger().severe(String.format(msg, args));
    }

    @NotNull
    @Override
    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }

    @NotNull
    @Override
    public MessageConfig getMessagesConfig() {
        return config;
    }

}
