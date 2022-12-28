package com.visionrent.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.ContactMessageDTO;
import com.visionrent.dto.request.ContactMessageRequest;

@Mapper(componentModel = "spring") // to inject any class and use
public interface ContactMessageMapper {

	// ContactMessage -->ContactMessageDTO
	ContactMessageDTO contactMessageToDTO(ContactMessage contactMessage);

	/*
	 * what we want to do with mapping contactMessageDTO.name = contactMessage.name;
	 * contactMessageDTO.subject = contactMessage.subject;
	 */

	// ContactMessageRequest --> ContactMessage
	@Mapping(target = "id", ignore = true) // don't map id field of the target
	ContactMessage contactMessageRequestToContactMessage(ContactMessageRequest contactMessageRequest);

	// List<ContactMessage> --> List<ContactMessageDTO>
	List<ContactMessageDTO> map(List<ContactMessage> contactMessageList);

}
