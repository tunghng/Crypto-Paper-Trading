package com.tma.helper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_credential")
public class UserCredential {

    @Id
    private UUID userId;

    private boolean enabled = true;

    @JsonIgnore
    private String password;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

}
