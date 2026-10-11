package com.az9374.eduadmin.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginOption {
    private Integer id;
    private String username;
    private String name;
    private String token;
}
