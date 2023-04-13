package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email") })
public class User {
    @Id
    @Column(name = "user_id", length = 50, nullable = false)
    private String userId;

    @Column(name = "username", length = 50, nullable = false)
    private String username;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "gender", length = 15, nullable = false)
    private String gender;

    @Column(name = "age")
    private Long age;

    @Column(name = "phone", length = 15, nullable = false)
    private String phone;

    @Column(name = "address", length = 250, nullable = false)
    private String address;

    @Column(name = "created_date", nullable = false)
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "bd_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "is_admin")
    private Boolean isAdmin = true;

    public User(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }

    public User() {

    }

    @Transient
    private String responseMessage;

    @Transient
    private String jwt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
