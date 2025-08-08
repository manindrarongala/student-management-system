package com.manindra.management.Repo;

import com.manindra.management.Product.StudentDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepo  extends JpaRepository<StudentDetails, Integer> {

    @Query("SELECT s FROM StudentDetails s WHERE " +
            "LOWER(s.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.gender) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "CAST(s.phoneno AS string) LIKE %:query% OR " +
            "CAST(s.year AS string) LIKE %:query% OR " +
            "LOWER(s.branch) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(s.skill) LIKE LOWER(CONCAT('%', :query, '%'))")

    List<StudentDetails> searchAcrossFields(@Param("query") String query);

}
