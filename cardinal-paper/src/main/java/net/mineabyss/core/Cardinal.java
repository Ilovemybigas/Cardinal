package net.mineabyss.core;

import com.mineabyss.lib.ConfigLoader;
import com.mineabyss.lib.bootstrap.MineAbyssPlugin;
import com.mineabyss.lib.commands.BukkitImperat;
import net.mineabyss.core.commands.api.CardinalSource;
import org.jetbrains.annotations.NotNull;

public final class Cardinal extends MineAbyssPlugin {

    public Cardinal(
    ) {
        super(
                ConfigLoader.builder("config.yml")
                .copyDefaults(true)
                .build()
        );
    }


    @Override
    public BukkitImperat loadImperat() {
        return BukkitImperat.builder(this)
                .sourceResolver(CardinalSource.class, CardinalSource::new)
                .build();
    }

    @Override
    protected void registerPluginCommands(@NotNull BukkitImperat bukkitImperat) {

    }

    @Override
    protected void registerPluginListeners() {

    }

    @Override
    protected void onStart() {
        //TODO start core
    }

    @Override
    protected void onStop() {
        //TODO stop core
    }

}
