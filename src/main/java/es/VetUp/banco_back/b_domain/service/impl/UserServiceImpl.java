package es.VetUp.banco_back.b_domain.service.impl;

import es.VetUp.banco_back.b_domain.exception.ResourceNotFoundException;
import es.VetUp.banco_back.b_domain.mapper.UserMapper;
import es.VetUp.banco_back.b_domain.model.User;
import es.VetUp.banco_back.b_domain.repository.UserRepository;
import es.VetUp.banco_back.b_domain.repository.entity.UserEntity;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Boolean authenticate(String username, String apiKey) {
        return userRepository.existsByApiKey(username, apiKey)
            .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found."));
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

    @Override
    public Optional<UserDto> findById(Long userId) {
        return userRepository.findById(userId)
            .map(userEntity -> UserMapper.getInstance()
                .fromUserToUserDto(UserMapper.getInstance().fromUserEntityToUser(userEntity)));
    }
}
