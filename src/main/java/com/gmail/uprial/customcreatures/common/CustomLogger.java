package com.gmail.uprial.customcreatures.common;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomLogger {
	private boolean debug = true;
			
	private final Logger logger;
	
	public CustomLogger(Logger logger) {
		this.logger = logger;
	}
	
	public void setDebugMode(boolean value) {
		this.debug = value;
	}
	
	public boolean isDebugMode() {
		return this.debug;
	}
	
	public void error(String message) {
		log(Level.SEVERE, String.format("[ERROR] %s", message));
	}

	public void warning(String message) {
		log(Level.WARNING, String.format("[WARNING] %s", message));
	}

	public void debug(String message) {
		if (debug) {
			log(Level.INFO, String.format("[DEBUG] %s", message));
		}
	}

	public void info(String message) {
		log(Level.INFO, message);
	}
	
	public void userError(CommandSender sender, String message) {
    	sender.sendMessage(ChatColor.RED + String.format("ERROR: %s", message));
    	log(Level.INFO, String.format("[user-error] <%s>: %s", sender.getName(), message));
    }

	public String entity2string(Entity entity) {
        Location location = entity.getLocation();
        return String.format("%s[world: %s, x: %.0f, y: %.0f, z: %.0f]",
                entity.getType().toString(),
                null != location.getWorld() ? location.getWorld().getName() : "empty",
                location.getX(), location.getY(), location.getZ());
	}

	public void userInfo(CommandSender sender, String message) {
    	sender.sendMessage(message);
    	log(Level.INFO, String.format("[user-info] <%s>: %s", sender.getName(), message));
    }

	protected void log(Level level, String message) {
		logger.log(level, message);
	}
}
