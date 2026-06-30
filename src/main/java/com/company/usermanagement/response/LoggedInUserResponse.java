package com.company.usermanagement.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoggedInUserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String role;
}