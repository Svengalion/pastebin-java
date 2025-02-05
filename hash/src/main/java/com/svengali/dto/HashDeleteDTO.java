package com.svengali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@NoArgsConstructor(force = true)
public class HashDeleteDTO {
    public final String hash;
}
