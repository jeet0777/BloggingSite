package com.training.bloggingsite.dtos;

public class RoleDto {

    private int id;
    private String role;

    @Override
    public String toString() {
        return "RoleDto{" +
                "role='" + role + '\'' +
                '}';
    }

    public RoleDto(){}

    public RoleDto(int id, String role) {
        this.id = id;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}