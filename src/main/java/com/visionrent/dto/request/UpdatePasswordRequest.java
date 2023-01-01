package com.visionrent.dto.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

	@NotBlank(message = "Provide old password")
	private String oldPassword;
	
	@NotBlank(message = "Provide new password")
	private String newPassword;
}
