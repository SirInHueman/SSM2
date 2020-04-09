package SSM;

import SSM.Kits.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EntityProjectile extends BukkitRunnable {

    protected Plugin plugin;
    protected Player firer;
    protected String name;
    protected Entity projectile;
    protected Location overridePosition;
    protected boolean fireOpposite = false;
    protected boolean clearOnFinish = true;
    protected boolean fired;
    protected boolean upwardKnockbackSet;
    protected double[] data;

    public EntityProjectile(Plugin plugin, Player firer, String name, Entity projectile) {
        this.plugin = plugin;
        this.firer = firer;
        this.name = name;
        this.projectile = projectile;
        this.data = new double[]{0, 0, 0, 0, 0, 0};
    }

    public double getRandomVariation() {
        double variation = getVariation();
        double randomAngle = Math.random() * variation / 2;
        if (new Random().nextBoolean()) {
            randomAngle *= -1;
        }
        return randomAngle * Math.PI / 180;
    }

    public void launchProjectile() {
        if (fired) {
            return;
        }
        Location firePosition = getOverridePosition();
        if (firePosition == null) {
            firePosition = firer.getEyeLocation();
        }
        projectile.teleport(firePosition);
        projectile.setCustomName(name);
        if (projectile instanceof Item) {
            Item item = (Item) projectile;
            item.setPickupDelay(1000000);
        }
        double magnitude = getSpeed();
        Vector direction = firer.getLocation().getDirection();
        if (getFireOpposite()) {
            direction.multiply(-1);
        }
        direction.rotateAroundX(getRandomVariation());
        direction.rotateAroundY(getRandomVariation());
        direction.rotateAroundZ(getRandomVariation());
        projectile.setVelocity(direction.multiply(magnitude));
        this.runTaskTimer(plugin, 0L, 1L);
        fired = true;
    }

    @Override
    public void run() {
        if (projectile.isDead() || !projectile.isDead() && projectile.isOnGround()) {
            onHit(null);
            this.cancel();
            return;
        }
        double hitboxRange = getHitboxSize();
        List<Entity> canHit = projectile.getNearbyEntities(hitboxRange, hitboxRange, hitboxRange);
        canHit.remove(projectile);
        canHit.remove(firer);
        if (canHit.size() <= 0) {
            return;
        }
        for (Entity entity : canHit) {
            if (!(entity instanceof LivingEntity)) {
                continue;
            }
            LivingEntity target = (LivingEntity) canHit.get(0);
            onHit(target);
            this.cancel();
            break;
        }
    }

    public boolean onHit(LivingEntity target) {
        boolean success = target != null;
        if (success) {
            double damage = getDamage();
            target.damage(damage);
            double knockback = getKnockback();
            double upwardKnockback = getUpwardKnockback();
            Vector velocity = projectile.getVelocity();
            if (upwardKnockbackSet) {
                velocity.setY(0);
                velocity = velocity.normalize().multiply(knockback);
                velocity.setY(upwardKnockback);
                target.setVelocity(new Vector(velocity.getX(), upwardKnockback, velocity.getZ()));
            } else {
                velocity = velocity.normalize().multiply(knockback);
                target.setVelocity(velocity);
            }
        }
        clearProjectile();
        return success;
    }

    public boolean clearProjectile() {
        if (getClearOnFinish()) {
            projectile.remove();
            return true;
        }
        return false;
    }

    public void setOverridePosition(Location overridePosition) {
        this.overridePosition = overridePosition;
    }

    public Location getOverridePosition() {
        return overridePosition;
    }


    public void setFireOpposite(boolean fireOpposite) {
        this.fireOpposite = fireOpposite;
    }

    public boolean getFireOpposite() {
        return fireOpposite;
    }

    public void setClearOnFinish(boolean clearOnFinish) {
        this.clearOnFinish = clearOnFinish;
    }

    public boolean getClearOnFinish() {
        return clearOnFinish;
    }

    public void setDamage(double damage) {
        data[0] = damage;
    }

    public double getDamage() {
        return data[0];
    }

    public void setSpeed(double speed) {
        data[1] = speed;
    }

    public double getSpeed() {
        return data[1];
    }

    public void setKnockback(double knockback) {
        data[2] = knockback;
    }

    public double getKnockback() {
        return data[2];
    }

    public void setUpwardKnockback(double upwardKnockback) {
        data[3] = upwardKnockback;
        upwardKnockbackSet = true;
    }

    public double getUpwardKnockback() {
        return data[3];
    }

    public void setHitboxSize(double hitboxRange) {
        data[4] = hitboxRange;
    }

    public double getHitboxSize() {
        return data[4];
    }

    public void setVariation(double variation) {
        data[5] = variation;
    }

    public double getVariation() {
        return data[5];
    }

}












