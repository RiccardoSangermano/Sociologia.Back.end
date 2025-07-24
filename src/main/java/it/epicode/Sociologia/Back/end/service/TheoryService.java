package it.epicode.Sociologia.Back.end.service;

import it.epicode.Sociologia.Back.end.exeption.ResourceNotFoundException;
import it.epicode.Sociologia.Back.end.model.Theory;
import it.epicode.Sociologia.Back.end.dto.TheoryDto;
import it.epicode.Sociologia.Back.end.dto.TheoryPagedResponseDto;
import it.epicode.Sociologia.Back.end.dto.TheoryResponseDto;
import it.epicode.Sociologia.Back.end.repository.TheoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TheoryService {

    @Autowired
    private TheoryRepository theoryRepository;

    private Theory mapToEntity(TheoryDto theoryDto) {
        Theory theory = new Theory();
        if (theoryDto.getId() != null) {
            theory.setId(theoryDto.getId());
        }
        theory.setNomeTeoria(theoryDto.getNomeTeoria());
        theory.setAutore(theoryDto.getAutore());
        theory.setImmagineAutoreUrl(theoryDto.getImmagineAutoreUrl());
        theory.setSpiegazione(theoryDto.getSpiegazione());
        theory.setEsempioApplicazioneModerna(theoryDto.getEsempioApplicazioneModerna());
        return theory;
    }

    private TheoryResponseDto mapToDto(Theory theory) {
        TheoryResponseDto dto = new TheoryResponseDto();
        dto.setId(theory.getId());
        dto.setNomeTeoria(theory.getNomeTeoria());
        dto.setAutore(theory.getAutore());
        dto.setImmagineAutoreUrl(theory.getImmagineAutoreUrl());
        dto.setSpiegazione(theory.getSpiegazione());
        dto.setEsempioApplicazioneModerna(theory.getEsempioApplicazioneModerna());
        return dto;
    }


    public List<TheoryResponseDto> getAllTeorie() {
        List<Theory> theories = theoryRepository.findAll();
        return theories.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    public TheoryResponseDto getTeoriaById(Long id) {
        Theory theory = theoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theory", "id", id));
        return mapToDto(theory);
    }

    public TheoryResponseDto createTeoria(TheoryDto theoryDto) {
        Theory theory = mapToEntity(theoryDto);
        Theory newTheory = theoryRepository.save(theory);
        return mapToDto(newTheory);
    }

    public TheoryResponseDto updateTeoria(Long id, TheoryDto theoryDto) {
        Theory theory = theoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theory", "id", id));

        theory.setNomeTeoria(theoryDto.getNomeTeoria());
        theory.setAutore(theoryDto.getAutore());
        theory.setImmagineAutoreUrl(theoryDto.getImmagineAutoreUrl());
        theory.setSpiegazione(theoryDto.getSpiegazione());
        theory.setEsempioApplicazioneModerna(theoryDto.getEsempioApplicazioneModerna());

        Theory updatedTheory = theoryRepository.save(theory);
        return mapToDto(updatedTheory);
    }

    public void deleteTeoria(Long id) {
        Theory theory = theoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Theory", "id", id));
        theoryRepository.delete(theory);
    }

    public TheoryPagedResponseDto getTeorie(
            String keyword,
            int pageNo,
            int pageSize,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Theory> theoriesPage;

        if (keyword != null && !keyword.trim().isEmpty()) {
            theoriesPage = theoryRepository.searchAllFields(keyword, pageable);
        } else {
            theoriesPage = theoryRepository.findAll(pageable);
        }

        List<TheoryResponseDto> content = theoriesPage.getContent().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        TheoryPagedResponseDto theoryPagedResponse = new TheoryPagedResponseDto();
        theoryPagedResponse.setContent(content);
        theoryPagedResponse.setPageNo(theoriesPage.getNumber());
        theoryPagedResponse.setPageSize(theoriesPage.getSize());
        theoryPagedResponse.setTotalElements(theoriesPage.getTotalElements());
        theoryPagedResponse.setTotalPages(theoriesPage.getTotalPages());
        theoryPagedResponse.setLast(theoriesPage.isLast());

        return theoryPagedResponse;
    }
}
