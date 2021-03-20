package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import com.gmail.uprial.customcreatures.listeners.CustomCreaturesAttackEventListener;
import com.gmail.uprial.customcreatures.listeners.CustomCreaturesDeathEventListener;
import com.gmail.uprial.customcreatures.listeners.CustomCreaturesSpawnEventListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;

import static com.gmail.uprial.customcreatures.CustomCreaturesCommandExecutor.COMMAND_NS;

public final class CustomCreatures extends JavaPlugin {
    private final String CONFIG_FILE_NAME = "config.yml";
    private final File configFile = new File(getDataFolder(), CONFIG_FILE_NAME);

    private CustomLogger consoleLogger = null;
    private CreaturesConfig creaturesConfig = null;

    private BukkitTask cronTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        cronTask = new CustomCreaturesCron(this).runTaskTimer();

        consoleLogger = new CustomLogger(getLogger());
        creaturesConfig = loadConfig(getConfig(), consoleLogger);

        getServer().getPluginManager().registerEvents(new CustomCreaturesSpawnEventListener(this, consoleLogger), this);
        getServer().getPluginManager().registerEvents(new CustomCreaturesAttackEventListener(this, consoleLogger), this);
        getServer().getPluginManager().registerEvents(new CustomCreaturesDeathEventListener(this, consoleLogger), this);

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
        HandlerList.unregisterAll(this);
        cronTask.cancel();
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
