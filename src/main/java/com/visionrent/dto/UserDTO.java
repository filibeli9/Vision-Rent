package com.visionrent.dto;

import java.util.HashSet;
import java.util.Set;

import com.visionrent.domain.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
	
	private Long id;

	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private String phoneNumber;
	
	private String address;
	
	private String postCode;
	
	private Boolean builtIn;
	
	private  Set<String> roles;
	
	public void setRoles(Set<Role> roles) {
		Set<String> roleStr = new HashSet<>();
		roles.forEach(r-> 
		{roleStr.add(r.getType().getName()); 
		});
		this.roles = roleStr;
	}
	
}
