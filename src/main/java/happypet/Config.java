package happypet;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static happypet.HappyPet.*;

/** Container and manager class for plugin's configuration values */
public class Config
{
    private static final ItemStack ITEM_SADDLE  = new ItemStack(Material.SADDLE, 1);
    private static final ItemStack ITEM_NAMETAG = new ItemStack(Material.NAME_TAG, 1);

    private Configuration config;
    private short         version;

    public final Pets pets = new Pets();
    public class Pets
    {
        public boolean ocelots = true;
        public boolean wolves  = true;
        public boolean horses  = true;
        public boolean llamas  = true;
    }

    public final Ownership ownership = new Ownership();
    public class Ownership
    {
        public int     maximum        = -1;
        public boolean ignoreCreative = true;
        public boolean nameOnClaim    = true;
        public boolean autoUntame     = true;
        public boolean claimOnTame    = true;
        public boolean claimOnName    = true;
        public boolean claimOnRide    = true;
        public boolean claimOnSaddle  = true;
    }

    public final Tools tools = new Tools();
    public class Tools
    {
        public Material wand = Material.BONE;
    }

    public final Commands commands = new Commands();
    public class Commands
    {
        public boolean summonAcrossWorlds = false;
    }

    public final Recipes recipes = new Recipes();
    public class Recipes
    {
        public ShapedRecipe saddle = new ShapedRecipe(ITEM_SADDLE)
            .setIngredient('L', Material.LEATHER)
            .setIngredient('I', Material.IRON_INGOT)
            .setIngredient('x', Material.AIR)
            .shape("LLL", "LIL", "IxI");

        public ShapedRecipe nameTag = new ShapedRecipe(ITEM_NAMETAG)
            .setIngredient('S', Material.STRING)
            .setIngredient('P', Material.PAPER)
            .setIngredient('x', Material.AIR)
            .shape("xxS", "xPx", "Pxx");
    }

    public final Protection protection = new Protection();
    public class Protection
    {
        public boolean playersUnclaimed     = false;
        public boolean playersClaimed       = true;
        public boolean mobsUnclaimed        = false;
        public boolean mobsClaimed          = false;
        public boolean suffocationUnclaimed = false;
        public boolean suffocationClaimed   = true;
    }

    Config()
    {
        populate();
        validate();
    }

    private void populate()
    {
        PLUGIN.saveDefaultConfig();
        PLUGIN.reloadConfig();

        config = PLUGIN.getConfig();
    }

    private void validate()
    {
        
    }
}
