package com.smart.dao;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.smart.entites.Myorder;
public interface MyorderRepository  extends JpaRepository<Myorder,Serializable>{
public Myorder findByOrderId(String orderId);
//public Page<Myorder> findOrderByUser(@Param("userId") int userId,Pageable Pageable);
 @Query("from Myorder as o where o.user.id=:userId")
public List<Myorder> findOrderByUser(@Param("userId") int userId);

}
