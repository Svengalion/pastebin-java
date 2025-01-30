package com.svengali.service;

import com.svengali.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HashService {
    HashDTO create();
    HashDTO update(HashUpdateDTO dto);
    void delete(HashDeleteDTO hash);
    Page<HashDTO> search(HashSearchDTO dto, Pageable pageable);
}
