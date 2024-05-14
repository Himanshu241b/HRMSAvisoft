package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.CreateDepartmentDTO;
import com.example.HRMSAvisoft.entity.Department;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.repository.DepartmentRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    private final EmployeeRepository employeeRepository;

    DepartmentService(DepartmentRepository departmentRepository, EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department addDepartment(CreateDepartmentDTO createDepartmentDTO) throws EmployeeService.EmployeeNotFoundException {
        Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(()-> new EmployeeService.EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
        Department newDepartment = new Department();
        newDepartment.setDepartment(createDepartmentDTO.getDepartment());
        newDepartment.setDescription(createDepartmentDTO.getDescription());
        newDepartment.setManager(manager);

        return departmentRepository.save(newDepartment);
    }

    public Department updateDepartment(CreateDepartmentDTO createDepartmentDTO, Long departmentId)throws  DepartmentNotFoundException, EmployeeService.EmployeeNotFoundException {
        Department departmentFoundById = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));

        if(createDepartmentDTO.getDepartment() != null){
            departmentFoundById.setDepartment(createDepartmentDTO.getDepartment());
        }
        if(createDepartmentDTO.getDescription() != null){
            departmentFoundById.setDescription(createDepartmentDTO.getDescription());
        }
        if(createDepartmentDTO.getManagerId() != null){
            Employee manager = employeeRepository.findById(createDepartmentDTO.getManagerId()).orElseThrow(()-> new EmployeeService.EmployeeNotFoundException(createDepartmentDTO.getManagerId()));
            departmentFoundById.setManager(manager);
        }
        return departmentRepository.save(departmentFoundById);
    }

    public void deleteDepartment(Long departmentId)throws DepartmentNotFoundException{
        Department departmentToDelete = departmentRepository.findById(departmentId).orElseThrow(()-> new DepartmentNotFoundException(departmentId));
        departmentRepository.delete(departmentToDelete);
    }

    public static class DepartmentNotFoundException extends RuntimeException {
        public DepartmentNotFoundException(Long departmentId) {
            super("Department with id " + departmentId+ " not found");
        }
    }
}
