package com.svengali.service;

import com.svengali.dto.HashCreateOrUpdateDTO;
import com.svengali.dto.HashDTO;
import com.svengali.dto.HashDeleteDTO;
import com.svengali.dto.HashSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HashService {
    HashDTO createOrUpdate(HashCreateOrUpdateDTO dto);
    void delete(HashDeleteDTO hash);
    Page<HashDTO> search(HashSearchDTO dto, Pageable pageable);
}
