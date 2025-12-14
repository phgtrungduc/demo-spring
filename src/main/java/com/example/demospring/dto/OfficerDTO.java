package com.example.demospring.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(description = "Officer Data Transfer Object")
public class OfficerDTO {
    
    @Schema(description = "Unique identifier of the officer", example = "1")
    private Long officerId;
    
    @Schema(description = "First name of the officer", example = "John")
    private String firstName;
    
    @Schema(description = "Last name of the officer", example = "Doe")
    private String lastName;
    
    @Schema(description = "Job title of the officer", example = "Manager")
    private String title;
    
    @Schema(description = "Start date of the officer's employment")
    private Date startDate;
    
    @Schema(description = "End date of the officer's employment (null if currently employed)")
    private Date endDate;
    
    @Schema(description = "Customer ID associated with the officer", example = "100")
    private Long custId;
}

