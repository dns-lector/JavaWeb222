package learning.itstep.javaweb222.data.dto;

import java.util.Date;
import java.util.UUID;

public class User {
    private UUID id;
    private String name;    
    private String email;
    private Date birthdate;
    private Date registeredAt;
    private Date deletedAt;

    public UUID getId() {
        return id;
    }

    public User setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public User setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public Date getRegisteredAt() {
        return registeredAt;
    }

    public User setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
        return this;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public User setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
    
    
}
