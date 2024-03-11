package br.com.registros.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/contato")
public class ContatoController {

    @GetMapping("/criar")
    public String paginaCriar() {
        return "contato";
    }
    
}
