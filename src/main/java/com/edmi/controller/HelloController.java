package com.edmi.controller;

import com.edmi.entity.Edmi_user;
import com.edmi.service.Edmi_userService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SessionAttributes(value={"user"},types={Edmi_user.class})
public class HelloController {

    @Autowired
    private Edmi_userService userServiceImp;

    @RequestMapping(value = "/login")
    public String login() {
        return "sign_in";
    }

    @RequestMapping(value = "/index")
    public ModelAndView  index(Edmi_user user) {
        Edmi_user edmi_user = userServiceImp.findByNameAndPassword(user.getName(),user.getPassword());
        ModelAndView mav = new ModelAndView("session");
        if(null!=edmi_user){
            mav.addObject("user",edmi_user);
            mav.setViewName("index");
        }else{
            mav.setViewName("login");
        }
        return mav;
    }

    @RequestMapping(value = "/welcome")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping(value = "/member-list")
    public String member_list() {


        return "member-list";
    }

    @RequestMapping(value = "/member-add")
    public String member_add() {
        return "member/member-add";
    }
}
