package com.svengali.dto;

import lombok.Builder;

@Builder
public class HashCreateOrUpdateDTO {
    public final String hash;
    public final Boolean isUsed;
}
