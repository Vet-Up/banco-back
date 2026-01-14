package es.VetUp.banco_back.a_presentation;

import es.VetUp.banco_back.a_presentation.mapper.UserPresentationMapper;
import es.VetUp.banco_back.a_presentation.webModel.request.ApiKeyRequest;
import es.VetUp.banco_back.a_presentation.webModel.response.UserDetailResponse;
import es.VetUp.banco_back.b_domain.service.UserService;
import es.VetUp.banco_back.b_domain.service.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {                                                                   
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserDetailResponse> getUserByUsername(@PathVariable String username){
        UserDto userDto = userService.findByUsername(username)
                .orElseThrow(()-> new RuntimeException("User not found"));
        UserDetailResponse userDetailResponse = UserPresentationMapper.getInstance().fromUserDtoToUserDetailResponse(userDto);

        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @GetMapping("/by-dni/{dni}")
    public ResponseEntity<UserDetailResponse> getUserByDni(@PathVariable String dni){
        UserDto userDto = userService.findByDni(dni)
                .orElseThrow(()-> new RuntimeException("User not found"));
        UserDetailResponse userDetailResponse = UserPresentationMapper.getInstance().fromUserDtoToUserDetailResponse(userDto);

        return new ResponseEntity<>(userDetailResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDetailResponse> checkApiKey(@RequestBody ApiKeyRequest apiKeyRequest){

        if(!userService.authenticate(apiKeyRequest.username(),apiKeyRequest.apiKey())){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
