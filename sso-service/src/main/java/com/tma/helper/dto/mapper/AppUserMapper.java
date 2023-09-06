package com.tma.helper.dto.mapper;


import com.tma.helper.dto.model.AppUserDto;
import com.tma.helper.model.AppUser;
import org.mapstruct.Mapper;


@Mapper
public interface AppUserMapper {

    AppUserDto toDto(AppUser user);

}
