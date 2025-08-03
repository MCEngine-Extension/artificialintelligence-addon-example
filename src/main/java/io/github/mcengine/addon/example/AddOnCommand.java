package io.github.mcengine.addon.example;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * Handles /exampleaddon command logic.
 */
public class AddOnCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§aExampleAddOn command executed!");
        return true;
    }
}
