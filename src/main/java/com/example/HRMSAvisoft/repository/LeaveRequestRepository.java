package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveRequest;
import com.example.HRMSAvisoft.entity.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest,Long> {
    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);
    Page<LeaveRequest> findByEmployeeAndStatus(Employee employee, LeaveStatus status, Pageable pageable);
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.employee.id = :employeeId AND " +
            "(:startDate BETWEEN lr.startDate AND lr.endDate OR " +
            ":endDate BETWEEN lr.startDate AND lr.endDate OR " +
            "lr.startDate BETWEEN :startDate AND :endDate OR " +
            "lr.endDate BETWEEN :startDate AND :endDate)")
    List<LeaveRequest> findOverlappingLeaveRequests(@Param("employeeId") Long employeeId,
                                                    @Param("startDate") LocalDate startDate,
                                                    @Param("endDate") LocalDate endDate);

    Page<LeaveRequest> findByEmployee(Employee employee, Pageable pageable);



}
