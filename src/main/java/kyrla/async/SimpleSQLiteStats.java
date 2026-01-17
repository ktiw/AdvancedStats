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
            database.сonnect(); // Открываем файл
            database.initialize(); // Создаем таблицу
            getLogger().info(" База данных успешно подключена!");
        } catch (SQLException e) {
            e.printStackTrace();
            getLogger().severe("Ошибка подключения к БД! Выключаю плагин...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            database.сonnect();
            database.initialize();
            getLogger().info(" База данных успешно подключена!");

            database.createPlayerProfile("test-uuid-123");
            System.out.println("👤 Тестовый игрок отправлен в базу!");

            database.updateKills("test-uuid-123", 10);
            System.out.println("Убийства обновлены!");
        } catch (SQLException e) {
        }

        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new StatsListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathEvent(this), this);
        getCommand("stats").setExecutor(new StatsCommand(this));


    }
    public Database getDatabase() {
        return database;
    }
}

