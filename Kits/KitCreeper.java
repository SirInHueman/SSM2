package SSM.Kits;

import SSM.*;
import SSM.Abilities.*;
import SSM.Attributes.DoubleJumps.GenericDoubleJump;
import SSM.Attributes.Regeneration;
import me.libraryaddict.disguise.LibsDisguises;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import me.libraryaddict.disguise.LibsDisguises;

public class KitCreeper extends Kit {

    public KitCreeper() {
        super();
        this.damage = 6;
        this.speed = 0.21f;
        this.regeneration = 0.4;
        this.knockback = 0;
        this.disguise = DisguiseType.CREEPER;
        this.name = "Creeper";
    }

    public void equipKit(Player player) {
        super.equipKit(player);
        setArmor(Material.IRON_BOOTS, 0);
        setArmor(Material.LEATHER_LEGGINGS, 1);
        setArmor(Material.LEATHER_CHESTPLATE, 2);
        setArmor(Material.LEATHER_HELMET, 3);

        setItem(Material.IRON_AXE, 0, new SulphurBomb());
        setItem(Material.IRON_SHOVEL, 1, new Explode());

        addAttribute(new Regeneration(regeneration, 1));
        addAttribute(new GenericDoubleJump(0.61, 0.8, 1));
    }
}
