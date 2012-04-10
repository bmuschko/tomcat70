package org.apache.tomcat.spring.example;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {
    @RequestMapping(value = "/index.htm")
    public ModelAndView indexPage() {
        ModelAndView modelAndView = new ModelAndView("/WEB-INF/jsp/index.jsp");
        modelAndView.addObject("var", "Hello!");
        return modelAndView;
    }
}