package br.com.delgadoo.dreps;

import br.com.delgadoo.dreps.commands.RepCommand;
import br.com.delgadoo.dreps.events.onPlayerJoinListener;
import br.com.delgadoo.dreps.models.database.ConnectionModel;
import br.com.delgadoo.dreps.models.database.MySQLConnection;
import br.com.delgadoo.dreps.models.database.SQLiteConnection;
import br.com.delgadoo.dreps.statements.Statements;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.SQLException;

public class dReps extends JavaPlugin {

    @Getter private static dReps instance;


    public static File database;
    public static ConnectionModel connectionModel;

    @Override
    public void onEnable() {
        instance = this;
        if (!new File(getDataFolder(), "config.yml").exists()) saveDefaultConfig();
        database = new File(getDataFolder(), "database.db");
        connectionModel = getConfig().getBoolean("Database.mysql") ? new MySQLConnection(getConfig().getString("Database.host"), getConfig().getInt("Database.port"), getConfig().getString("Database.database"), getConfig().getString("Database.user"), getConfig().getString("Database.password")) : new SQLiteConnection();
        Statements.initialize();
        loadCommands();
        loadConfig();
        loadEvents();
        if (getConfig().getBoolean("Database.mysql") == isEnabled()) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[dReps] Sistema carregado com sucesso!");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §aMySQL");
            Bukkit.getConsoleSender().sendMessage("");
        } else {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§a[dReps] Sistema carregado com sucesso!");
            Bukkit.getConsoleSender().sendMessage("§7Detectado: §8SQLite");
            Bukkit.getConsoleSender().sendMessage("");
        }
        int time = getConfig().getInt("Config.tempo-top") * 60;
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Statements.getTops();
            Bukkit.getConsoleSender().sendMessage("§e[dReps] TOP 10 atualizado!");
        }, 0L, time * 20L);
    }

    @Override
    public void onDisable() {
        saveDefaultConfig();
        try {
            Statements.saveAccounts();
            Statements.connection.close();
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[dReps] Sistema desativado com sucesso.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §aSalvo");
            Bukkit.getConsoleSender().sendMessage("");
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage("");
            Bukkit.getConsoleSender().sendMessage("§c[dReps] Sistema desativado com erro no armazenamento.");
            Bukkit.getConsoleSender().sendMessage("§7Armazenamento: §cErro");
            Bukkit.getConsoleSender().sendMessage("");
        }
    }

    public void loadCommands() {
        getCommand("rep").setExecutor(new RepCommand());
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
    }

    public void loadEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new onPlayerJoinListener(), this);
    }
}
