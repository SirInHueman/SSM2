package SSM.Kits;

import SSM.Abilities.InkShotgun;
import SSM.Abilities.SpinWeb;
import SSM.Attributes.DoubleJumps.GenericDoubleJump;
import SSM.Attributes.Regeneration;
import SSM.Kit;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import SSM.Ability;

public class KitSquid extends Kit {


    public KitSquid() {
        super();

        this.damage = 6;
        this.speed = 0.24f;
        this.regeneration = 0.25;
        this.knockback = 0;
        this.name = "SkySquid";
        this.disguise = DisguiseType.SQUID;
    }

    public void equipKit(Player player) {
        super.equipKit(player);

        setArmor(Material.CHAINMAIL_BOOTS, 0);
        setArmor(Material.CHAINMAIL_LEGGINGS, 1);
        setArmor(Material.CHAINMAIL_CHESTPLATE, 2);

        setItem(Material.IRON_AXE, 0, new InkShotgun());
        setItem(Material.IRON_SWORD, 1);
        setItem(Material.IRON_SHOVEL, 2);

        addAttribute(new Regeneration(regeneration, 1));
        addAttribute(new GenericDoubleJump(0.61, 0.8, 1));
    }

}
