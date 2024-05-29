package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveRequest;
import com.example.HRMSAvisoft.entity.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {
    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
    Page<LeaveRequest> findByEmployeeAndStatus(Employee employee, LeaveStatus status, Pageable pageable);


}
