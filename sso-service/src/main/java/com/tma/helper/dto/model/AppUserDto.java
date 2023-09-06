package com.tma.helper.dto.model;

import com.tma.helper.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
    private UUID id;
    private String username;
    private String displayName;
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
