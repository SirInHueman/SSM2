package SSM.Abilities;

import SSM.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class RopedArrow extends Ability {

    private Arrow arrow;

    public RopedArrow() {
        super();
        this.name = "Roped Arrow";
        this.cooldownTime = 8;
        this.leftClickActivate = true;
    }

    public void activate() {
        arrow = owner.launchProjectile(Arrow.class);
        arrow.setCustomName("Roped Arrow");
        arrow.setDamage(4.0);
        arrow.setVelocity(owner.getLocation().getDirection().multiply(1.8D));
    }

    @EventHandler
    public void pullToArrow(ProjectileHitEvent e) {
        if (e.getEntity() != arrow) {
            return;
        }
        Vector p = owner.getLocation().toVector();
        Vector a = arrow.getLocation().toVector();
        Vector pre = a.subtract(p);
        Vector velocity = pre.normalize().multiply(1.8);

        owner.setVelocity(new Vector(velocity.getX(), 1.2, velocity.getZ()));
        arrow.remove();
    }

}
