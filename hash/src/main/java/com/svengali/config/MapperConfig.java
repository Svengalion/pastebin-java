package com.svengali.config;

import com.svengali.dto.HashDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Используем правильный интерфейс Provider
        Provider<Object> customProvider = request -> {
            Class<?> destinationType = request.getRequestedType();
            if (destinationType.equals(HashDTO.class)) {
                // Убедись, что у HashDTO есть конструктор с двумя аргументами
                return new HashDTO(null, null);
            }
            try {
                return destinationType.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать объект для " + destinationType, e);
            }
        };

        mapper.getConfiguration().setProvider(customProvider);

        return mapper;
    }
}