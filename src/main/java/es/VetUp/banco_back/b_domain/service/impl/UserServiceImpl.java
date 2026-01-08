package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.exception.ResourceNotFoundException;
import es.VetUp.banco_back.b_domain.mapper.UserMapper;
import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.repository.UserRepository;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto authenticate(String username, String apiKey) {
        return userRepository.findByUsername(username)
            .map(userEntity -> UserMapper.getInstance().fromUserToUserDto(
                UserMapper.getInstance().fromUserEntityToUser(userEntity)))
            .filter(userDto -> userDto.apiKey().equals(apiKey))
            .orElseThrow(() -> new ResourceNotFoundException("Usuario o API key incorrectos"));
    }

    @Override
    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .map(userEntity -> UserMapper.getInstance()
                .fromUserToUserDto(UserMapper.getInstance().fromUserEntityToUser(userEntity)));
    }

    @Override
    public Optional<UserDto> findByDni(String dni) {
        return userRepository.findByDni(dni)
            .map(userEntity -> UserMapper.getInstance()
                .fromUserToUserDto(UserMapper.getInstance().fromUserEntityToUser(userEntity)));
    }
}
