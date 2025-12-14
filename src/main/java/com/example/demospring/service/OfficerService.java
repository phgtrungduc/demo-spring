package com.example.demospring.service;

import com.example.demospring.dto.OfficerDTO;
import com.example.demospring.entity.Officer;
import com.example.demospring.repository.OfficerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficerService {
    
    private final OfficerRepository officerRepository;
    
    @Transactional(readOnly = true)
    public List<OfficerDTO> findAllOfficers() {
        return officerRepository.findAll()
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<OfficerDTO> findOfficerById(Long id) {
        return officerRepository.findById(id)
            .map(this::convertToDTO);
    }
    
    @Transactional(readOnly = true)
    public List<OfficerDTO> findOfficersByCustId(Long custId) {
        return officerRepository.findByCustId(custId)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<OfficerDTO> searchOfficers(String keyword) {
        return officerRepository.searchByKeyword(keyword)
            .stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    private OfficerDTO convertToDTO(Officer officer) {
        OfficerDTO dto = new OfficerDTO();
        dto.setOfficerId(officer.getOfficerId());
        dto.setFirstName(officer.getFirstName());
        dto.setLastName(officer.getLastName());
        dto.setTitle(officer.getTitle());
        dto.setStartDate(officer.getStartDate());
        dto.setEndDate(officer.getEndDate());
        dto.setCustId(officer.getCustId());
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveOfficerOptional(Officer officer) {
        var officer new Officer()
        return
    }
}

