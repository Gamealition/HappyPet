package com.github.JohnGuru.happypet;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public final class HPListeners implements Listener
{
    private final HappyPet happyPet;

    public HPListeners(HappyPet happyPet) { this.happyPet = happyPet; }

    @EventHandler
    public void onPlayerHitEntity(EntityDamageByEntityEvent ev)
    {
        if (   !(ev.getDamager() instanceof Player)
            || !(ev.getEntity()  instanceof Tameable))
            return;

        Animals animal = (Animals) ev.getEntity();
        Player  player = (Player)  ev.getDamager();

        if (    player.getItemInHand().getType() != HappyPet.cfgWand
            || !player.hasPermission(HPPermissions.WAND) )
            return;

        happyPet.handlers.showPetInfo(animal, player);
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent ev)
    {
//        Entity animal = ev.getRightClicked();
//        Player player = ev.getPlayer();
//
//
//
//        List<MetadataValue> meta = player.getMetadata(key);
//        if (!meta.isEmpty())
//        {
//            String giveto = meta.get(0).asString();
//            Plugin plug = meta.get(0).getOwningPlugin();
//            happyPet.reassign(player, animal, giveto, plug);
//            player.removeMetadata(key, plug);
//        }
//        else switch (animal.getType())
//        {
//            case WOLF:
//                happyPet.handleAngry(player, animal);
//                break;
//            case OCELOT:
//            case HORSE:
//                break;
//            default:
//                player.sendMessage("This is not a valid animal type. Try again.");
//                break;
//        }
        // ev.setCancelled(true);
    }
}
