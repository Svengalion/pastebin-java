package com.svengali.service;

import com.svengali.dto.HashDTO;
import com.svengali.dto.HashDeleteDTO;
import com.svengali.dto.HashSearchDTO;
import com.svengali.dto.HashUpdateDTO;
import com.svengali.jpa.models.Hash;
import com.svengali.jpa.repository.HashRepository;
import com.svengali.config.PostgresContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = com.svengali.HashApplication.class)
@ContextConfiguration(initializers = PostgresContainer.class)
@Transactional
public class HashServiceTest {

    @Autowired
    private HashService hashService;

    @Autowired
    private HashRepository hashRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private HashGenerator hashGenerator;

    @BeforeEach
    public void setUp() {
        hashRepository.deleteAll();
    }

    @Test
    @DisplayName("Создание нового хэша успешно")
    public void testCreateHash_Success() {
        HashDTO createdHash = hashService.create();
        assertNotNull(createdHash, "Созданный HashDTO не должен быть null");
        assertNotNull(createdHash.hash, "Поле hash не должно быть null");

        Hash savedHash = hashRepository.findByHash(createdHash.hash);
        assertNotNull(savedHash, "Сохраненный хэш не должен быть null");
        assertEquals(createdHash.hash, savedHash.getHash(), "Значение сохраненного хэша должно совпадать");
    }

    @Test
    @DisplayName("Обновление существующего хэша успешно")
    public void testUpdateHash_Success() {
        HashDTO createdHash = hashService.create();
        assertNotNull(createdHash, "Созданный HashDTO не должен быть null");

        Hash updateHash = modelMapper.map(createdHash, Hash.class);
        updateHash.setUsed(true);
        HashDTO updatedHash = hashService.update(modelMapper.map(updateHash, HashUpdateDTO.class));
        assertNotNull(updatedHash, "Обновленный HashDTO не должен быть null");
        assertEquals(createdHash.hash, updatedHash.hash, "Hash должен совпадать");

        Hash savedHash = hashRepository.findByHash(updatedHash.hash);
        assertNotNull(savedHash, "Сохраненный хэш не должен быть null");
    }

    @Test
    @DisplayName("Удаление существующего хэша успешно")
    public void testDeleteHash_Success() {
        HashDTO createdHash = hashService.create();
        assertNotNull(createdHash, "Созданный HashDTO не должен быть null");

        hashService.delete(modelMapper.map(createdHash, HashDeleteDTO.class));

        Hash deletedHash = hashRepository.findByHash(createdHash.hash);
        assertNull(deletedHash, "Хэш должен быть удален из базы данных");
    }

    @Test
    @DisplayName("Поиск хэшей по критериям успешно")
    public void testSearchHashes_Success() {
        HashDTO hash1 = hashService.create();
        HashDTO hash2 = hashService.create();

        HashSearchDTO searchDTO = null;
        searchDTO = modelMapper.map(hash1, HashSearchDTO.class);

        Page<HashDTO> result = hashService.search(searchDTO, Pageable.unpaged());

        assertNotNull(result, "Результат поиска не должен быть null");
        assertTrue(result.getTotalElements() == 1, "Должно быть найдено как минимум 2 хэша");

        for (HashDTO dto : result.getContent()) {
            assertTrue(dto.hash.contains(hash1.hash));
        }
    }

    @Test
    @DisplayName("Поиск хэшей без результатов")
    public void testSearchHashes_NoResults() {
        HashDTO hash = hashService.create();
        assertNotNull(hash, "Созданный HashDTO не должен быть null");

        HashSearchDTO searchDTO = modelMapper.map(hashService.create(), HashSearchDTO.class);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("hash").ascending());

        // Выполнение поиска через сервис
        Page<HashDTO> result = hashService.search(searchDTO, pageable);

        // Проверка результатов
        assertNotNull(result, "Результат поиска не должен быть null");
        assertEquals(0, result.getTotalElements(), "Должно быть найдено 0 хэшей");
    }
}
