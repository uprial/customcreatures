package com.gmail.uprial.customcreatures;

import com.gmail.uprial.customcreatures.config.InvalidConfigException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.gmail.uprial.customcreatures.common.CustomLogger;

class CustomCreaturesCommandExecutor implements CommandExecutor {
    public static final String COMMAND_NS = "customcreatures";

    private final CustomCreatures plugin;

    CustomCreaturesCommandExecutor(CustomCreatures plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase(COMMAND_NS)) {
            CustomLogger customLogger = new CustomLogger(plugin.getLogger(), sender);

            if((args.length >= 1) && (args[0].equalsIgnoreCase("reload"))) {
                if (sender.hasPermission(COMMAND_NS + ".reload")) {
                    plugin.reloadCreaturesConfig(customLogger);
                    customLogger.info("CustomCreatures config reloaded.");
                    return true;
                }
            }
            else if((args.length >= 2) && (args[0].equalsIgnoreCase("apply"))) {
                if (sender.hasPermission(COMMAND_NS + ".apply")) {
                    try {
                        final int counter = plugin.apply(customLogger, args[1]);
                        customLogger.info(String.format("CustomCreatures handler '%s' applied to %d entities",
                                args[1], counter));
                    } catch (InvalidConfigException exception) {
                        customLogger.error(exception.getMessage());
                    }
                    return true;
                }
            } else if((args.length == 0) || (args[0].equalsIgnoreCase("help"))) {
                String helpString = "==== CustomCreatures help ====\n";

                if (sender.hasPermission(COMMAND_NS + ".reload")) {
                    helpString += '/' + COMMAND_NS + " reload - reload config from disk\n";
                }
                if (sender.hasPermission(COMMAND_NS + ".apply")) {
                    helpString += '/' + COMMAND_NS + " apply <handler> - apply a handler to currently living entities\n";
                }

                customLogger.info(helpString);
                return true;
            }
        }
        return false;
    }
}
