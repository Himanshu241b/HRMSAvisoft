package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee getByEmployeeId(Long employeeId);
}
