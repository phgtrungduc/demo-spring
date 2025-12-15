package com.example.demospring.repository;

import com.example.demospring.entity.Officer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfficerRepository extends JpaRepository<Officer, Long> {
    
    List<Officer> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
    
    List<Officer> findByCustId(Long custId);
    
    @Query("SELECT o FROM Officer o WHERE o.firstName LIKE %:keyword% OR o.lastName LIKE %:keyword% OR o.title LIKE %:keyword%")
    List<Officer> searchByKeyword(@Param("keyword") String keyword);
}

