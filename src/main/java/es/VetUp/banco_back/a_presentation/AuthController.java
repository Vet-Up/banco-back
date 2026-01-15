package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.a_presentation.mapper.UserPresentationMapper;
import es.VetUp.banco_back.a_presentation.webModel.request.LoginRequest;
import es.VetUp.banco_back.a_presentation.webModel.response.LoginResponse;
import es.VetUp.banco_back.a_presentation.webModel.response.UserDetailResponse;
import es.VetUp.banco_back.b_domain.service.JwtService;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Optional<UserDto> userOpt = userService.findByUsername(loginRequest.username());
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginRequest.password(), userOpt.get().password())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtService.generateToken(userOpt.get());
        return new LoginResponse(token);
    }

    @GetMapping("/validate")
    public UserDetailResponse validateToken(@RequestHeader("Authorization") String authHeader) {
        UserDto user = jwtService.getUserFromToken(authHeader.substring(7));
        return UserPresentationMapper.getInstance().fromUserDtoToUserDetailResponse(user);
    }
}
