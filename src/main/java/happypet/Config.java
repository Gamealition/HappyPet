package happypet;

import happypet.util.ConfigUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static happypet.HappyPet.LOGGER;
import static happypet.HappyPet.PLUGIN;

/** Container and manager class for plugin's configuration values */
public class Config
{
    private static final ItemStack ITEM_SADDLE  = new ItemStack(Material.SADDLE, 1);
    private static final ItemStack ITEM_NAMETAG = new ItemStack(Material.NAME_TAG, 1);

    private FileConfiguration config;

    public final Pets pets = new Pets();
    public class Pets
    {
        public boolean ocelots;
        public boolean wolves;
        public boolean horses;
        public boolean llamas;
    }

    public final Ownership ownership = new Ownership();
    public class Ownership
    {
        public int     maximum;
        public boolean ignoreCreative;
        public boolean nameOnClaim;
        public boolean autoUntame;
        public boolean claimOnTame;
        public boolean claimOnName;
        public boolean claimOnRide;
        public boolean claimOnSaddle;
    }

    public final Tools tools = new Tools();
    public class Tools
    {
        public Material wand;
    }

    public final Commands commands = new Commands();
    public class Commands
    {
        public boolean summonAcrossWorlds = false;
    }

    public final Recipes recipes = new Recipes();
    public class Recipes
    {
        public ShapedRecipe saddle;
        public ShapedRecipe nameTag;
    }

    public final Protection protection = new Protection();
    public class Protection
    {
        public boolean playersUnclaimed;
        public boolean playersClaimed;
        public boolean mobsUnclaimed;
        public boolean mobsClaimed;
        public boolean suffocationUnclaimed;
        public boolean suffocationClaimed;
    }

    Config()
    {
        load();
        populate();
        validate();
    }

    private void load()
    {
        PLUGIN.saveDefaultConfig();
        PLUGIN.reloadConfig();

        config = PLUGIN.getConfig();
        LOGGER.fine("Loaded config from disk");

        // It would be better to track config version and do migrations, but after trying I found
        // that comments are not preserved on config save. This is not what we want. Since the old
        // config had only two options, it's easier just to halt plugin load.
        if ( config.isSet("ignoreHorses") || config.isSet("wand") )
            throw new RuntimeException("Old config file found; please delete it and reload the " +
                "plugin to generate a new one");
    }

    private void populate()
    {
        pets.ocelots = config.getBoolean("pets.ocelots");
        pets.wolves  = config.getBoolean("pets.wolves");
        pets.horses  = config.getBoolean("pets.horses");
        pets.llamas  = config.getBoolean("pets.llamas");

        ownership.maximum        = config.getInt("ownership.maximum");
        ownership.ignoreCreative = config.getBoolean("ownership.ignoreCreative");
        ownership.nameOnClaim    = config.getBoolean("ownership.nameOnClaim");
        ownership.autoUntame     = config.getBoolean("ownership.autoUntame");
        ownership.claimOnTame    = config.getBoolean("ownership.claimOnTame");
        ownership.claimOnName    = config.getBoolean("ownership.claimOnName");
        ownership.claimOnRide    = config.getBoolean("ownership.claimOnRide");
        ownership.claimOnSaddle  = config.getBoolean("ownership.claimOnSaddle");

        tools.wand = ConfigUtils.getMaterial(config, "tools.wand");

        commands.summonAcrossWorlds = config.getBoolean("commands.summonAcrossWorlds");

        recipes.saddle  = ConfigUtils.getRecipe(config, ITEM_SADDLE, "recipes.saddle");
        recipes.nameTag = ConfigUtils.getRecipe(config, ITEM_NAMETAG, "recipes.nameTag");

        protection.playersUnclaimed     = config.getBoolean("protection.players.unclaimed");
        protection.playersClaimed       = config.getBoolean("protection.players.claimed");
        protection.mobsUnclaimed        = config.getBoolean("protection.mobs.unclaimed");
        protection.mobsClaimed          = config.getBoolean("protection.mobs.claimed");
        protection.suffocationUnclaimed = config.getBoolean("protection.suffocation.claimed");
        protection.suffocationClaimed   = config.getBoolean("protection.suffocation.claimed");
        LOGGER.fine("Populated all config classes from loaded config");
    }

    private void validate()
    {
        if (tools.wand == null)
        {
            tools.wand = Material.BONE;
            LOGGER.warn("Using default wand material of BONE");
        }

        LOGGER.fine("Validated all loaded config");
    }
}
