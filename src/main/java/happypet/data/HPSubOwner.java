package happypet.data;

import com.avaje.ebean.validation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "happypet_subowners")
public class HPSubOwner
{
    @Id private int id;

    @NotNull private UUID subOwnerUuid;

    @ManyToOne(optional = false)
    private HPPet pet;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public UUID getSubOwnerUuid()
    {
        return subOwnerUuid;
    }

    public void setSubOwnerUuid(UUID subOwnerUuid)
    {
        this.subOwnerUuid = subOwnerUuid;
    }

    public HPPet getPet()
    {
        return pet;
    }

    public void setPet(HPPet pet)
    {
        this.pet = pet;
    }
}