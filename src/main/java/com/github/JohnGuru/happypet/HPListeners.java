package com.github.JohnGuru.happypet;

import org.bukkit.entity.*;
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
        if      ( !(ev.getRightClicked() instanceof Tameable) )
            return;
        else if ( ev.getRightClicked() instanceof Horse && HappyPet.cfgIgnoreHorses )
            return;

        Animals animal = (Animals) ev.getRightClicked();
        Player  player = ev.getPlayer();

        if ( player.hasMetadata(HPMetadata.OWNER_TARGET) )
            onPlayerOwner(player, animal);

        HPMetadata.clearFrom(player, happyPet);

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
//         ev.setCancelled(true);
    }

    private void onPlayerOwner(Player player, Animals animal)
    {
        Tameable    tameable = (Tameable) animal;
        AnimalTamer tamer    = tameable.getOwner();

        if ( tamer == null || tamer != player )
        if ( !player.hasPermission(HPPermissions.OWNER_ANY) )
        {
            player.sendMessage("[HappyPet] You can only give away your own pets");
            return;
        }

        Player target = (Player) player
            .getMetadata(HPMetadata.OWNER_TARGET)
            .get(0).value();

        tameable.setOwner(target);

        String msgFrom = String.format( "[HappyPet] You have given that pet %s to %s",
            animal.getName(), target.getName() );
        String msgTo   = String.format( "[HappyPet] %s has given you a pet %s",
            player.getName(), animal.getName() );

        player.sendMessage(msgFrom);
        target.sendMessage(msgTo);
    }
}
