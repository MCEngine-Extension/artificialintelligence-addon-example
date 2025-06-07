package io.github.mcengine.addon.artificialintelligence.example;

import io.github.mcengine.api.mcengine.addon.IMCEngineAddOn;
import io.github.mcengine.api.mcengine.addon.MCEngineAddOnLogger;
import io.github.mcengine.addon.example.command.AddOnCommand;
import io.github.mcengine.addon.example.listener.AddOnListener;
import io.github.mcengine.addon.example.tabcompleter.AddOnTabCompleter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Example AddOn for MCEngineArtificialIntelligence.
 */
public class ExampleAddOn implements IMCEngineAddOn {
    @Override
    public void onLoad(Plugin plugin) {
        MCEngineAddOnLogger logger = new MCEngineAddOnLogger(plugin, "Example-AddOn");

        try {
            // Register listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new AddOnListener(plugin), plugin);

            // Access Bukkit's CommandMap reflectively
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define and register the /exampleaddon command
            Command exampleAddOnCommand = new Command("exampleaddon") {
                private final AddOnCommand handler = new AddOnCommand();
                private final AddOnTabCompleter completer = new AddOnTabCompleter();

                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            exampleAddOnCommand.setDescription("Example AddOn command.");
            exampleAddOnCommand.setUsage("/exampleaddon");

            commandMap.register(plugin.getName().toLowerCase(), exampleAddOnCommand);

            logger.info("Enabled successfully.");
        } catch (Exception e) {
            logger.warning("Failed to initialize Example-AddOn: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
