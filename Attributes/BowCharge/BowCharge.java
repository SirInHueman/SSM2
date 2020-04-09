package SSM.Attributes.BowCharge;

import SSM.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

public abstract class BowCharge extends Attribute {

    double delay;
    double rate;
    int maxCharge;

    int charge = 0;

    public BowCharge(double delay, double rate, int maxCharge) {
        super();
        this.name = "Bow Charge";
        this.delay = delay;
        this.rate = rate;
        this.maxCharge = maxCharge;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void drawBow(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (player != owner) {
            return;
        }
        if (player.getInventory().getItemInMainHand().getType() != Material.BOW) {
            return;
        }
        if (!player.getInventory().contains(Material.ARROW)) {
            return;
        }
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            finishFiring();
            task = Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    if (charge < maxCharge) {
                        charge++;
                        incrementedCharge();
                    }
                }
            }, (long) (delay * 20), (long) (rate * 20));
        }
    }

    @EventHandler
    public void checkFiredBow(EntityShootBowEvent e) {
        Entity fired = e.getProjectile();
        if (!(fired instanceof Arrow)) {
            return;
        }
        Arrow arrow = (Arrow) fired;
        ProjectileSource source = arrow.getShooter();
        if (!(source instanceof Player)) {
            return;
        }
        Player player = (Player) source;
        if (player != owner) {
            return;
        }
        firedBow(arrow);
        finishFiring();
    }

    // make it run last so the check for firing the bow works first (if it does run)
    @EventHandler(priority = EventPriority.HIGHEST)
    public void switchedWeapon(PlayerItemHeldEvent e) {
        finishFiring();
    }

    public void incrementedCharge() {
        owner.setExp(Math.min(0.9999F, (float) charge / maxCharge));
        owner.playSound(owner.getLocation(), Sound.UI_BUTTON_CLICK, 1.0F, 1.0F + 0.1F * charge);
    }

    public abstract void firedBow(Arrow p);

    public void finishFiring() {
        cancelTask();
        owner.setExp(0);
        charge = 0;
    }

}

