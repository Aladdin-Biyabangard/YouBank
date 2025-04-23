package com.aladdin.youbank001.model.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerDto {

    private String fullName;

    private String phoneNumber;

    private String address;

    private String fin;

}
