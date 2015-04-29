package com.github.JohnGuru.happypet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class HappyPet extends JavaPlugin
{
    public static Material wand;
    static final String admin = "animalhelper.admin";
    static final String key = "animalhelper";

    @Override
    public void onEnable()
    {
        saveDefaultConfig();

        String mat = getConfig().getString("wand", "bone");
        wand = Material.matchMaterial(mat);
        if (wand == null)
        {
            getLogger().warning("Wand: material " + mat + " unrecognized");
            wand = Material.BONE; // set default
        }
        getLogger().info("Wand material is " + wand);
        // Register the listener
        getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
    }

    @Override
    public void onDisable()
    {
        // Unregister this plugin's event handlers
        HandlerList.unregisterAll(this);
        saveConfig();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        // Add more commands here
        if (cmd.getName().equalsIgnoreCase("hdowner"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("This command cannot be run from the console.");
                return true;
            }
            if (args.length == 1)
            {
                setOwner((Player) sender, args[0]);
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("hdfree"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage("This command cannot be run from the console.");
            }
            else if (args.length == 0) resetOwner((Player) sender);
            return true;
        }
        return false;
    }


    /*
     * Here is the core that actually identifies and updates the animal
     */
    public final class PlayerInteractListener implements Listener
    {

        @EventHandler
        public void OnPlayerInteract(PlayerInteractEntityEvent ev)
        {
            // Analyze the event
            Entity animal = ev.getRightClicked();
            Player p = ev.getPlayer();
            if (p.getItemInHand().getType() != wand)
                // Not the wand to edit an animal
                return;
            List<MetadataValue> meta = p.getMetadata(key);
            if (!meta.isEmpty())
            {
                String giveto = meta.get(0).asString();
                Plugin plug = meta.get(0).getOwningPlugin();
                reassign(p, animal, giveto, plug);
                p.removeMetadata(key, plug);
            }
            else switch (animal.getType())
            {
                case WOLF:
                    handleAngry(p, animal);
                    handleCart(p, animal);
                    handleTameableHealth(p, animal);
                    break;
                case OCELOT:
                case HORSE:
                    handleCart(p, animal);
                    handleTameableHealth(p, animal);
                    break;
                case CHICKEN:
                case SHEEP:
                case COW:
                case PIG:
                case MUSHROOM_COW:
                    handleCart(p, animal);
                    handleHealth(p, animal);
                    break;
                default:
                    p.sendMessage("This is not a valid animal type. Try again.");
                    break;
            }
            // ev.setCancelled(true);
        }
    }

    /*
     * ***************************************************************************
     * 
     * These functions are called from the command handler before clicking on the animal
     * 
     * ***************************************************************************
     */
    /*
     * Request owner change
     */
    private void setOwner(Player requestor, String newowner)
    {
        if (!requestor.hasPermission(admin))
        {
            requestor.sendMessage("Not allowed (" + admin);
            return;
        }
        OfflinePlayer op = getServer().getOfflinePlayer(newowner);
        if (!op.isOnline() && !op.hasPlayedBefore())
        {
            requestor.sendMessage(newowner + ": " + " no such player");
            return;
        }
        requestor.removeMetadata(key, this);
        requestor.setMetadata(key, new FixedMetadataValue(this, newowner));

        requestor.sendMessage("Now right-click on the animal");
    }

    /*
     * Release a tamed animal to unowned status
     */
    private void resetOwner(Player requestor)
    {
        requestor.setMetadata(key, new FixedMetadataValue(this, ""));
        requestor.sendMessage("Now right-click on the animal");
    }
 
    /*
     * ***************************************************************************
     * 
     * These functions are called from the Listener on a right-click
     * 
     * ***************************************************************************
     */

    private void handleAngry(Player p, Entity ent)
    {
        Wolf dog = (Wolf) ent;
        AnimalTamer owner = dog.getOwner();
        if (owner == null)
        {
            p.sendMessage(ChatColor.RED + "This is a wild dog");
            return;
        }
        p.sendMessage("This dog is owned by " + owner.getName());
        if (p != owner && !p.hasPermission(admin))
        {
            return;
        }
        if (dog.isAngry())
        {
            dog.setAngry(false);
            p.sendMessage(ChatColor.GREEN + "This dog is now happy!");
        }
    }

    private void handleCart(Player p, Entity ent)
    {
        /*
         * If in cart, eject it. We don't care about ownership
    	 */
        if (ent.isInsideVehicle()) ent.leaveVehicle();
    }

    private void handleTameableHealth(Player p, Entity ent)
    {
        // Only valid for animal's owner or an admin
        Tameable T = (Tameable) ent;
        if (T.isTamed())
            if (T.getOwner() == p || p.hasPermission(admin)) handleHealth(p, ent);
            else p.sendMessage("This animal is owned by " + T.getOwner().getName());
    }

    private void handleHealth(Player p, Entity ent)
    {
        // restore health
        Damageable animal = (Damageable) ent;
        int max = (int) animal.getMaxHealth();
        int was = (int) animal.getHealth();
        if (was < max)
        {
            animal.setHealth(max);
            p.sendMessage("Health " + was + " set to " + max);
        }
    }

    private void reassign(Player p, Entity e, String newowner, Plugin context)
    {
        if (e.getType() != EntityType.WOLF && e.getType() != EntityType.OCELOT && e.getType() != EntityType.HORSE)
        {
            p.sendMessage("You can only reassign dogs, cats, and horses.");
            return;
        }
        Tameable animal = (Tameable) e;
        if (newowner.length() > 0)
        {
            // Assigning a new owner
            OfflinePlayer owner = context.getServer().getOfflinePlayer(newowner);
            if (owner != null && (owner.hasPlayedBefore() || owner.isOnline()))
            {
                animal.setOwner(owner);
                p.sendMessage("Animal has been reassigned to " + animal.getOwner().getName());
            }
            else p.sendMessage("Invalid owner: " + newowner);
        }
        else
        {
    		/*
    		 * Freeing a previously tamed animal. The animal's owner can do this
    		 * or a user having animalhelper.admin permission
    		 */
            if (p.hasPermission(admin))
            {
                animal.setOwner(null);
                p.sendMessage("Animal has been released.");
            }
            else if (animal.isTamed()) if (animal.getOwner() == p)
            {
                animal.setOwner(null);
                p.sendMessage("Animal has been released.");
            }
            else p.sendMessage("This animal is owned by " + animal.getOwner().getName());
            else p.sendMessage("This animal is not tamed.");
        }

        return;
    }
}
