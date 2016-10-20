package org.bukkit.inventory;

/** Mock class to anticipate support of llamae in 1.11 */
public interface LlamaInventory extends Inventory
{
    /**
     * Gets the item in the llama's carpet slot.
     *
     * @return the carpet item
     */
    ItemStack getCarpet();

    /**
     * Sets the item in the llama's carpet slot.
     *
     * @param stack the new item
     */
    void setCarpet(ItemStack stack);
}
