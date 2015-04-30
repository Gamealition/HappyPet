package com.github.JohnGuru.happypet;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HPHandlers
{
    private static final DecimalFormat DOUBLE_FMT = new DecimalFormat("#.##");

    private final HappyPet happyPet;

    public HPHandlers(HappyPet happyPet) { this.happyPet = happyPet; }

    /**
     * Checks if a given command sender is a player. Gives message if not.
     */
    public boolean requirePlayer(CommandSender sender)
    {
        if (sender instanceof Player)
            return true;
        else
        {
            sender.sendMessage("[HappyPet] Command only executable by player");
            return false;
        }
    }

    /**
     * Checks if a given command sender has a given permission or is console; if not,
     * gives an error message.
     */
    public boolean requirePermission(CommandSender sender, String permission)
    {
        if ( sender.hasPermission(permission) || !(sender instanceof Player) )
            return true;
        else
        {
            sender.sendMessage("[HappyPet] Command requires permission: " + permission);
            return false;
        }
    }

    /**
     * Gets an offline record of a player known by the server
     *
     * @param name Case-insensitive name of player to find
     * @return Offline player if known, null if not
     */
    public OfflinePlayer getPreviousPlayer(String name)
    {
        for ( OfflinePlayer player : happyPet.getServer().getOfflinePlayers() )
            if ( player.getName().equalsIgnoreCase(name) )
                return player;

        return null;
    }

    public void showPetInfo(Animals animal, Player player)
    {
        ArrayList<String> msg = new ArrayList<>();

        Tameable    tameable = (Tameable) animal;
        AnimalTamer tamer    = tameable.getOwner();

        String name      = animal.getName();
        String nickname  = animal.getCustomName();
        String health    = DOUBLE_FMT.format( animal.getHealth() );
        String maxHealth = DOUBLE_FMT.format( animal.getMaxHealth() );

        msg.add("Infocard for " + name);

        if (nickname != null) msg.add( "• Nickname: " + nickname );
        if (tamer    != null) msg.add( "• Owner: "    + tamer.getName() );
        else                  msg.add( "• Owner: <none>" );

        msg.add( "• Stage: "     + parseStage( animal.getAge() ) );
        msg.add( "• Age: "       + parseLived( animal.getTicksLived() ) );
        msg.add( "• Breedable: " + animal.canBreed() );
        msg.add( "• Health: "    + health + "/" + maxHealth );

        if (animal instanceof Wolf)
        {
            Wolf wolf = (Wolf) animal;
            msg.add( "• Angry: "   + wolf.isAngry() );
            msg.add( "• Sitting: " + wolf.isSitting() );
            msg.add( "• Collar: "  + wolf.getCollarColor().toString() );
        }
        else if (animal instanceof Ocelot)
        {
            Ocelot  ocelot  = (Ocelot) animal;
            msg.add( "• Sitting: " + ocelot.isSitting() );
            msg.add( "• Type: "    + ocelot.getCatType().toString() );
        }
        else if (animal instanceof Horse)
        {
            Horse horse = (Horse) animal;
        }

        player.sendMessage( msg.toArray( new String[msg.size()] ) );
    }

    public String parseStage(int age)
    {
        age = -age / 20;

        if (age == 0)
            return "Adult";
        else
            return "Child (" + -age + " seconds until adult)";
    }

    public String parseLived(int lived)
    {
        return (lived / 20) + " seconds old";
    }
}
