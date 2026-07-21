package com.fgupioc.api_novasys.security.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class UpdateRequest extends RegisterRequest {
    private Long id;
}
