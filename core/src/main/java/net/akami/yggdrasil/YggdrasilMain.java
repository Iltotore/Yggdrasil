package net.akami.yggdrasil;

import com.google.inject.Inject;
import net.akami.yggdrasil.api.game.events.CancelledEventsListener;
import net.akami.yggdrasil.api.game.events.DamageEventListener;
import net.akami.yggdrasil.api.game.task.GameItemClock;
import net.akami.yggdrasil.api.game.task.YggdrasilScheduler;
import net.akami.yggdrasil.api.input.ItemInteractionsListener;
import net.akami.yggdrasil.api.input.PlayerConnectionListener;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayer;
import net.akami.yggdrasil.api.player.AbstractYggdrasilPlayerManager;
import net.akami.yggdrasil.commands.RebirthCommand;
import net.akami.yggdrasil.player.YggdrasilPlayerManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

@Plugin(id = "yggdrasil", name = "Yggdrasil", version = "alpha", description = "Core plugin for Yggdrasil")
public class YggdrasilMain {

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        logger.info("Plugin successfully initialized");

        AbstractYggdrasilPlayerManager playerManager = new YggdrasilPlayerManager();
        List<AbstractYggdrasilPlayer> players = playerManager.getPlayers();

        GameItemClock clock = YggdrasilScheduler.scheduleItemClock(this);
        YggdrasilScheduler.scheduleManaRestoring(this, players);
        YggdrasilScheduler.scheduleFoodRestoring(this, players);

        registerListeners(Sponge.getEventManager(),
                new PlayerConnectionListener(playerManager),
                new CancelledEventsListener(playerManager),
                new ItemInteractionsListener(players, clock),
                new DamageEventListener(players));

        registerCommand(Sponge.getCommandManager(), new RebirthCommand(playerManager), "rebirth");
        registerLocalSchematic("earthTower");
    }

    private void registerListeners(EventManager manager, Object... listeners) {
        for (Object listener : listeners) {
            manager.registerListeners(this, listener);
        }
    }

    private void registerCommand(CommandManager manager, CommandExecutor executor, String... aliases) {
        manager.register(this, CommandSpec
                .builder()
                .permission("yggdrasil.commands")
                .executor(executor)
                .build(), aliases);

    }

    private void registerLocalSchematic(String name) {
        try {
            InputStream stream = YggdrasilMain.class.getResourceAsStream("/schematics/" + name + ".schematic");
            if(stream == null) {
                logger.warn("Structure not found: " + name);
                return;
            }
            SchematicRegistry.register(name, new GZIPInputStream(stream));
        } catch(IOException e) {
            e.printStackTrace();
            logger.warn("Failed to load structure " + name);
        }
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        logger.info("Plugin successfully stopped");
    }
}
