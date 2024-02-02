package br.com.registros.controller;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.dto.RegistroDTO;
import br.com.registros.model.dto.RegistroListDTO;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.enums.StatusRegistro;
import br.com.registros.service.RegistroService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static br.com.registros.model.enums.StatusRegistro.DIANA;
import static br.com.registros.model.enums.StatusRegistro.LIAM;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private final RegistroService registroService;

    private String formatoDesejado = "dd/MM/yyyy HH:mm:ss";

    public RegistroController(RegistroService registroService) {
        this.registroService = registroService;
    }

    @GetMapping("/criar")
    public String paginaCriar() {
        return "registros/criar";
    }

    @GetMapping("/listar")
    public ModelAndView paginaListar() {
        ModelAndView mv = new ModelAndView("registros/listar");
        List<Registro> registros = registroService.findAll();
        List<RegistroListDTO> registrosDTO = new ArrayList<>();

        for (Registro registro : registros) {
            RegistroListDTO registroDTO = new RegistroListDTO();

            registroDTO.setTitulo(registro.getTitulo());
            registroDTO.setDataCadastro(formatarLocalDateTime(registro.getDataCadastro(), formatoDesejado));

            registroDTO.setStatus(formatarStatus(registro.getStatus()));

            registrosDTO.add(registroDTO);
        }

        mv.addObject("registros", registrosDTO);
        return mv;
    }

    @GetMapping("/sucesso")
    public String paginaSucesso() {
        return "registros/sucesso";
    }

    @GetMapping("/erro")
    public String paginaErro() {
        return "registros/erro";
    }

    @PostMapping("/criar")
    public ModelAndView criar(RegistroDTO registroDTO) {

        final Registro registro = registroDTO.converterParaRegistro();

        try {

            ModelAndView mv = new ModelAndView("registros/sucesso");

            Registro registroSalvo = registroService.salvarRegistro(registro);

            mv.addObject("registroSalvo", registroSalvo);

            return mv;

        } catch (RegraNegocioException e) {

            ModelAndView mv = new ModelAndView("registros/erro");

            mv.addObject("mensagem", e.getMessage());

            return mv;

        }

    }

    private static String formatarLocalDateTime(LocalDateTime localDateTime, String formato) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);
        return localDateTime.format(formatter);
    }

    private static String formatarStatus(StatusRegistro status) {

        String textoStatus = "";

        textoStatus = switch (status) {
            case DIANA -> "Visita na casa da Diana";
            case LIAM -> "Visita na casa do Liam";
            default -> "Passeio";
        };

        return textoStatus;

    }

}
