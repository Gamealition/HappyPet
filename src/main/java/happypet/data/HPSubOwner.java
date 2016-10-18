package happypet.data;

import com.avaje.ebean.validation.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "happypet_subowners")
public class HPSubOwner
{
    @Id private int id;

    @NotEmpty private String subOwnerUuid;

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

    public String getSubOwnerUuid()
    {
        return subOwnerUuid;
    }

    public void setSubOwnerUuid(String subOwnerUuid)
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