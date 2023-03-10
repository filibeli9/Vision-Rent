package com.visionrent.dto.request;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserUpdateRequest {
	@Size(max = 50)
	@NotBlank(message = "Please provide your first name")
	private String firstName;
	
	@Size(max = 50)
	@NotBlank(message = "Please provide your last name")
	private String lastName;
	
	@Size(min=5,max=80)
	@NotBlank(message = "Please provide your email" )
	private String email;
	
	@Size(min=5,max=80,message="Please provide Correct Size of Password")
	@NotBlank(message = "Please provide your password")
	private String password;
	
	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[-.]?\\d{3}[-.]?\\d{4}$",
            message = "Please provide valid phone number")
	@Size(max = 14)
	@NotBlank(message = "Please provide your phone number")
	private String phoneNumber;
	
	@Size(max=100)
	@NotBlank(message = "Please provide your address")
	private String address;
	
	@Size(max = 8)
	@NotBlank(message = "Please provide your post code")
	private String postCode;
	
	private Boolean builtIn;
	
	private Set<String> roles;

}
