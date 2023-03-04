package com.yj0524;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Main extends JavaPlugin implements Listener {

    public String deathMessage;
    public String naturalDeathMessage;
    public boolean deathMessageEnable;
    public boolean deathMessageShow;

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
        deathMessage = config.getString("deathMessage", "&a%deathPlayer%&r 님이 &c%killer%&r 님에게 죽었습니다.");
        naturalDeathMessage = config.getString("naturalDeathMessage", "&a%deathPlayer%&r 님이 &c자연사&r했습니다.");
        deathMessageEnable = config.getBoolean("deathMessageEnable", true);
        deathMessageShow = config.getBoolean("deathMessageShow", true);
        // Save config
        config.set("deathMessage", deathMessage);
        config.set("naturalDeathMessage", naturalDeathMessage);
        config.set("deathMessageEnable", deathMessageEnable);
        config.set("deathMessageShow", deathMessageShow);
        saveConfig();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!deathMessageShow) {
            event.deathMessage(null);
        }
        else if (deathMessageEnable && deathMessageShow) {
            String deathMessage = this.deathMessage.replace("&", "§");
            String message = deathMessage.replace("%deathPlayer%", event.getEntity().getName());
            if (event.getEntity().getKiller() != null) {
                message = message.replace("%killer%", event.getEntity().getKiller().getName());
            }
            else {
                String naturalDeathMessage = this.naturalDeathMessage.replace("&", "§");
                message = naturalDeathMessage.replace("%deathPlayer%", event.getEntity().getName());
            }
            event.setDeathMessage(message);
        }
    }
}
