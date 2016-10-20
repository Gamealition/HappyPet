package org.bukkit.entity;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.LlamaInventory;

/** Mock class to anticipate support of llamae in 1.11 */
public interface Llama extends Animals, Vehicle, InventoryHolder, Tameable
{
    /** Represents the base color that the llama has. */
    public enum Color
    {
        /** White */
        WHITE,

        /** Light gray */
        LIGHT_GRAY,

        /** Sandy */
        SANDY,

        /** Brown */
        BROWN;
    }

    /**
     * Gets the llama's color.
     *
     * @return a {@link Color} representing the llama's color
     */
    public Color getColor();

    /**
     * Sets the llama's color.
     *
     * @param color a {@link Color} for this llama
     */
    public void setColor(Color color);

    /**
     * Gets whether the llama has a chest equipped.
     *
     * @return true if the llama has chest storage
     */
    public boolean isCarryingChest();

    /**
     * Sets whether the llama has a chest equipped.
     * Removing a chest will also clear the chest's inventory.
     *
     * @param chest true if the llama should have a chest
     */
    public void setCarryingChest(boolean chest);

    /**
     * Gets the domestication level of this llama.
     * <p>
     * A higher domestication level indicates that the llama is closer to becoming tame. As the
     * domestication level gets closer to the max domestication level, the chance of the llama
     * becoming tame increases.
     *
     * @return domestication level
     */
    public int getDomestication();

    /**
     * Sets the domestication level of this llama.
     * <p>
     * Setting the domestication level to a high value will increase the llama's chances of
     * becoming tame.
     * <p>
     * Domestication level must be greater than zero and no greater than the max domestication level
     * of the llama, determined with {@link #getMaxDomestication()}
     *
     * @param level domestication level
     */
    public void setDomestication(int level);

    /**
     * Gets the maximum domestication level of this llama.
     * <p>
     * The higher this level is, the longer it will likely take for the llama to be tamed.
     *
     * @return the max domestication level
     */
    public int getMaxDomestication();

    /**
     * Gets the carrying strength of this llama.
     * <p>
     * Carrying strength defines how much a chested llama can carry.
     * The total number of slots is strength * 3.
     *
     * @return the llama's carrying strength
     */
    public int getStrength();

    /**
     * Sets the carrying strength of this llama.
     * <p>
     * A higher carrying strength increases how much a chested llama can carry.
     * The total number of slots is strength * 3.
     * You cannot set a carrying strength to a value below 1 or above 5.
     *
     * @param strength carrying strength for this llama
     */
    public void setStrength(int strength);

    @Override
    public LlamaInventory getInventory();
}
