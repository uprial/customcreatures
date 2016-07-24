package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import static com.gmail.uprial.customcreatures.CustomCreaturesCommandExecutor.COMMAND_NS;

public final class CustomCreatures extends JavaPlugin {
    private CustomLogger customLogger = null;
    private CreaturesConfig creaturesConfig = null;
    private CustomCreaturesEventListener customCreaturesEventListener = null;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        customLogger = new CustomLogger(getLogger());
        creaturesConfig = loadConfig(getConfig(), customLogger);
        customCreaturesEventListener = new CustomCreaturesEventListener(this, customLogger);

        getServer().getPluginManager().registerEvents(customCreaturesEventListener, this);
        getCommand(COMMAND_NS).setExecutor(new CustomCreaturesCommandExecutor(this));
        customLogger.info("Plugin enabled");
    }

    public CreaturesConfig getCreaturesConfig() {
        return creaturesConfig;
    }

    public void reloadCreaturesConfig(CustomLogger customLogger) {
        reloadConfig();
        creaturesConfig = loadConfig(getConfig(), customLogger);
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(customCreaturesEventListener);
        customLogger.info("Plugin disabled");
    }

    static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger customLogger) {
        CreaturesConfig creaturesConfig = null;
        try {
            creaturesConfig = new CreaturesConfig(config, customLogger);
        } catch (InvalidConfigException e) {
            customLogger.error(e.getMessage());
        }

        return creaturesConfig;
    }
}
