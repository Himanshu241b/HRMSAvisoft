package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.LeaveBalance;
import com.example.HRMSAvisoft.entity.LeaveType;
import com.example.HRMSAvisoft.repository.LeaveBalanceRepository;
import com.example.HRMSAvisoft.repository.LeaveTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private  final LeaveTypeRepository leaveTypeRepository;
    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepository,LeaveTypeRepository leaveTypeRepository){
        this.leaveBalanceRepository=leaveBalanceRepository;
        this.leaveTypeRepository=leaveTypeRepository;
    }
    @Transactional
    public void initializeLeaveBalancesForEmployee(Employee employee) {
        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (LeaveType leaveType : leaveTypes) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setEmployee(employee);
            leaveBalance.setLeaveType(leaveType);
            leaveBalance.setAccruedLeave(leaveType.getTotalLeaves());
            leaveBalance.setUsedLeave(0);
            leaveBalance.setCarryForward(0);
            leaveBalanceRepository.save(leaveBalance);
        }
    }
}
