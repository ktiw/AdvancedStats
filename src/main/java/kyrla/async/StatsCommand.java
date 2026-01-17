package kyrla.async;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class StatsCommand implements CommandExecutor {

    private final SimpleSQLiteStats plugin;

    public StatsCommand(SimpleSQLiteStats plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        player.sendMessage(ChatColor.YELLOW + "Загружаю статистику...");

        player.sendMessage(ChatColor.YELLOW + "Загружаю статистику...");

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    int kills = plugin.getDatabase().getKills(uuid);
                    int deaths = plugin.getDatabase().getDeaths(uuid);

                    double kdRatio = 0.0;
                    if (deaths > 0) {
                        kdRatio = (double) kills / deaths;
                    } else {
                        kdRatio = kills;
                    }

                    String kdFormatted = String.format("%.2f", kdRatio);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.sendMessage(ChatColor.DARK_GREEN + "========================");
                            player.sendMessage(ChatColor.GREEN + " 👤 Статистика " + player.getName());
                            player.sendMessage(ChatColor.WHITE + " ⚔️ Убийств: " + ChatColor.GREEN + kills);
                            player.sendMessage(ChatColor.WHITE + " ☠️ Смертей: " + ChatColor.RED + deaths);
                            player.sendMessage(ChatColor.WHITE + " 📊 K/D Ratio: " + ChatColor.GOLD + kdFormatted);
                            player.sendMessage(ChatColor.DARK_GREEN + "========================");
                        }
                    }.runTask(plugin);

                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.RED + "Ошибка получения данных!");
                }
            }
        }.runTaskAsynchronously(plugin);

        return true;
    }
}