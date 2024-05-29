package com.example.HRMSAvisoft.repository;

import com.example.HRMSAvisoft.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, String > {
}
