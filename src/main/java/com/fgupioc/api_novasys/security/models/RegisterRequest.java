package com.fgupioc.api_novasys.security.models;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RegisterRequest {
    private String name;
    private String lastname;
    private String document;
    private String phone;
    private String codStatus;
    private String password;
    private Long rolId;
}
