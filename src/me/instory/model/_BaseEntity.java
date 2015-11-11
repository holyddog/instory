package me.instory.model;

import org.springframework.data.annotation.Id;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public abstract class _BaseEntity implements Serializable {
	private static final long serialVersionUID = -7039767502550947919L;

	private int version;

    @Id
    private String id;

    private Date timeCreated;

    public _BaseEntity() {
        this(UUID.randomUUID());
    }

    public _BaseEntity(UUID guid) {
        Assert.notNull(guid, "UUID is required");
        id = guid.toString();
        this.timeCreated = new Date();
    }

    public _BaseEntity(String guid) {
        Assert.notNull(guid, "UUID is required");
        id = guid;
        this.timeCreated = new Date();
    }

    public String getId() {
        return id;
    }

    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        _BaseEntity that = (_BaseEntity) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    public int getVersion() {
        return version;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

}
