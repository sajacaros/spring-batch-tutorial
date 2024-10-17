package com.study.springbatch.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DatabaseConfig {
    String url;
    String username;
    String password;
    String filePath;
}
