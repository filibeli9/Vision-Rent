package com.visionrent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionrent.domain.ImageFile;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String>{

	@EntityGraph(attributePaths = "id")// related imageData fields aren't got to brought, only that class' fields are going to be brought
	List<ImageFile> findAll();
}
