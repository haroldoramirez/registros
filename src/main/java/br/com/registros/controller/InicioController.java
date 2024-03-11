package br.com.registros.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class InicioController {
    @GetMapping("/")
    public String paginaCriar() {
        return "inicio";
    }
}
