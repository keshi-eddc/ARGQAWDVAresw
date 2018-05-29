package com.edmi.service;

import com.edmi.entity.Edmi_user;

public interface Edmi_userService {
    Edmi_user findByNameAndPassword(String name, String password);
}
