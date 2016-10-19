package happypet.data;

import com.avaje.ebean.annotation.EnumValue;
import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "happypet_pets")
public class HPPet
{
    public enum Type
    {
        @EnumValue("0")
        Ocelot,

        @EnumValue("1")
        Wolf,

        @EnumValue("2")
        Horse,

        @EnumValue("3")
        Llama
    }

    @Id
    @NotNull  private UUID   petUuid;
    @NotEmpty private String petName;
    @NotNull  private Type   petType;
    @NotNull  private UUID   ownerUuid;
    @NotEmpty private String posWorld;
    @NotNull  private double posX;
    @NotNull  private double posY;
    @NotNull  private double posZ;

    @OneToMany(mappedBy = "pet")
    private List<HPSubOwner> subOwners;

    public UUID getPetUuid()
    {
        return petUuid;
    }

    public void setPetUuid(UUID petUuid)
    {
        this.petUuid = petUuid;
    }

    public String getPetName()
    {
        return petName;
    }

    public void setPetName(String petName)
    {
        this.petName = petName;
    }

    public Type getPetType()
    {
        return petType;
    }

    public void setPetType(Type petType)
    {
        this.petType = petType;
    }

    public UUID getOwnerUuid()
    {
        return ownerUuid;
    }

    public void setOwnerUuid(UUID ownerUuid)
    {
        this.ownerUuid = ownerUuid;
    }

    public String getPosWorld()
    {
        return posWorld;
    }

    public void setPosWorld(String posWorld)
    {
        this.posWorld = posWorld;
    }

    public double getPosX()
    {
        return posX;
    }

    public void setPosX(double posX)
    {
        this.posX = posX;
    }

    public double getPosY()
    {
        return posY;
    }

    public void setPosY(double posY)
    {
        this.posY = posY;
    }

    public double getPosZ()
    {
        return posZ;
    }

    public void setPosZ(double posZ)
    {
        this.posZ = posZ;
    }

    public List<HPSubOwner> getSubOwners()
    {
        return subOwners;
    }

    public void setSubOwners(List<HPSubOwner> subOwners)
    {
        this.subOwners = subOwners;
    }
}