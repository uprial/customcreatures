package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import static com.gmail.uprial.customcreatures.CustomCreaturesCommandExecutor.COMMAND_NS;

public final class CustomCreatures extends JavaPlugin {
    private CustomLogger consoleLogger = null;
    private CreaturesConfig creaturesConfig = null;
    private CustomCreaturesEventListener customCreaturesEventListener = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        consoleLogger = new CustomLogger(getLogger());
        creaturesConfig = loadConfig(getConfig(), consoleLogger);
        customCreaturesEventListener = new CustomCreaturesEventListener(this, consoleLogger);

        getServer().getPluginManager().registerEvents(customCreaturesEventListener, this);
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
        HandlerList.unregisterAll(customCreaturesEventListener);
        consoleLogger.info("Plugin disabled");
    }

    static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger customLogger) {
        return loadConfig(config, customLogger, null);
    }

    static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger mainLogger, CustomLogger secondLogger) {
        CreaturesConfig creaturesConfig = null;
        try {
            boolean isDebugMode = CreaturesConfig.isDebugMode(config, mainLogger);
            mainLogger.setDebugMode(isDebugMode);
            if(secondLogger != null) {
                secondLogger.setDebugMode(isDebugMode);
            }

            creaturesConfig = new CreaturesConfig(config, mainLogger);
        } catch (InvalidConfigException e) {
            mainLogger.error(e.getMessage());
        }

        return creaturesConfig;
    }
}
