package es.VetUp.banco_back.a_presentation.mapper;

import es.VetUp.banco_back.a_presentation.webModel.response.UserDetailResponse;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;

public class UserPresentationMapper {
    private static UserPresentationMapper INSTANCE;

    private UserPresentationMapper(){

    }

    public static UserPresentationMapper getInstance(){
        if (INSTANCE == null){
            INSTANCE = new UserPresentationMapper();
        }
        return INSTANCE;
    }

    public UserDetailResponse fromUserDtoToUserDetailResponse(UserDto userDto){
        return new UserDetailResponse(
                userDto.userId(),
                userDto.username(),
                userDto.password(),
                userDto.name(),
                userDto.firstSurname(),
                userDto.secondSurname(),
                userDto.dni()
        );
    }



}
