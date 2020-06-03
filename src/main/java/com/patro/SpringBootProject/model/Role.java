package com.patro.SpringBootProject.model;



import javax.persistence.*;

@Entity
@Table(name = "role_data")
public class Role {

    @Id
    @Column(name = "role_id")
    private String id;

    @Column(name = "role_name")
    private String role;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
