package com.fgupioc.api_novasys.security.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserView {
    private Long id;
    private String document;
    private String firstName;
    private String lastName;
    private Long roleId;
}
