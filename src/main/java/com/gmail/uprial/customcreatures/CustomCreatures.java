package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.listeners.CustomCreaturesAttackEventListener;
import com.gmail.uprial.customcreatures.listeners.CustomCreaturesSpawnEventListener;
import com.gmail.uprial.customcreatures.schema.HItemAttributes;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.UUID;

import static com.gmail.uprial.customcreatures.CustomCreaturesCommandExecutor.COMMAND_NS;

public final class CustomCreatures extends JavaPlugin {
    private final String CONFIG_FILE_NAME = "config.yml";
    private final File configFile = new File(getDataFolder(), CONFIG_FILE_NAME);

    private CustomLogger consoleLogger = null;
    private CreaturesConfig creaturesConfig = null;
    private CustomCreaturesSpawnEventListener customCreaturesSpawnEventListener = null;
    private CustomCreaturesAttackEventListener customCreaturesAttackEventListener = null;

    private int cronTaskId;
    private int playerTrackerTaskId;

    @Override
    public void onEnable() {
        HItemAttributes.setBackwardCompatibility(true);

        saveDefaultConfig();

        cronTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this,
                new CustomCreaturesCron(), CustomCreaturesCron.getInterval(), CustomCreaturesCron.getInterval());
        playerTrackerTaskId = getServer().getScheduler().scheduleSyncRepeatingTask(this,
                new CustomCreaturesPlayerTracker(this), CustomCreaturesPlayerTracker.getInterval(), CustomCreaturesPlayerTracker.getInterval());

        consoleLogger = new CustomLogger(getLogger());
        creaturesConfig = loadConfig(getConfig(), consoleLogger);

        customCreaturesSpawnEventListener = new CustomCreaturesSpawnEventListener(this, consoleLogger);
        getServer().getPluginManager().registerEvents(customCreaturesSpawnEventListener, this);

        customCreaturesAttackEventListener = new CustomCreaturesAttackEventListener(this, consoleLogger);
        getServer().getPluginManager().registerEvents(customCreaturesAttackEventListener, this);

        getCommand(COMMAND_NS).setExecutor(new CustomCreaturesCommandExecutor(this));
        consoleLogger.info("Plugin enabled");
    }

    public CreaturesConfig getCreaturesConfig() {
        return creaturesConfig;
    }

    public void reloadCreaturesConfig(CustomLogger userLogger) {
        reloadConfig();
        creaturesConfig = loadConfig(getConfig(), userLogger, consoleLogger);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(customCreaturesAttackEventListener);
        HandlerList.unregisterAll(customCreaturesSpawnEventListener);
        getServer().getScheduler().cancelTask(playerTrackerTaskId);
        getServer().getScheduler().cancelTask(cronTaskId);
        consoleLogger.info("Plugin disabled");
    }

    @Override
    public void saveDefaultConfig() {
        if (!configFile.exists()) {
            saveResource(CONFIG_FILE_NAME, false);
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return YamlConfiguration.loadConfiguration(configFile);
    }

    public static void defer(Runnable task) {
        CustomCreaturesCron.defer(task);
    }

    public Player getOnlinePlayerByUUID(UUID uuid) {
        for (Player player : getServer().getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) {
                return player;
            }
        }

        return null;
    }

    static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger customLogger) {
        return loadConfig(config, customLogger, null);
    }

    private static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger mainLogger, CustomLogger secondLogger) {
        CreaturesConfig creaturesConfig = null;
        try {
            boolean isDebugMode = CreaturesConfig.isDebugMode(config, mainLogger);
            mainLogger.setDebugMode(isDebugMode);
            if(secondLogger != null) {
                secondLogger.setDebugMode(isDebugMode);
            }

            creaturesConfig = CreaturesConfig.getFromConfig(config, mainLogger);
        } catch (InvalidConfigException e) {
            mainLogger.error(e.getMessage());
        }

        return creaturesConfig;
    }
}
