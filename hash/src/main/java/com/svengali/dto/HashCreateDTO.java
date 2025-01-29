package com.svengali.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
public class HashCreateDTO {
    public final String hash;
    public final Boolean isUsed;
}
