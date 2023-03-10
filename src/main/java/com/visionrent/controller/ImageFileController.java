package com.visionrent.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.visionrent.domain.ImageFile;
import com.visionrent.dto.ImageFileDTO;
import com.visionrent.dto.response.ImageSaveResponse;
import com.visionrent.dto.response.ResponseMessage;
import com.visionrent.dto.response.VRResponse;
import com.visionrent.service.ImageFileService;

@RestController
@RequestMapping("/files")
public class ImageFileController {

	@Autowired
	private ImageFileService imageFileService;
	
	//***upload
	@PostMapping("/upload")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ImageSaveResponse> uploadFile(@RequestParam("file")MultipartFile file){
		String imageId= imageFileService.saveImage(file);
		
		ImageSaveResponse response = new ImageSaveResponse(imageId, ResponseMessage.IMAGE_SAVED_RESPONSE,true);
		
		return ResponseEntity.ok(response);
	}
	
	//***download
	@GetMapping("/download/{id}")
	public ResponseEntity<byte[]> downloadFile(@PathVariable String id){
		ImageFile imageFile = imageFileService.getImageById(id);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename=" + imageFile.getName()).
				body(imageFile.getImageData().getData());
	}
	
	//****image Display
	@GetMapping("/display/{id}")
	public ResponseEntity<byte[]> displayFile(@PathVariable String id){
		ImageFile imageFile = imageFileService.getImageById(id);
		// to put MediaType information to header
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<>(imageFile.getImageData().getData(),header,HttpStatus.OK);
	}
	
	//****Get All Images
	@GetMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ImageFileDTO>> getAllImages(){
		List<ImageFileDTO> imagefileDTOs = imageFileService.getAllImages();
		
		return ResponseEntity.ok(imagefileDTOs);
	}
	
	//****Delete Images
	@DeleteMapping("/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<VRResponse> deleteImage(@PathVariable String id){
		imageFileService.removeById(id);
		VRResponse response = new VRResponse(ResponseMessage.IMAGE_DELETE_RESPONSE,true);
		
		return ResponseEntity.ok(response);
	}

	
}
