package com.example.HRMSAvisoft.service;

import com.example.HRMSAvisoft.dto.AddressDTO;
import com.example.HRMSAvisoft.entity.Address;
import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Zipcode;
import com.example.HRMSAvisoft.repository.AddressRepository;
import com.example.HRMSAvisoft.repository.EmployeeRepository;
import com.example.HRMSAvisoft.repository.ZipCodeRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    private AddressRepository addressRepository;
    private ZipCodeRepository zipcodeRepository;
    private EmployeeRepository employeeRepository;

    public AddressService(AddressRepository addressRepository,EmployeeRepository employeeRepository,ZipCodeRepository
                        zipcodeRepository){
        this.addressRepository=addressRepository;
        this.employeeRepository=employeeRepository;
        this.zipcodeRepository=zipcodeRepository;
    }
    public Employee addAddressToEmployee(Long employeeId, AddressDTO address) throws EmployeeService.EmployeeNotFoundException,AddressAlreadyPresentException {

        Address addAddress=new Address();
        addAddress.setPropertyNumber(address.getPropertyNumber());
        addAddress.setCountry(address.getCountry());
        Zipcode zipcode =new Zipcode();
        zipcode.setCity(address.getCity());
        zipcode.setState(address.getState());
        zipcode.setZipCode(address.getZipCode());
        zipcode= zipcodeRepository.save(zipcode);
        addAddress.setZipCode(zipcode);
        addAddress.setAddressType(address.getAddressType());

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeService.EmployeeNotFoundException( employeeId));
        if (employee.getAddresses().contains(addAddress)) {
            throw new AddressAlreadyPresentException();
        }

        Address newAddress =addressRepository.save(addAddress);
        employee.getAddresses().add(newAddress);

        return employeeRepository.save(employee);
    }
    public Employee removeAddressFromEmployee(Long employeeId, Long addressId) throws EmployeeService.EmployeeNotFoundException {
        // Retrieve the employee entity by its ID
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeService.EmployeeNotFoundException( employeeId));

        Address addressToRemove=addressRepository.findById(addressId)
                .orElseThrow(()->new EmployeeService.AddressNotFoundException(addressId));

        if (addressToRemove != null) {
            if (employee.getAddresses().contains(addressToRemove)) {
                employee.getAddresses().remove(addressToRemove);
            } else {
                throw new EmployeeService.AddressNotFoundException(employeeId,addressId);
            }
        } else {
            throw new EmployeeService.AddressNotFoundException(addressId);
        }

        return employeeRepository.save(employee);
    }

    public static class AddressAlreadyPresentException extends Exception {
        public AddressAlreadyPresentException() {
            super("Address is already associated with the employee." );
        }
    }
}
