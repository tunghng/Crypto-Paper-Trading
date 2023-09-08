package com.microservices.crypto.sso.dto.mapper;


import com.microservices.crypto.sso.model.AppUser;
import com.microservices.crypto.sso.dto.model.AppUserDto;
import org.mapstruct.Mapper;


@Mapper
public interface AppUserMapper {

    AppUserDto toDto(AppUser user);

}
