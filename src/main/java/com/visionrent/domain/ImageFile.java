package com.visionrent.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_imageFile")
public class ImageFile {
	
	@Id
	@GeneratedValue(generator = "uuid")//12 characters String id
	@GenericGenerator(name= "uuid", strategy = "uuid2")
	private String id;
	
	private String name;
	
	private String type;
	
	private long length;
	
	@OneToOne(cascade = CascadeType.ALL)// if imageFile gets deleted, imageData is to get deleted as well
	private ImageData imageData;
	
	public ImageFile(String name, String type, ImageData imageData) {
		this.name = name;
		this.type = type;
		this.imageData = imageData;
		this.length = imageData.getData().length;// ImageFile data length equals ImageData data.length
	}

}
