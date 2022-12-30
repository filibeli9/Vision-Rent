package com.visionrent.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.visionrent.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByEmail(String email);

	// ManyToMany User_Role relation default fetch type is LAZY
	// We do it EAGER by this annotation to get roles with users
	@EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);

}
