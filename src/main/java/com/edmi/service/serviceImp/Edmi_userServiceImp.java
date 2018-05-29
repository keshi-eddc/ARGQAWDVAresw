package com.edmi.service.serviceImp;

import com.edmi.dao.Edmi_userRepository;
import com.edmi.entity.Edmi_user;
import com.edmi.service.Edmi_userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userService")
public class Edmi_userServiceImp implements Edmi_userService {

    @Autowired
    private Edmi_userRepository userDao;

    @Override
    public Edmi_user findByNameAndPassword(String name, String password) {
        return userDao.findByNameAndPassword(name,password);
    }
}
