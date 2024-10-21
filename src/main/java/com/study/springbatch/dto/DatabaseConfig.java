package com.study.springbatch.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DatabaseConfig {
    @NotEmpty(message = "url은 필수입니다.")
    String url;
    @NotEmpty(message = "username은 필수입니다.")
    String username;
    @NotEmpty(message = "password은 필수입니다.")
    String password;
    @NotEmpty(message = "filePath은 필수입니다.")
    String filePath;
    @NotEmpty(message = "schema는 필수입니다.")
    String schema;
}
