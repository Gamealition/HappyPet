package com.github.JohnGuru.happypet;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class HappyPet extends JavaPlugin
{
    public static Material cfgWand;
    public static boolean  cfgIgnoreHorses;

    HPListeners listeners;
    HPHandlers  handlers;

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
            cfgWand = Material.BONE;
        }
        getLogger().info("Wand material is " + cfgWand);

        cfgIgnoreHorses = getConfig().getBoolean("ignoreHorses", false);

        listeners = new HPListeners(this);
        handlers  = new HPHandlers(this);
        getServer().getPluginManager().registerEvents(listeners, this);
    }

    @Override
    public void onDisable()
    {
        HandlerList.unregisterAll(this);
        listeners = null;
        handlers  = null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
    {
        if (args.length < 1)
            return false;

        switch ( args[0].toLowerCase() )
        {
            case "owner":
                return onCommandOwner(sender, args);
            case "calm":
                return onCommandCalm(sender, args);
            case "free":
                return onCommandFree(sender, args);
            case "wand":
                return onCommandWand(sender, args);
            case "reload":
                return onCommandReload(sender, args);
            default:
                return false;
        }
    }

    private boolean onCommandOwner(CommandSender sender, String[] args)
    {
        if (args.length < 2)
            return false;

        if (   !handlers.requirePlayer(sender)
            || !handlers.requirePermission(sender, HPPermissions.OWNER) )
            return true;

        Player        source       = (Player) sender;
        String        target       = args[1];
        OfflinePlayer targetPlayer = handlers.getPreviousPlayer(target);

        if (targetPlayer == null)
        {
            sender.sendMessage("[HappyPet] That player has not played on this server");
            return true;
        }
        else
            target = targetPlayer.getName();

        // Clear metadata to avoid conflict with previous commands
        // (e.g. if player does /owner, doesn't punch a pet, then does /free)
        HPMetadata.clearFrom(source, this);
        source.setMetadata(HPMetadata.OWNER_TARGET,
            new FixedMetadataValue(this, targetPlayer) );

        sender.sendMessage("[HappyPet] Right-click the pet to give to " + target);
        return true;
    }

    private boolean onCommandCalm(CommandSender sender, String[] args)
    {
        if ( !handlers.requirePlayer(sender) )
            return false;

        return true;
    }

    private boolean onCommandFree(CommandSender sender, String[] args)
    {
        if ( !handlers.requirePlayer(sender) )
            return false;

        return true;
    }

    private boolean onCommandWand(CommandSender sender, String[] args)
    {
        if ( !handlers.requirePlayer(sender) )
            return false;

        sender.sendMessage("[HappyPet] Infowand is set to: " + cfgWand);

        if ( !sender.hasPermission(HPPermissions.WAND) )
            sender.sendMessage("(You do not have permission to use the wand)");

        return true;
    }

    private boolean onCommandReload(CommandSender sender, String[] args)
    {
        if ( !handlers.requirePermission(sender, HPPermissions.RELOAD) )
            return false;

        onDisable();
        onEnable();

        sender.sendMessage("[HappyPet] Plugin reloaded");
        return true;
    }

//    private void setOwner(Player requestor, String newowner)
//    {
//        if (!requestor.hasPermission(admin))
//        {
//            requestor.sendMessage("Not allowed (" + admin);
//            return;
//        }
//        OfflinePlayer op = getServer().getOfflinePlayer(newowner);
//        if (!op.isOnline() && !op.hasPlayedBefore())
//        {
//            requestor.sendMessage(newowner + ": " + " no such player");
//            return;
//        }
//        requestor.removeMetadata(key, this);
//        requestor.setMetadata(key, new FixedMetadataValue(this, newowner));
//
//        requestor.sendMessage("Now right-click on the animal");
//    }
//
//    private void resetOwner(Player requestor)
//    {
//        requestor.setMetadata(key, new FixedMetadataValue(this, ""));
//        requestor.sendMessage("Now right-click on the animal");
//    }
//
//    void handleAngry(Player p, Entity ent)
//    {
//        Wolf dog = (Wolf) ent;
//        AnimalTamer owner = dog.getOwner();
//        if (owner == null)
//        {
//            p.sendMessage(ChatColor.RED + "This is a wild dog");
//            return;
//        }
//        p.sendMessage("This dog is owned by " + owner.getName());
//        if (p != owner && !p.hasPermission(admin))
//        {
//            return;
//        }
//        if (dog.isAngry())
//        {
//            dog.setAngry(false);
//            p.sendMessage(ChatColor.GREEN + "This dog is now happy!");
//        }
//    }
//
//    void reassign(Player p, Entity e, String newowner, Plugin context)
//    {
//        if (e.getType() != EntityType.WOLF && e.getType() != EntityType.OCELOT && e.getType() != EntityType.HORSE)
//        {
//            p.sendMessage("You can only reassign dogs, cats, and horses.");
//            return;
//        }
//        Tameable animal = (Tameable) e;
//        if (newowner.length() > 0)
//        {
//            // Assigning a new owner
//            OfflinePlayer owner = context.getServer().getOfflinePlayer(newowner);
//            if (owner != null && (owner.hasPlayedBefore() || owner.isOnline()))
//            {
//                animal.setOwner(owner);
//                p.sendMessage("Animal has been reassigned to " + animal.getOwner().getName());
//            }
//            else p.sendMessage("Invalid owner: " + newowner);
//        }
//        else
//        {
//    		/*
//    		 * Freeing a previously tamed animal. The animal's owner can do this
//    		 * or a user having animalhelper.admin permission
//    		 */
//            if (p.hasPermission(admin))
//            {
//                animal.setOwner(null);
//                p.sendMessage("Animal has been released.");
//            }
//            else if (animal.isTamed()) if (animal.getOwner() == p)
//            {
//                animal.setOwner(null);
//                p.sendMessage("Animal has been released.");
//            }
//            else p.sendMessage("This animal is owned by " + animal.getOwner().getName());
//            else p.sendMessage("This animal is not tamed.");
//        }
//
//        return;
//    }
}
