package com.svengali.dto;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public class HashDeleteDTO {
    public final String hash;
}
