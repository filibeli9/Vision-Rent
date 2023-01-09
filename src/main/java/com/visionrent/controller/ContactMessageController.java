package com.visionrent.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.visionrent.service.ContactMessageService;
import com.visionrent.domain.ContactMessage;
import com.visionrent.dto.ContactMessageDTO;
import com.visionrent.dto.request.ContactMessageRequest;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.mapper.ContactMessageMapper;

@RestController
@RequestMapping("/contactmessage")
public class ContactMessageController {

	private ContactMessageService contactMessageService;
	private ContactMessageMapper contactMessageMapper;

	@Autowired
	public ContactMessageController(ContactMessageService contactMessageService,
			ContactMessageMapper contactMessageMapper) {
		this.contactMessageService = contactMessageService;
		this.contactMessageMapper = contactMessageMapper;
	}

	// localhost:8080/contactmessage/visitors
	@PostMapping("/visitors")
	public ResponseEntity<VRResponse> createMessage(@Valid @RequestBody ContactMessageRequest contactMessageRequest) {

		// to change DTO to POJO with mapstruct

		ContactMessage contactMessage = contactMessageMapper
				.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.saveMessage(contactMessage);

		VRResponse response = new VRResponse("ContactMessage successfully created", true);
		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	// to get all contact messages
	@GetMapping
	public ResponseEntity<List<ContactMessageDTO>> getAllContactMessage() {
		List<ContactMessage> contactMessagesList = contactMessageService.getAll();
		// mapstruct
		List<ContactMessageDTO> contactMessageDTOList = contactMessageMapper.map(contactMessagesList);
		return ResponseEntity.ok(contactMessageDTOList);
	}
	// if data is too much it is best to make getAll method with paging

	@GetMapping("/pages")
	public ResponseEntity<Page<ContactMessageDTO>> getAllContactMessageWithPage(@RequestParam("page") int page,
																				@RequestParam("size") int size, 
																				@RequestParam("sort") String prop, // to sort by what
																				@RequestParam(value = "direction", 
																				required = false, 
																				defaultValue = "DESC") Direction direction) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
		Page<ContactMessage> contactMessagePage = contactMessageService.getAll(pageable);
		// ContactMessage --> ContactMessageDTO
		// add getPageDTO method at the end of this class
		Page<ContactMessageDTO> pageDTO = getPageDTO(contactMessagePage);
		return ResponseEntity.ok(pageDTO);
	}

	// to get a specific ContactMessage
	@GetMapping("/{id}")
	public ResponseEntity<ContactMessageDTO> getMessageWithPath(@PathVariable("id") Long id) {
		ContactMessage contactMessage = contactMessageService.getContactMessage(id);
		ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);

		return ResponseEntity.ok(contactMessageDTO);
	}

	// getById with RequestParam
	@GetMapping("/request")
	public ResponseEntity<ContactMessageDTO> getMessageWithRequestParam(@RequestParam("id") Long id) {
		ContactMessage contactMessage = contactMessageService.getContactMessage(id);
		ContactMessageDTO contactMessageDTO = contactMessageMapper.contactMessageToDTO(contactMessage);

		return ResponseEntity.ok(contactMessageDTO);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<VRResponse> deleteContactMessage(@PathVariable Long id) {
		contactMessageService.deleteContactMessage(id);
		VRResponse response = new VRResponse(ResponseMessage.CONTACTMESSAGE_DELETE_RESPONSE, true);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<VRResponse> dupdateContactMessage(@PathVariable Long id,
			@Valid @RequestBody ContactMessageRequest contactMessageRequest) {
		ContactMessage contactMessage = contactMessageMapper
				.contactMessageRequestToContactMessage(contactMessageRequest);
		contactMessageService.updateContactMessage(id, contactMessage);
		VRResponse response = new VRResponse(ResponseMessage.CONTACTMESSAGE_UPDATE_RESPONSE, true);

		return ResponseEntity.ok(response);
	}

	// getPageDTO method
	private Page<ContactMessageDTO> getPageDTO(Page<ContactMessage> contactMessagePage) {
		// using map method belongs Page class
		Page<ContactMessageDTO> dtoPage = contactMessagePage
				.map(new java.util.function.Function<ContactMessage, ContactMessageDTO>() {
					@Override
					public ContactMessageDTO apply(ContactMessage contactMessage) {

						return contactMessageMapper.contactMessageToDTO(contactMessage);
					}
				});
		return dtoPage;
	}

}
