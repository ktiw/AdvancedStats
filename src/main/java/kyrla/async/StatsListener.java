package kyrla.async;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent; // ИМПОРТИРУЙ ЭТО!
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.sql.SQLException;

public class StatsListener implements Listener {

    private final SimpleSQLiteStats plugin;

    public StatsListener(SimpleSQLiteStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String uuid = player.getUniqueId().toString(); // запись игрока

        System.out.println("DEBUG: Игрок зашел: " + player.getName() + " c UUID: " + uuid);

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    plugin.getDatabase().createPlayerProfile(uuid);
                    System.out.println("DEBUG: Профиль проверен/создан в БД.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();

        if (killer == null) {
            return;
        }

        String killerUUID = killer.getUniqueId().toString();

        killer.sendMessage("⚔️ Моб повержен! Добавляем +1 в базу...");

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    plugin.getDatabase().addKill(killerUUID);
                    System.out.println("DEBUG: +1 добавлено в БД для " + killer.getName());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("DEBUG: Ошибка SQL при добавлении килла!");
                }
            }
        }.runTaskAsynchronously(plugin);
    }


}