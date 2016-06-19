package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.common.CustomLogger;
import com.gmail.uprial.customcreatures.common.InvalidConfigException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import static com.gmail.uprial.customcreatures.CustomCreaturesCommandExecutor.COMMAND_NS;

public final class CustomCreatures extends JavaPlugin {
	private CustomLogger customLogger;
	private CreaturesConfig creaturesConfig;
	private CreatureSpawnListener creatureSpawnListener;
	
    @Override
    public void onEnable() {
    	saveDefaultConfig();
    	customLogger = new CustomLogger(getLogger());
    	creaturesConfig = loadConfig(getConfig(), customLogger);
    	creatureSpawnListener = new CreatureSpawnListener(this, customLogger);

    	getServer().getPluginManager().registerEvents(creatureSpawnListener, this);
    	getCommand(COMMAND_NS).setExecutor(new CustomCreaturesCommandExecutor(this, customLogger));
    	customLogger.info("Plugin enabled");
    }
    
    public CreaturesConfig getCreaturesConfig() {
    	return creaturesConfig;
    }
    
    public void reloadCreaturesConfig() {
		reloadConfig();
		creaturesConfig = loadConfig(getConfig(), customLogger);
    }
    
    @Override
    public void onDisable() {
    	HandlerList.unregisterAll(creatureSpawnListener);
    	customLogger.info("Plugin disabled");
    }

	protected static CreaturesConfig loadConfig(FileConfiguration config, CustomLogger customLogger) {
		CreaturesConfig creaturesConfig = null;
		try {
			creaturesConfig = new CreaturesConfig(config, customLogger);
		} catch (InvalidConfigException e) {
			customLogger.error(e.getMessage());
		}

		return creaturesConfig;
	}
}
