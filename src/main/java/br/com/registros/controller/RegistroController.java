package br.com.registros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistroController {

    @GetMapping("/create")
    public String create() {
        return "registros/create";
    }

}
