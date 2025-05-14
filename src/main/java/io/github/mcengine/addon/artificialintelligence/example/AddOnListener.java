package io.github.mcengine.addon.artificialintelligence.example.listener;

import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Example event listener for ExampleAddOn.
 */
public class AddOnListener implements Listener {
    private final Plugin plugin;

    public AddOnListener(Plugin plugin) {
        this.plugin = plugin;
    }

    // Add your event handlers here
}
