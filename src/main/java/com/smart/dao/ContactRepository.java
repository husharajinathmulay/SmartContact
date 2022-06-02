package com.smart.dao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.smart.entites.Contact;
import com.smart.entites.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.io.Serializable;
import java.util.List;
public interface ContactRepository extends JpaRepository<Contact,Serializable>{
	@Query("from Contact as c where c.user.id=:userId")
	//current page
	//contact per page
	public Page<Contact> findContactByUser(@Param("userId") int userId,Pageable Pageable);
	public List<Contact> findByNameAndUser(String name,User user);
	//public List<Contact> findByNameContainingAndUser(String name,User user);
}
