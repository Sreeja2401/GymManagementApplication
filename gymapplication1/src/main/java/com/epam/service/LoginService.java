package com.epam.service;

import com.epam.dto.response.LoginCredentials;

public interface LoginService  {
	public void updateCredentials(LoginCredentials credentials, String newPassword);
}
