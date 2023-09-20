package com.example.spring.models;

import enums.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "TB_ROLE")
public class RoleModel implements Serializable, GrantedAuthority {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private Role role;

    @Override
    public String getAuthority() {
        return this.role.toString();
    }

    public UUID getRoleId() {
        return roleId;
    }

    public void setRoleId(UUID roleId) {
        this.roleId = roleId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
