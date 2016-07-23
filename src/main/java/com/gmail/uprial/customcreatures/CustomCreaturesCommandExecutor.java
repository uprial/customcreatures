package com.gmail.uprial.customcreatures;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.uprial.customcreatures.common.CustomLogger;

class CustomCreaturesCommandExecutor implements CommandExecutor {
    public static final String COMMAND_NS = "customcreatures";

    private final CustomCreatures plugin;
    private final CustomLogger customLogger;

    CustomCreaturesCommandExecutor(CustomCreatures plugin, CustomLogger customLogger) {
        this.plugin = plugin;
        this.customLogger = customLogger;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(COMMAND_NS)) {
            if((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) {
                if (sender.hasPermission(COMMAND_NS + ".reload")) {
                    plugin.reloadCreaturesConfig();
                    customLogger.userInfo(sender, "CustomCreatures config reloaded.");
                    return true;
                }
            }
            else if((args.length == 0) || (args[0].equalsIgnoreCase("help"))) {
                String helpString = "==== CustomCreatures help ====\n";

                if (sender.hasPermission(COMMAND_NS + ".reload")) {
                    helpString += "/" + COMMAND_NS + " reload - reload config from disk\n";
                }

                customLogger.userInfo(sender, helpString);
                return true;
            }
        }
        return false;
    }    
}
