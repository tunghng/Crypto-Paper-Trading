package com.microservices.crypto.sso.model;

import com.microservices.crypto.sso.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "app_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser extends BaseEntity {
    private String username;
    private String displayName;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
