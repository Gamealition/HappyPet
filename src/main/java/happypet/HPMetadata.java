package happypet;

import org.bukkit.entity.Player;

/**
 * Static class of metadata keys
 */
public class HPMetadata
{
    public static final String CALM_TARGET  = "happypetCalmTarget";
    public static final String FREE_TARGET  = "happypetFreeTarget";
    public static final String OWNER_TARGET = "happypetOwnerTarget";

    /**
     * Clears all HappyPet metadata from a player. Helps avoid conflicts if multiple
     * metadata-reliant commands are used (e.g. if player does /owner, doesn't punch a
     * pet, then does /free)
     * @param player Player to clear metadata from
     */
    public static void clearFrom(Player player, HappyPet happyPet)
    {
        player.removeMetadata(HPMetadata.CALM_TARGET, happyPet);
        player.removeMetadata(HPMetadata.FREE_TARGET, happyPet);
        player.removeMetadata(HPMetadata.OWNER_TARGET, happyPet);
    }
}
