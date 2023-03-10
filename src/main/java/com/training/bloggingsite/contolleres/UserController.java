package com.training.bloggingsite.contolleres;

import com.training.bloggingsite.dtos.UserDto;
import com.training.bloggingsite.services.interfaces.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(UserController.class);

    // Logging Page
    @GetMapping("/login")
    public String viewLogin(){
        return "login";
    }

    // Redirecting the USER to User-Dashboard.
    @GetMapping("/user/home")
    public String getUser(Principal principal, Model model){
        UserDto userDto = this.userService.findUserByEmail(principal.getName());
        model.addAttribute("name",userDto.getName());
        return "user-dashboard";
    }

    // Register User, default role will be USER.
    @GetMapping("/register")
    public ModelAndView getRegisterUser(){
        ModelAndView mav = new ModelAndView("register-user");
        mav.addObject("userData",new UserDto());
        return mav;
    }

    // Saving the User to database.
    @PostMapping("/register/save")
    public String postRegisterUser(@Valid @ModelAttribute("userData")UserDto userDto, BindingResult result){
        if(result.hasErrors()){
            logger.error(String.valueOf(result));
            return "redirect:/register";
        }
        this.userService.addUser(userDto);
        return "redirect:/login";
    }

}



