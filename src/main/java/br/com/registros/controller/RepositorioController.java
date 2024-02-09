package br.com.registros.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/repositorios")
public class RepositorioController {

    @GetMapping("/criar")
    public String paginaCriar() {
        return "repositorios/criar";
    }
}
