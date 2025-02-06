package com.svengali.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
public class HashUpdateDTO {
    public final String hash;
    public final Boolean isUsed;
}
