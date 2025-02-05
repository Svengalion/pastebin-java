package com.svengali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@NoArgsConstructor(force = true)
public class HashDTO {
    public final String hash;
    public final Boolean isUsed;
}
