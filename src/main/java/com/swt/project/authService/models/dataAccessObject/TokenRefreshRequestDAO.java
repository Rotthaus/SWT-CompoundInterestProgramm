package com.swt.project.authService.models.dataAccessObject;

import lombok.Data;

@Data
public class TokenRefreshRequestDAO {

    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
