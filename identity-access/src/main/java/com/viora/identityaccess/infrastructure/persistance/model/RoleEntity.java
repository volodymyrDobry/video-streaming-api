package com.viora.identityaccess.infrastructure.persistance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor(force = true)
public class RoleEntity {
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private final String name;
}
