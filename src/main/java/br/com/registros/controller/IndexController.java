package br.com.registros.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController implements ErrorController {

    private final static String PATH = "/error";

    @RequestMapping(PATH)
    public String getErrorPath() {
        return "registros/error";
    }

}
