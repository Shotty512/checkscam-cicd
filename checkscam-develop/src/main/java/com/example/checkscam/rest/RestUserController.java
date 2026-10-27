package com.example.checkscam.rest;

import com.example.checkscam.dto.ResCreateUserDTO;
import com.example.checkscam.dto.request.CreateUserRequest;
import com.example.checkscam.dto.request.UpdateUserRequest;
import com.example.checkscam.entity.User;
import com.example.checkscam.response.CheckScamResponse;
import com.example.checkscam.service.UserService;
import com.example.checkscam.exception.IdInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class RestUserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RestUserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // Chỉ cho phép ADMIN tạo user
//    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody User postManUser) {
//        String hashPassword = this.passwordEncoder.encode(postManUser.getPassword());
//        postManUser.setPassword(hashPassword);
//        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleCreateUser(postManUser));
//    }
    public ResponseEntity<ResCreateUserDTO> createNewUser(@RequestBody CreateUserRequest request) {
        // Mã hóa mật khẩu
        String hashPassword = this.passwordEncoder.encode(request.getPassword());

        // Chuyển đổi từ DTO sang entity User
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(hashPassword)
                .build();
        ResCreateUserDTO response = this.userService.handleCreateUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @ExceptionHandler(value = IdInvalidException.class)
    public ResponseEntity<String> handleIdException(IdInvalidException idException) {
        return ResponseEntity.badRequest().body(idException.getMessage());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User deleted successfully");
        return ResponseEntity.ok(response);
    }

    // fetch user by id
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CheckScamResponse<User> getUserById(@PathVariable("id") long id) {
        return new CheckScamResponse<>(this.userService.fetchUserById(id));
    }

    // fetch all users
    @GetMapping()
    public ResponseEntity<List<User>> getAllUser() {
        // return ResponseEntity.ok(this.userService.fetchAllUser());
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest updateUserRequest) {
        User updatedUser = this.userService.handleUpdateUser(id, updateUserRequest);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser); //  Return 200 OK with the updated user.
        } else {
            return ResponseEntity.notFound().build(); //  Return 404 Not Found if the user doesn't exist.
        }
    }

}