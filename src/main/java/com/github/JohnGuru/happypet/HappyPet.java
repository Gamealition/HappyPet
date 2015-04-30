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
        if (   !handlers.requirePlayer(sender)
            || !handlers.requirePermission(sender, HPPermissions.CALM) )
            return true;

        Player source = (Player) sender;

        // Clear metadata to avoid conflict with previous commands
        // (e.g. if player does /owner, doesn't punch a pet, then does /free)
        HPMetadata.clearFrom(source, this);
        source.setMetadata(HPMetadata.CALM_TARGET, new FixedMetadataValue(this, null) );

        sender.sendMessage("[HappyPet] Right-click the wolf to calm down");
        return true;
    }

    private boolean onCommandFree(CommandSender sender, String[] args)
    {
        if (   !handlers.requirePlayer(sender)
            || !handlers.requirePermission(sender, HPPermissions.FREE) )
            return true;

        Player source = (Player) sender;

        // Clear metadata to avoid conflict with previous commands
        // (e.g. if player does /owner, doesn't punch a pet, then does /free)
        HPMetadata.clearFrom(source, this);
        source.setMetadata(HPMetadata.FREE_TARGET, new FixedMetadataValue(this, null) );

        sender.sendMessage("[HappyPet] Right-click the pet to free into the wild");
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
}
