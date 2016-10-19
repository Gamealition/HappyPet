package happypet.imports;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.SqlRow;
import happypet.data.HPPet;
import happypet.data.HPSubOwner;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static happypet.HappyPet.LOGGER;
import static happypet.HappyPet.PLUGIN;

/** Imports data from Raum's Stables plugin into HappyPet, in a fail-safe manner */
public class StablesImport
{
    private EbeanServer      database;
    private List<SqlRow>     stablesHorses;
    private List<SqlRow>     stablesRiders;
    private List<HPPet>      newPets;
    private List<HPSubOwner> newSubOwners;

    /** Begins an import from Stables into HappyPet */
    public void start()
    {
        LOGGER.info("Beginning import of Stables data...");

        try
        {
            database = PLUGIN.getDatabase();

            getStablesData();
            stablesHorses.forEach(this::convertStablesHorses);
            stablesRiders.forEach(this::convertStablesRiders);
            commitHappyPetData();

            LOGGER.info("Import of Stables data finished");
        }
        catch (Exception e)
        {
            LOGGER
                .warn("Stables import failed: %s", e.getMessage())
                .warn("↳ Caused by %s", e.getCause())
                .warn("No changes were made. Check your database and try again.");
        }
    }

    /** Fetches Stables horses and riders into temporary collection of maps */
    private void getStablesData()
    {
        try
        {
            stablesHorses = database
                .createSqlQuery("SELECT * FROM stables_horses ORDER BY id")
                .findList();

            stablesRiders = database
                .createSqlQuery("SELECT * FROM stables_riders")
                .findList();

            if (stablesHorses.size() == 0)
                throw new IllegalStateException();

            newPets      = new ArrayList<>( stablesHorses.size() );
            newSubOwners = new ArrayList<>( stablesRiders.size() );

            LOGGER.info("Found %d horses and %d riders to import from Stables",
                stablesHorses.size(), stablesRiders.size()
            );
        }
        catch (PersistenceException e)
        {
            throw new RuntimeException("Could not get Stables data; either the tables do not " +
                "exist or something went wrong on the database server.", e);
        }
        catch (IllegalStateException e)
        {
            throw new RuntimeException("Stables' horse table appears to be empty. Either there " +
                "are no horses to import or something has gone wrong with the data.", e);
        }
    }

    private void convertStablesHorses(SqlRow horse)
    {
        int id = horse.getInteger("id");
        try
        {
            HPPet pet = new HPPet();

            pet.setPetType(HPPet.Type.Horse);
            pet.setPetUuid( horse.getUUID("uid") );
            pet.setPetName( horse.getString("named") );
            pet.setOwnerUuid( horse.getUUID("owneruuid") );
            pet.setPosWorld( horse.getString("world") );
            pet.setPosX( horse.getDouble("x") );
            pet.setPosY( horse.getDouble("y") );
            pet.setPosZ( horse.getDouble("z") );

            InvalidValue error = database.validate(pet);
            if (error != null)
            {
                LOGGER.warn("Skipping horse ID %d because of bad data: %s", id, error );
                return;
            }

            newPets.add(pet);
        }
        catch (Exception e)
        {
            LOGGER.warn("Skipping horse ID %d because of exception: %s", id, e);
        }
    }

    private void convertStablesRiders(SqlRow rider)
    {
        int id = rider.getInteger("id");

        try
        {
            HPSubOwner subOwner  = new HPSubOwner();
            HPPet      linkedPet = newPets.stream()
                .filter( pet -> pet.getPetUuid().equals( rider.getUUID("uid") ) )
                .findFirst()
                .get();

            // No need to validate; if it's a bad UUID, we'll get an IllegalArgumentException
            subOwner.setSubOwnerUuid( rider.getUUID("rideruuid") );
            subOwner.setPet(linkedPet);

            newSubOwners.add(subOwner);
        }
        catch (NoSuchElementException e)
        {
            LOGGER.warn("Skipping rider ID %d because its horse is missing", id);
        }
        catch (Exception e)
        {
            LOGGER.warn("Skipping rider ID %d because of exception: %s", id, e);
        }
    }

    private void commitHappyPetData()
    {
        try
        {
            database.beginTransaction();
            int subOwnerRows = database.save(newSubOwners);
            int petRows      = database.save(newPets);
            database.commitTransaction();

            LOGGER.info("Saved %d pets and %d sub-owners from Stables", petRows, subOwnerRows);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not commit new data to HappyPet. No data has been " +
                "saved to HappyPet's tables.", e);
        }
        finally
        {
            database.endTransaction();
        }
    }
}
