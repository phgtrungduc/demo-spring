package com.example.demospring.controller;

import com.example.demospring.dto.OfficerDTO;
import com.example.demospring.service.OfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/officers")
@RequiredArgsConstructor
@Tag(name = "Officer Management", description = "APIs for managing officers")
public class OfficerController {
    
    private final OfficerService officerService;

    /**
     * Test endpoint để kiểm tra API hoạt động và kết nối Oracle database
     */
    @Operation(summary = "Test API Connection", description = "Check if API is working and database connection is successful")
    @ApiResponse(responseCode = "200", description = "API is working successfully")
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working! Connection to Oracle database is successful.");
    }

    /**
     * Lấy danh sách tất cả officers
     */
    @Operation(summary = "Get All Officers", description = "Retrieve a list of all officers")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of officers")
    @GetMapping
    public ResponseEntity<List<OfficerDTO>> getAllOfficers() {
        List<OfficerDTO> officers = officerService.findAllOfficers();
        return ResponseEntity.ok(officers);
    }

    /**
     * Lấy thông tin officer theo ID
     */
    @Operation(summary = "Get Officer by ID", description = "Retrieve officer information by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Officer found successfully"),
        @ApiResponse(responseCode = "404", description = "Officer not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<OfficerDTO> getOfficerById(
            @Parameter(description = "ID of the officer to retrieve", required = true)
            @PathVariable Long id) {
        return officerService.findOfficerById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Lấy danh sách officers theo Customer ID
     */
    @Operation(summary = "Get Officers by Customer ID", description = "Retrieve all officers associated with a specific customer")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved officers for the customer")
    @GetMapping("/customer/{custId}")
    public ResponseEntity<List<OfficerDTO>> getOfficersByCustomerId(
            @Parameter(description = "Customer ID to filter officers", required = true)
            @PathVariable Long custId) {
        List<OfficerDTO> officers = officerService.findOfficersByCustId(custId);
        return ResponseEntity.ok(officers);
    }

    /**
     * Tìm kiếm officer theo keyword (firstName, lastName, title)
     */
    @Operation(summary = "Search Officers", description = "Search officers by keyword (firstName, lastName, or title)")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    @GetMapping("/search")
    public ResponseEntity<List<OfficerDTO>> searchOfficers(
            @Parameter(description = "Keyword to search in firstName, lastName, or title", required = true)
            @RequestParam String keyword) {
        List<OfficerDTO> officers = officerService.searchOfficers(keyword);
        return ResponseEntity.ok(officers);
    }
}

