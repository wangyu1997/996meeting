package com.lab.meet.Model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role_table")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    public static Role GenerateUserRole() {
        Role role = new Role();
        role.setDescription("普通用户权限");
        role.setName("ROLE_USER");
        return role;
    }
}
