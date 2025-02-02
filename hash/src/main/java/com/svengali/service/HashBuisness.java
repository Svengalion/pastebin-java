package com.svengali.service;

import com.svengali.dto.*;
import com.svengali.jpa.models.Hash;
import com.svengali.jpa.repository.HashRepository;
import com.svengali.jpa.specification.HashSearchSpecification;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HashBuisness implements HashService {

    private final HashRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public HashDTO update(HashUpdateDTO dto) {
        return modelMapper.map(repository.save(modelMapper.map(dto, Hash.class)), HashDTO.class);
    }

    @Override
    public HashDTO create(HashCreateDTO dto) {
        return modelMapper.map(repository.save(modelMapper.map(dto, Hash.class)), HashDTO.class);
    }

    @Override
    public void delete(HashDeleteDTO deleteDTO) {
        repository.delete(repository.findByHash(deleteDTO.hash));
    }

    @Override
    public Page<HashDTO> search(HashSearchDTO dto, Pageable pageable) {
        return repository.findAll(new HashSearchSpecification(dto), pageable)
                .map(hash -> modelMapper.map(hash, HashDTO.class));
    }
}
