package kyrla.async;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private final String url;
    private Connection connection;

    public Database(String name) {
        this.url = "jdbc:sqlite:plugins/Async/" + name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void сonnect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(this.url);
        }
    }


    // Добавить в Database.java
public void closeConnection() throws SQLException {
    if (connection != null && !connection.isClosed()) {
        connection.close();
    }
}

    // Метод для создания таблицы.
    public void initialize() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS player_stats (" +
                "uuid TEXT PRIMARY KEY, " +
                "kills INTEGER, " +
                "deaths INTEGER" +
                ");";


        try (java.sql.Statement statement = connection.createStatement()) {
            statement.execute(sql); // Отправляем запрос в базу
        }
    }

    // Метод: Создать запись об игроке
    public void createPlayerProfile(String uuid) throws SQLException {

        String sql = "INSERT OR IGNORE INTO player_stats (uuid, kills, deaths) VALUES (?, 0, 0);";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);

            stmt.executeUpdate();
        }
    }

    // Метод: Обновить количество убийств у игрока
    public void updateKills(String uuid, int newKills) throws SQLException {
        String sql = "UPDATE player_stats SET kills = ? WHERE uuid = ?;";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, newKills);

            stmt.setString(2, uuid);

            stmt.executeUpdate();
        }
    }

    public void addKill(String uuid) throws SQLException {
        String sql = "UPDATE player_stats SET kills = kills + 1 WHERE uuid = ?;";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        }
    }

    // Метод: Получить количество убийств
    public int getKills(String uuid) throws SQLException {
        String sql = "SELECT kills FROM player_stats WHERE uuid = ?;";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);

            java.sql.ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("kills");
            } else {
                return 0;
            }
        }
    }

    // Метод: Добавить +1 к смертям
    public void addDeath(String uuid) throws SQLException {
        String sql = "UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?;";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        }
    }

    // Метод: Узнать количество смертей (для команды /stats)
    public int getDeaths(String uuid) throws SQLException {
        String sql = "SELECT deaths FROM player_stats WHERE uuid = ?;";

        try (java.sql.PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid);
            java.sql.ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("deaths");
            } else {
                return 0;
            }
        }
    }
}
