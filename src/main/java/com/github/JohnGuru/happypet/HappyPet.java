package com.github.JohnGuru.happypet;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class HappyPet extends JavaPlugin
{
    public static Material cfgWand;
    public static boolean  cfgIgnoreHorses;

    HPPlayerInteractListener listener;

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        reloadConfig();

        String mat = getConfig().getString("wand", "bone");
        cfgWand    = Material.matchMaterial(mat);
        if (cfgWand == null)
        {
            getLogger().warning("Wand material " + mat + " unrecognized");
            cfgWand = Material.BONE; // set default
        }
        getLogger().info("Wand material is " + cfgWand);

        cfgIgnoreHorses = getConfig().getBoolean("ignoreHorses", false);

        listener = new HPPlayerInteractListener(this);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        listener = null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (args.length < 1)
            return false;

        switch ( args[0].toLowerCase() )
        {
            case "owner":
                onCommandOwner(sender, args); break;
            case "free":
                onCommandFree(sender, args); break;
            case "wand":
                onCommandWand(sender, args); break;
            case "reload":
                onCommandReload(sender, args); break;
        }

        return true;
    }

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

    private void resetOwner(Player requestor)
    {
        requestor.setMetadata(key, new FixedMetadataValue(this, ""));
        requestor.sendMessage("Now right-click on the animal");
    }

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
