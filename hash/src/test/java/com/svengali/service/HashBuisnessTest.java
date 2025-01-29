package com.svengali.service;

import com.svengali.dto.*;
import com.svengali.jpa.models.Hash;
import com.svengali.jpa.repository.HashRepository;
import com.svengali.jpa.specification.HashSearchSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HashBuisnessTest {

    private HashRepository repository;
    private ModelMapper modelMapper;
    private HashBuisness HashBuisness;

    @BeforeEach
    void setUp() {
        repository = mock(HashRepository.class);
        modelMapper = mock(ModelMapper.class);
        HashBuisness = new HashBuisness(repository, modelMapper);
    }

    @Test
    void create_ShouldReturnHashDTO_WhenInputIsValid() {
        // Arrange
        HashCreateDTO createDTO = HashCreateDTO.builder()
                .hash("sampleHash")
                .isUsed(false)
                .build();

        Hash hashEntity = new Hash();
        hashEntity.setHash("sampleHash");
        hashEntity.setUsed(false);

        Hash savedHash = new Hash();
        savedHash.setHash("sampleHash");
        savedHash.setUsed(false);

        HashDTO hashDTO = HashDTO.builder()
                .hash("sampleHash")
                .isUsed(false)
                .build();

        when(modelMapper.map(createDTO, Hash.class)).thenReturn(hashEntity);
        when(repository.save(hashEntity)).thenReturn(savedHash);
        when(modelMapper.map(savedHash, HashDTO.class)).thenReturn(hashDTO);

        // Act
        HashDTO result = HashBuisness.create(createDTO);

        // Assert
        assertNotNull(result);
        assertEquals("sampleHash", result.getHash());
        assertFalse(result.getIsUsed());

        verify(modelMapper, times(1)).map(createDTO, Hash.class);
        verify(repository, times(1)).save(hashEntity);
        verify(modelMapper, times(1)).map(savedHash, HashDTO.class);
    }

    @Test
    void update_ShouldReturnUpdatedHashDTO_WhenHashExists() {
        // Arrange
        HashUpdateDTO updateDTO = HashUpdateDTO.builder()
                .hash("existingHash")
                .isUsed(true)
                .build();

        Hash existingHash = new Hash();
        existingHash.setHash("existingHash");
        existingHash.setUsed(false);

        Hash updatedHash = new Hash();
        updatedHash.setHash("existingHash");
        updatedHash.setUsed(true);

        HashDTO hashDTO = HashDTO.builder()
                .hash("existingHash")
                .isUsed(true)
                .build();

        when(repository.findByHash("existingHash")).thenReturn(existingHash);
        when(repository.save(existingHash)).thenReturn(updatedHash);
        when(modelMapper.map(updatedHash, HashDTO.class)).thenReturn(hashDTO);

        // Act
        HashDTO result = HashBuisness.update(updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("existingHash", result.getHash());
        assertTrue(result.getIsUsed());

        verify(repository, times(1)).findByHash("existingHash");
        verify(repository, times(1)).save(existingHash);
        verify(modelMapper, times(1)).map(updatedHash, HashDTO.class);
    }

    @Test
    void update_ShouldThrowHashNotFoundException_WhenHashDoesNotExist() {
        // Arrange
        HashUpdateDTO updateDTO = HashUpdateDTO.builder()
                .hash("nonExistingHash")
                .isUsed(true)
                .build();

        when(repository.findByHash("nonExistingHash")).thenReturn(null);

        // Act & Assert
        HashNotFoundException exception = assertThrows(HashNotFoundException.class, () -> {
            HashBuisness.update(updateDTO);
        });

        assertEquals("Hash not found: nonExistingHash", exception.getMessage());

        verify(repository, times(1)).findByHash("nonExistingHash");
        verify(repository, never()).save(any(Hash.class));
        verify(modelMapper, never()).map(any(), eq(HashDTO.class));
    }

    @Test
    void delete_ShouldInvokeRepositoryDelete_WhenHashExists() {
        // Arrange
        HashDeleteDTO deleteDTO = HashDeleteDTO.builder()
                .hash("hashToDelete")
                .build();

        Hash existingHash = new Hash();
        existingHash.setHash("hashToDelete");
        existingHash.setUsed(false);

        when(repository.findByHash("hashToDelete")).thenReturn(existingHash);
        doNothing().when(repository).delete(existingHash);

        // Act
        HashBuisness.delete(deleteDTO);

        // Assert
        verify(repository, times(1)).findByHash("hashToDelete");
        verify(repository, times(1)).delete(existingHash);
    }

    @Test
    void delete_ShouldThrowHashNotFoundException_WhenHashDoesNotExist() {
        // Arrange
        HashDeleteDTO deleteDTO = HashDeleteDTO.builder()
                .hash("nonExistingHash")
                .build();

        when(repository.findByHash("nonExistingHash")).thenReturn(null);

        // Act & Assert
        HashNotFoundException exception = assertThrows(HashNotFoundException.class, () -> {
            HashBuisness.delete(deleteDTO);
        });

        assertEquals("Hash not found: nonExistingHash", exception.getMessage());

        verify(repository, times(1)).findByHash("nonExistingHash");
        verify(repository, never()).delete(any(Hash.class));
    }

    @Test
    void search_ShouldReturnPageOfHashDTO_WhenSearchCriteriaIsProvided() {
        // Arrange
        HashSearchDTO searchDTO = HashSearchDTO.builder()
                .hash("searchHash")
                .isUsed(true)
                .build();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("hash").ascending());

        Hash hash1 = new Hash();
        hash1.setHash("searchHash1");
        hash1.setUsed(true);

        Hash hash2 = new Hash();
        hash2.setHash("searchHash2");
        hash2.setUsed(true);

        List<Hash> hashList = Arrays.asList(hash1, hash2);
        Page<Hash> hashPage = new PageImpl<>(hashList, pageable, hashList.size());

        HashDTO dto1 = HashDTO.builder()
                .hash("searchHash1")
                .isUsed(true)
                .build();

        HashDTO dto2 = HashDTO.builder()
                .hash("searchHash2")
                .isUsed(true)
                .build();

        when(repository.findAll(any(HashSearchSpecification.class), eq(pageable))).thenReturn(hashPage);
        when(modelMapper.map(hash1, HashDTO.class)).thenReturn(dto1);
        when(modelMapper.map(hash2, HashDTO.class)).thenReturn(dto2);

        // Act
        Page<HashDTO> result = HashBuisness.search(searchDTO, pageable);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("searchHash1", result.getContent().get(0).getHash());
        assertEquals("searchHash2", result.getContent().get(1).getHash());

        verify(repository, times(1)).findAll(any(HashSearchSpecification.class), eq(pageable));
        verify(modelMapper, times(1)).map(hash1, HashDTO.class);
        verify(modelMapper, times(1)).map(hash2, HashDTO.class);
    }
}
