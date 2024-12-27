package com.montage.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.auth.entity.AuthProvider;
import com.montage.auth.entity.User;
import com.montage.auth.exception.ResourceNotFoundException;
import com.montage.auth.repository.UserRepository;

import com.montage.auth.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
  

    @Override
    @Transactional
    public User findOrCreateAzureUser(String email, String name) {
//        return userRepository.findByEmail(email)
//                .orElseGet(() -> {
//                    User newUser = new User();
//                    newUser.setEmail(email);
//                    newUser.setName(name);
//                    newUser.setAuthProvider(AuthProvider.AZURE);
//                    newUser.setEnabled(true);
//                    newUser.setActive(true);
//                    return userRepository.save(newUser);
//                });
    	User respose = new User();
    	return respose;
    }

    @Override
    public User createUser(User user) {
        
        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);
        //existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setEnabled(user.isEnabled());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setPhone(user.getPhone());
        // Don't update sensitive fields
        // existingUser.setPassword(user.getPassword());
        // existingUser.setAuthProvider(user.getAuthProvider());
        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public List<User> findAllByAuthProvider(AuthProvider provider) {
    	List<User> respo= new ArrayList<>();
    	// userRepository.findAllByAuthProvider(provider);
    	return respo;
    }

   
} 