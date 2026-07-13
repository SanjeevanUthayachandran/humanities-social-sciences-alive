package com.hssa.auth.repository; 

import com.hssa.auth.entity.User; 
import org.springframework.data.jpa.repository.JpaRepository; 
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional; 
import java.util.UUID; 

public interface UserRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> { 
    Optional<User> findByEmail(String email); 
    Optional<User> findByEmployeeNumber(String employeeNumber); 
    Optional<User> findByStudentNumber(String studentNumber); 
    boolean existsByEmail(String email); 
    boolean existsByEmployeeNumber(String employeeNumber); 
    boolean existsByStudentNumber(String studentNumber); 
}
