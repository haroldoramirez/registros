package br.com.registros.controller;

import br.com.registros.model.Registro;
import br.com.registros.model.RegistroDTO;
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
    public String create(RegistroDTO registroDTO) {

        final Registro registro = registroDTO.converterParaRegistro();

        System.out.println("Criado objeto registro apartir do registroDTO");
        System.out.println("Título: " + registro.getTitulo());
        System.out.println("Status: " + registro.isStatus());

        return "redirect:/success";
    }

}
