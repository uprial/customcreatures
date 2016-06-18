package com.gmail.uprial.customcreatures;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.uprial.customcreatures.common.CustomLogger;

class CustomCreaturesCommandExecutor implements CommandExecutor {
	private final CustomCreatures plugin;
	private final CustomLogger customLogger;

	public CustomCreaturesCommandExecutor(CustomCreatures plugin, CustomLogger customLogger) {
		this.plugin = plugin;
		this.customLogger = customLogger;
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    	if (command.getName().equalsIgnoreCase("customcreatures")) {
			if((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) {
	    		if (sender.hasPermission("customcreatures.reload")) {
	    			plugin.reloadCreaturesConfig();
	    			customLogger.userInfo(sender, "CustomCreatures config reloaded.");
	    			return true;
	    		}
			}
			else if((args.length == 0) || (args[0].equalsIgnoreCase("help"))) {
				String Help = "==== CustomCreatures help ====\n";

				if (sender.hasPermission("customcreatures.reload"))
					Help += "/customcreatures reload - reload config from disk\n";

				customLogger.userInfo(sender, Help);
    			return true;
			}
    	} 
    	return false; 
    }    
}
