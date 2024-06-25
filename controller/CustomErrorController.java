/*package com.greenfinal.controller;

import com.greenfinal.service.UserinfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CustomErrorController implements ErrorController {
	
    @RequestMapping("/error")
    public ModelAndView handleError(@AuthenticationPrincipal User user){
        ModelAndView mav = new ModelAndView("error/error");
        return mav;
    }
}
*/