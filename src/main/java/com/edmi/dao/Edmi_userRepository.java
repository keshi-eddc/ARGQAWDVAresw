package com.edmi.dao;

import com.edmi.entity.Edmi_user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Edmi_userRepository extends JpaRepository<Edmi_user,Long> {


    Edmi_user findByNameAndPassword(String name, String password);


}
