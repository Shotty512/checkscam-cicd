package com.example.checkscam.service;

import com.example.checkscam.constant.RoleName;
import com.example.checkscam.dto.ResCreateUserDTO;
import com.example.checkscam.dto.request.UpdateUserRequest;
import com.example.checkscam.entity.Role;
import com.example.checkscam.entity.User;
import com.example.checkscam.repository.RoleRepository;
import com.example.checkscam.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResCreateUserDTO handleCreateUser(User user) {
        ResCreateUserDTO resCreateUserDTO = new ResCreateUserDTO();
        // Lấy role COLLAB
        Role collabRole = roleRepository.findByName(RoleName.COLLAB);
        Set<Role> roles = new HashSet<>();
        roles.add(collabRole);
        user.setRoles(roles); // Gán role COLLAB cho user

        user = userRepository.save(user);
        resCreateUserDTO.setId(user.getId());
        resCreateUserDTO.setEmail(user.getEmail());
        resCreateUserDTO.setName(user.getName());
        return resCreateUserDTO;
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.orElse(null);
    }

    public List<User> fetchAllUser() {
        return this.userRepository.findAll();
    }


    public User handleUpdateUser(Long id, UpdateUserRequest updateUserRequest) {
        User currentUser = this.fetchUserById(id);
        if (currentUser != null) {
            currentUser.setEmail(updateUserRequest.getEmail());
            currentUser.setName(updateUserRequest.getName());
            if (updateUserRequest.getPassword() != null && !updateUserRequest.getPassword().isEmpty()) {
                // Mã hóa mật khẩu trước khi lưu
                String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
                currentUser.setPassword(encodedPassword);
            }
            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

}
