package com.yj0524;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    public String joinMessage;
    public String leftMessage;
    public boolean joinMessageEnable;
    public boolean leftMessageEnable;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Plugin Enabled");

        loadConfig();
        File cfile = new File(getDataFolder(), "config.yml");
        if (cfile.length() == 0) {
            getConfig().options().copyDefaults(true);
            saveConfig();
        }

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Plugin Disabled");
    }

    private void loadConfig() {
        // Load chest size from config
        FileConfiguration config = getConfig();
        joinMessage = config.getString("joinMessage", "[&a+&r] %player% 님이 입장했습니다.");
        leftMessage = config.getString("leftMessage", "[&c-&r] %player% 님이 퇴장했습니다.");
        joinMessageEnable = config.getBoolean("joinMessageEnable", true);
        leftMessageEnable = config.getBoolean("leftMessageEnable", true);
        // Save config
        config.set("joinMessage", joinMessage);
        config.set("leftMessage", leftMessage);
        config.set("joinMessageEnable", joinMessageEnable);
        config.set("leftMessageEnable", leftMessageEnable);
        saveConfig();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (joinMessageEnable) {
            String joinMessage = this.joinMessage.replace("&", "§");
            String message = joinMessage.replace("%player%", event.getPlayer().getDisplayName());
            event.setJoinMessage(message);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (leftMessageEnable) {
            String leftMessage = this.leftMessage.replace("&", "§");
            String message = leftMessage.replace("%player%", event.getPlayer().getDisplayName());
            event.setQuitMessage(message);
        }
    }
}
