package br.com.registros.controller;

import br.com.registros.model.Registro;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    @GetMapping("/create")
    public String pageCreate() {
        return "registros/create";
    }

    @GetMapping("/success")
    public String pageSuccess() {
        return "registros/success";
    }

    @PostMapping("/create")
    public String create(Registro registro) {
        System.out.println("Titulo do registro: " + registro.getTitulo());
        System.out.println("Status: " + registro.isStatus());
        return "redirect:/success";
    }

}
