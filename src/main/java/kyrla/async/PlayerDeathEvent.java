package kyrla.async;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class PlayerDeathEvent implements Listener {

    private final SimpleSQLiteStats plugin;

    public PlayerDeathEvent(SimpleSQLiteStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDie(org.bukkit.event.entity.PlayerDeathEvent e) {
        Player victim = e.getEntity();
        String uuid = victim.getUniqueId().toString();

        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    plugin.getDatabase().addDeath(uuid);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
}
