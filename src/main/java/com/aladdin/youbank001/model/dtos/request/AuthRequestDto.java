package com.aladdin.youbank001.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthRequestDto {

    private String email;
    private String password;

}
