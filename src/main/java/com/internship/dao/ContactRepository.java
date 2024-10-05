package com.internship.dao;


import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.internship.entities.Contact;
import com.internship.entities.User;

public interface ContactRepository extends JpaRepository<Contact,Integer>
{
	//pagination...
	@Query("from Contact as c where c.user.id =:userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId, org.springframework.data.domain.Pageable pageable);

	//public Page<Contact> findContactsByUser(int id, org.springframework.data.domain.Pageable pageable);
	
	//search
	public List<Contact> findByNameContainingAndUser(String name,User user);
	
	
	
}
