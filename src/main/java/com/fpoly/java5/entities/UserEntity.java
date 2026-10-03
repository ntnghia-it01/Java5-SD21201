package com.fpoly.java5.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private int id;
	
	@Column(name = "full_name", columnDefinition = "NVARCHAR (100)", nullable = false)
	private String name;
	
	@Column(name = "email", length = 150, nullable = false)
	private String email;
	
	@Column(name = "password_hash", length = 255, nullable = false)
	private String password;
	
	@Column(name = "phone", length = 20, nullable = true)
	private String phone;
	
	@Column(name = "address", columnDefinition = "NVARCHAR (100)", nullable = true)
	private String address;
	
	@Column(name = "role", nullable = true)
	private int role = 1;
	
	@Column(name = "is_active", nullable = true)
	private boolean active = true;
}


//CREATE TABLE [dbo].[users] (
//	    [user_id]       INT            IDENTITY (1, 1) NOT NULL,
//	    [full_name]     NVARCHAR (100) NOT NULL,
//	    [email]         VARCHAR (150)  NOT NULL,
//	    [password_hash] VARCHAR (255)  NOT NULL,
//	    [phone]         VARCHAR (20)   NULL,
//	    [address]       NVARCHAR (255) NULL,
//	    [role]          INT            DEFAULT ((1)) NOT NULL,
//	    [is_active]     BIT            DEFAULT ((1)) NOT NULL,
//	    PRIMARY KEY CLUSTERED ([user_id] ASC),
//	    CONSTRAINT [ck_users_role] CHECK ([role]=(2) OR [role]=(1)),
//	    UNIQUE NONCLUSTERED ([email] ASC)
//	);
//
