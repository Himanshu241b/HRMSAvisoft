package com.example.HRMSAvisoft.controller;

import com.example.HRMSAvisoft.dto.CreatePerformanceDTO;
import com.example.HRMSAvisoft.dto.ErrorResponseDTO;
import com.example.HRMSAvisoft.entity.Performance;
import com.example.HRMSAvisoft.exception.EmployeeNotFoundException;
import com.example.HRMSAvisoft.service.DepartmentService;
import com.example.HRMSAvisoft.service.PerformanceService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/performance")
@Transactional
public class PerformanceController {

    private final PerformanceService performanceService;

    PerformanceController(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Performance>> getAllPerformanceOfEmployee(@PathVariable("employeeId") Long employeeId) throws EmployeeNotFoundException {
        List<Performance> performanceListOfEmployee = performanceService.getAllPerformanceOfEmployee(employeeId);
        return ResponseEntity.ok(performanceListOfEmployee);
    }

    @PostMapping("/addPerformance?employeeId={employeeId}&reviewerId={reviewerId}")
    public ResponseEntity<Map<String, Object>> addPerformanceOfEmployee(@PathParam("employeeId") Long employeeId, @PathParam("reviewerId") Long reviewerId, @RequestBody CreatePerformanceDTO createPerformanceDTO)throws EmployeeNotFoundException, IllegalAccessException{
        Performance newPerformanceRecord = performanceService.addPerformanceOfEmployee(employeeId, reviewerId, createPerformanceDTO);

        return ResponseEntity.status(201).body(Map.of("success", true, "message", "performance record added", "performance", newPerformanceRecord));
    }


    @ExceptionHandler({
            EmployeeNotFoundException.class,
    })

    public ResponseEntity<ErrorResponseDTO> handleErrors(Exception exception){
        String message;
        HttpStatus status;
        if(exception instanceof EmployeeNotFoundException) {
            message = exception.getMessage();
            status = HttpStatus.NOT_FOUND;
        }
        else if(exception instanceof IllegalAccessException){
            message = exception.getMessage();
            status = HttpStatus.FORBIDDEN;
        }
        else{
            message = "something went wrong";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ErrorResponseDTO errorResponse = ErrorResponseDTO.builder()
                .message(message)
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

}
