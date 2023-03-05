package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.entities.Role;
import com.training.bloggingsite.entities.User;
import com.training.bloggingsite.exceptions.UserEmailAlreadyExistsException;
import com.training.bloggingsite.exceptions.UserNotFoundException;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.repositories.UserRepository;
import com.training.bloggingsite.services.interfaces.UserService;
import com.training.bloggingsite.utils.DefaultValue;
import com.training.bloggingsite.utils.UserConvertor;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public UserDto addUser(UserDto userDto) {
        User user = this.userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            User userToBeInserted = UserConvertor.toUser(userDto);
            Role role = this.roleRepository.findByName(DefaultValue.USER);
            Set<Role> roleSet = new HashSet<>();
            roleSet.add(role);
            userToBeInserted.setRoles(roleSet);
            this.userRepository.save(userToBeInserted);
            logger.info("User Added :" + userDto);
            return userDto;
        } else {
            logger.info("User already present with the email " + userDto);
            throw new UserEmailAlreadyExistsException(userDto.getEmail());
        }
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = this.userRepository.findAll();
        if (users == null) {
            throw new UserNotFoundException();
        }
        List<UserDto> userDtos = users.stream().map(U->UserConvertor.toUserDto(U)).collect(Collectors.toList());
        logger.info("Users fetched : " + userDtos);
        return userDtos;
    }

    @Override
    public UserDto findUserById(long id) {
        User user = this.userRepository.findById(id).get();
        if (user == null) {
            throw new UserNotFoundException();
        }
        UserDto userDto = UserConvertor.toUserDto(user);
        logger.info("User fetched by id :" + userDto + userDto.getId());
        return userDto;
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) {
            throw new UserNotFoundException();
        }
        UserDto userDto = UserConvertor.toUserDto(user);
        logger.info("User fetched by email :" + userDto);
        return userDto;
    }

    @Override
    public void updateUserRole(long id, String role) {
        User user = this.userRepository.findById(id).get();
        Set<Role> roleSet = new HashSet<>();

        Role roleToInsert = this.roleRepository.findByName(
                role.equals(DefaultValue.ADMIN) ? DefaultValue.USER
                        : DefaultValue.ADMIN);

        roleSet.add(roleToInsert);
        user.setRoles(roleSet);
        logger.info("User " + user.getName() + " changed as " + roleToInsert.getName() + ".");
        this.userRepository.save(user);
    }


}
