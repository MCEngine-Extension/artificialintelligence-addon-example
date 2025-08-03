package io.github.mcengine.addon.example;

import io.github.mcengine.api.core.MCEngineCoreApi;
import io.github.mcengine.api.core.extension.logger.MCEngineExtensionLogger;
import io.github.mcengine.api.artificialintelligence.extension.addon.IMCEngineArtificialIntelligenceAddOn;

import io.github.mcengine.addon.example.AddOnCommand;
import io.github.mcengine.addon.example.AddOnListener;
import io.github.mcengine.addon.example.AddOnTabCompleter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Main class for the ExampleAddOn.
 * <p>
 * Registers the /exampleaddon command and event listeners.
 */
public class ExampleAddOn implements IMCEngineArtificialIntelligenceAddOn {

    /**
     * Initializes the ExampleAddOn.
     * Called automatically by the MCEngine core plugin.
     *
     * @param plugin The Bukkit plugin instance.
     */
    @Override
    public void onLoad(Plugin plugin) {
        MCEngineExtensionLogger logger = new MCEngineExtensionLogger(plugin, "AddOn", "ExampleAddOn");

        try {
            // Register event listener
            PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new AddOnListener(plugin), plugin);

            // Reflectively access Bukkit's CommandMap
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

            // Define the /exampleaddon command
            Command exampleAddOnCommand = new Command("exampleaddon") {
                /**
                 * Handles logic for /exampleaddon command.
                 */
                private final AddOnCommand handler = new AddOnCommand();

                /**
                 * Provides tab-completion for /exampleaddon.
                 */
                private final AddOnTabCompleter completer = new AddOnTabCompleter();

                /**
                 * Executes the /exampleaddon command.
                 *
                 * @param sender The command sender.
                 * @param label  The command label.
                 * @param args   The command arguments.
                 * @return true if successful.
                 */
                @Override
                public boolean execute(CommandSender sender, String label, String[] args) {
                    return handler.onCommand(sender, this, label, args);
                }

                /**
                 * Handles tab-completion for the /exampleaddon command.
                 *
                 * @param sender The command sender.
                 * @param alias  The alias used.
                 * @param args   The current arguments.
                 * @return A list of possible completions.
                 */
                @Override
                public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
                    return completer.onTabComplete(sender, this, alias, args);
                }
            };

            exampleAddOnCommand.setDescription("Example AddOn command.");
            exampleAddOnCommand.setUsage("/exampleaddon");

            // Dynamically register the /exampleaddon command
            commandMap.register(plugin.getName().toLowerCase(), exampleAddOnCommand);

            logger.info("Enabled successfully.");
        } catch (Exception e) {
            logger.warning("Failed to initialize ExampleAddOn: " + e.getMessage());
            e.printStackTrace();
        }

        // Check for updates
        MCEngineCoreApi.checkUpdate(plugin, logger.getLogger(),
            "github", "MCEngine-AddOn", "example",
            plugin.getConfig().getString("github.token", "null"));
    }

    @Override
    public void onDisload(Plugin plugin) {
        // No specific unload logic
    }

    @Override
    public void setId(String id) {
        MCEngineCoreApi.setId("example-addon");
    }
}
