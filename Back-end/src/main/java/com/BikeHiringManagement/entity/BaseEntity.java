package com.BikeHiringManagement.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_user", nullable = false)
    private String createdUser;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_user")
    private String modifiedUser;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @Column(name = "is_deleted")
    private Boolean isDeleted = Boolean.FALSE;

    public BaseEntity() {
    }

    public BaseEntity(Long id, String createUser, Date createTime, String modifiedUser, Date modifiedTime, boolean isDeleted) {
        this.id = id;
        this.createdUser = createUser;
        this.createdDate = createTime;
        this.modifiedUser = modifiedUser;
        this.modifiedDate = modifiedTime;
        this.isDeleted = isDeleted;
    }


}
