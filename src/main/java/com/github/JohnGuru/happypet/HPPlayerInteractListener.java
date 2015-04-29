package com.github.JohnGuru.happypet;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public final class HPPlayerInteractListener implements Listener
{
    private HappyPet happyPet;

    public HPPlayerInteractListener(HappyPet happyPet) { this.happyPet = happyPet; }

    @EventHandler
    public void OnPlayerInteract(PlayerInteractEntityEvent ev)
    {
        // Analyze the event
        Entity animal = ev.getRightClicked();
        Player p = ev.getPlayer();
        if (p.getItemInHand().getType() != HappyPet.cfgWand)
            // Not the wand to edit an animal
            return;
        List<MetadataValue> meta = p.getMetadata(key);
        if (!meta.isEmpty())
        {
            String giveto = meta.get(0).asString();
            Plugin plug = meta.get(0).getOwningPlugin();
            happyPet.reassign(p, animal, giveto, plug);
            p.removeMetadata(key, plug);
        }
        else switch (animal.getType())
        {
            case WOLF:
                happyPet.handleAngry(p, animal);
                happyPet.handleCart(p, animal);
                happyPet.handleTameableHealth(p, animal);
                break;
            case OCELOT:
            case HORSE:
                happyPet.handleCart(p, animal);
                happyPet.handleTameableHealth(p, animal);
                break;
            case CHICKEN:
            case SHEEP:
            case COW:
            case PIG:
            case MUSHROOM_COW:
                happyPet.handleCart(p, animal);
                happyPet.handleHealth(p, animal);
                break;
            default:
                p.sendMessage("This is not a valid animal type. Try again.");
                break;
        }
        // ev.setCancelled(true);
    }
}
