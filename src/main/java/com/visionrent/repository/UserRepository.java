package com.visionrent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.visionrent.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Boolean existsByEmail(String email);

	// ManyToMany User_Role relation default fetch type is LAZY
	// We do it EAGER by this annotation to get roles with users
	@EntityGraph(attributePaths = "roles")
	Optional<User> findByEmail(String email);
	
	@EntityGraph(attributePaths = "roles")
	List<User> findAll();// role is defined LAZY by default so each role creates sql code, to disable that we make fetch type EAGER by 
						// @EntityGraph annotation
	
	@EntityGraph(attributePaths = "roles")
	Page<User> findAll(Pageable pageable);

	@EntityGraph(attributePaths = "roles")
	Optional<User> findById(Long id);
	
	@EntityGraph(attributePaths = "id")// if id is put here, roles aren't retrieved
	Optional<User> findUserById(Long id);
	
	@Modifying// if we do DML operations with custom query in JpaRepository 
	@Query("UPDATE User u SET u.firstName = :firstName, u.lastName = :lastName, u.email = :email, u.phoneNumber = :phoneNumber, u.address = :address, u.postCode = :postCode WHERE u.id = :id")
	void update(@Param("id") Long id,
				@Param("firstName") String firstName,
				@Param("lastName") String lastName,
				@Param("email") String email,
				@Param("phoneNumber") String phoneNumber,
				@Param("address") String address,
				@Param("postCode") String postCode);
	
}
