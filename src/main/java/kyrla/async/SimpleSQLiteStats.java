package kyrla.async;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class SimpleSQLiteStats extends JavaPlugin {

    private Database database;

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        database = new Database("stats.db");

        try {
            database.сonnect(); // Открываем дверь
            database.initialize(); // Строим полки (таблицы)
            getLogger().info("✅ База данных успешно подключена!");
            

        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("❌ Ошибка подключения к БД! Плагин выключается, чтобы не сломать сервер.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new StatsListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(this), this);
        getCommand("stats").setExecutor(new StatsCommand(this));
    }

    @Override
    public void onDisable() {
        
        try {
            if (database != null) {
                database.closeConnection(); 
                getLogger().info("🔒 Соединение с БД закрыто.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Database getDatabase() {
        return database;
    }
}
