package com.svengali.service;

import com.svengali.dto.HashDTO;
import com.svengali.jpa.models.Hash;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.UUID;

@AllArgsConstructor
@Service
public class HashGeneratorImpl implements HashGenerator {

    private final ModelMapper modelMapper;

    @Override
    public HashDTO generate() {
        String uuid = UUID.randomUUID().toString();
        String hash = sha256(uuid).substring(0, 10);
        Hash hashModel = new Hash();
        hashModel.setHash(hash);
        hashModel.setUsed(false);
        return modelMapper.map(hashModel, HashDTO.class);
    }

    private static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return HexFormat.of().formatHex(hash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
