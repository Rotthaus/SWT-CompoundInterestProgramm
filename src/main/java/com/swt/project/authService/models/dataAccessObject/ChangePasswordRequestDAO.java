package com.swt.project.authService.models.dataAccessObject;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChangePasswordRequestDAO {
    String oldPassword;
    String newPassword;

}
