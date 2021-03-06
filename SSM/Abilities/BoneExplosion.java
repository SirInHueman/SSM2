package SSM.Abilities;

import SSM.Ability;
import SSM.EntityProjectile;
import SSM.GameManagers.OwnerEvents.OwnerRightClickEvent;
import SSM.Utilities.DamageUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;

public class BoneExplosion extends Ability implements OwnerRightClickEvent {

    private int boneAmount = 40;
    private double range = 5;
    private double baseDamage = 6;

    public BoneExplosion() {
        super();
        this.name = "Bone Explosion";
        this.cooldownTime = 8;
    }

    public void onOwnerRightClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        checkAndActivate(player);
    }

    public void activate() {
        Location loc = owner.getLocation();
        owner.getWorld().playSound(owner.getLocation(), Sound.ENTITY_SKELETON_DEATH, 10L, 1L);
        ItemStack bone = new ItemStack(Material.BONE, 1);
        for (int i = 0; i < boneAmount; i++) {
            ItemMeta boneMeta = bone.getItemMeta();
            boneMeta.setDisplayName("bone" + i);
            bone.setItemMeta(boneMeta);
            Item firing = owner.getWorld().dropItem(owner.getEyeLocation(), bone);
            BoneProjectile projectile = new BoneProjectile(plugin, owner, name, firing);
            projectile.setOverridePosition(owner.getEyeLocation().subtract(0, -1, 0));
            projectile.launchProjectile();
        }
        List<Entity> canHit = owner.getNearbyEntities(range, range, range);
        canHit.remove(owner);

        for (Entity entity : canHit) {
            if ((entity instanceof LivingEntity)) {
                double dist = loc.distance(entity.getLocation());
                DamageUtil.dealDamage(owner, (LivingEntity) entity, (range - dist) * (baseDamage / range), true, false);
                Vector target = entity.getLocation().toVector();
                Vector player = owner.getLocation().toVector();
                Vector pre = target.subtract(player);
                Vector velocity = pre.normalize().multiply(1.35);
                entity.setVelocity(new Vector(velocity.getX(), 0.4, velocity.getZ()));
            }
        }

    }

    class BoneProjectile extends EntityProjectile {

        public BoneProjectile(Plugin plugin, Player firer, String name, Entity projectile) {
            super(plugin, firer, name, projectile);
            this.setDamage(0.0);
            this.setSpeed(0.5 + Math.random() * 0.3);
            this.setKnockback(0.0);
            this.setUpwardKnockback(0.0);
            this.setHitboxSize(0.0);
            this.setVariation(360);
            this.setFireOpposite(false);
        }
    }
}