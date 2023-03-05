package com.training.bloggingsite.services.impl;

import com.training.bloggingsite.dtos.RoleDto;
import com.training.bloggingsite.entities.Role;
import com.training.bloggingsite.exceptions.RoleAlreadyExistsException;
import com.training.bloggingsite.exceptions.RoleNotFoundException;
import com.training.bloggingsite.repositories.RoleRepository;
import com.training.bloggingsite.services.interfaces.RoleService;
import com.training.bloggingsite.utils.RoleConvertor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    private Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Override
    public RoleDto addRole(RoleDto roleDto) {
        Role checkRoleInDB = this.roleRepository.findByName(roleDto.getName());

        if (checkRoleInDB == null) {
            Role roleToBeInserted = RoleConvertor.toRole(roleDto);
            roleToBeInserted.setName(roleToBeInserted.getName().toUpperCase());
            this.roleRepository.save(roleToBeInserted);
            logger.info("Role Added :" + roleDto);
            return roleDto;
        } else {
            throw new RoleAlreadyExistsException(roleDto.getName().toUpperCase());
        }
    }

    @Override
    public List<RoleDto> findAllRoles() {
        List<Role> roles = this.roleRepository.findAll();
        if (roles == null) {
            throw new RoleNotFoundException();
        }
        List<RoleDto> roleDtos = roles.stream().map(R->RoleConvertor.toRoleDto(R)).collect(Collectors.toList());
        logger.info("Roles Fetched :" + roleDtos);
        return roleDtos;
    }

    @Override
    public void deleteRole(int id) {
        this.roleRepository.deleteById(id);
        logger.info("Role Deleted with id :" + id);
    }

}
