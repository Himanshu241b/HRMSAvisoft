package com.example.HRMSAvisoft.dto;

import com.example.HRMSAvisoft.entity.Employee;
import com.example.HRMSAvisoft.entity.Rating;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePerformanceDTO {

    private String reviewDate;

    private Rating rating;

    private String comment;

}
