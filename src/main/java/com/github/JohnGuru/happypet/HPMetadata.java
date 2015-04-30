package com.github.JohnGuru.happypet;

import org.bukkit.entity.Player;

/**
 * Static class of metadata keys
 */
public class HPMetadata
{
    public static final String OWNER_TARGET = "happypetOwnerTarget";

    /**
     * Clears all HappyPet metadata from a player. Helps avoid conflicts if multiple
     * metadata-reliant commands are used.
     * @param player Player to clear metadata from
     */
    public static void clearFrom(Player player, HappyPet happyPet)
    {
        player.removeMetadata(HPMetadata.OWNER_TARGET, happyPet);
    }
}
