package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomCreatures extends JavaPlugin {
	private CustomLogger customLogger;
	private CreaturesConfig creaturesConfig;
	private CreatureSpawnListener creatureSpawnListener;
	
    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	customLogger = new CustomLogger(getLogger());
    	creaturesConfig = loadConfig(customLogger);
    	creatureSpawnListener = new CreatureSpawnListener(this, customLogger);

    	getServer().getPluginManager().registerEvents(creatureSpawnListener, this);
    	getCommand("customcreatures").setExecutor(new CustomCreaturesCommandExecutor(this, customLogger));
    	customLogger.info("Plugin enabled");
    }
    
    public CreaturesConfig getCreaturesConfig() {
    	return creaturesConfig;
    }
    
    public void reloadCreaturesConfig() {
		reloadConfig();
		creaturesConfig = loadConfig(customLogger);
    }
    
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(creatureSpawnListener);
    	customLogger.info("Plugin disabled");
    }

	private CreaturesConfig loadConfig(CustomLogger customLogger) {
		CreaturesConfig creaturesConfig = null;
		try {
			creaturesConfig = new CreaturesConfig(getConfig(), customLogger);
		} catch (InvalidConfigException e) {
			customLogger.error(e.getMessage());
		}

		return creaturesConfig;
	}
}
