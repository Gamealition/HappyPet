package happypet.util;

import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static happypet.HappyPet.LOGGER;

/** Static utility class for processing some config elements */
public class ConfigUtils
{
    /** Gets the requested material by path, or null if invalid */
    public static Material getMaterial(Configuration config, String key)
    {
        String   materialName = config.getString(key);
        Material material     = Material.matchMaterial(materialName);

        if (material == null)
            LOGGER.warn("Unrecognized material for '%s': %s", key, materialName);

        return material;
    }

    /** Gets the requested recipe by path, or null if invalid */
    public static ShapedRecipe getRecipe(Configuration config, ItemStack target, String key)
    {
        try
        {
            // Consider a non-section as a disabled recipe
            if ( !config.isConfigurationSection(key) )
                return null;

            ConfigurationSection section = config.getConfigurationSection(key);

            if ( !section.isList("grid") || !section.isConfigurationSection("materials") )
                return null;

            ShapedRecipe recipe = new ShapedRecipe(target);
            String[]     shape  = section
                .getStringList("grid")
                .toArray(new String[3]);

            recipe.shape(shape);

            section
                .getConfigurationSection("materials")
                .getValues(false)
                .forEach((k, v) -> {
                    Material material = Material.matchMaterial((String) v);

                    recipe.setIngredient(k.charAt(0), material);
                });

            return null;
        }
        catch (Exception e)
        {
            LOGGER.warn("Could not parse recipe '%s': %s", key, e);
            return null;
        }
    }
}
