package br.com.registros.controller;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.dto.RegistroDTO;
import br.com.registros.model.dto.RegistroListDTO;
import br.com.registros.model.dto.TotalRegistrosDTO;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
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

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private final RegistroService registroService;

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
        List<Registro> registros = registroService.listarRegistrosPorDataDecrescente();
        List<RegistroListDTO> registrosDTO = new ArrayList<>();
        List<TotalRegistrosDTO> totalRegistrosDTO = new ArrayList<>();

        List<TotalRegistros> registrosContabilizados = registroService.contarRegistrosPorStatus();

        for (TotalRegistros registroContabilizados : registrosContabilizados) {

            TotalRegistrosDTO totalRegistroDTO = new TotalRegistrosDTO();

            totalRegistroDTO.setStatus(formatarStatusLista(registroContabilizados.getStatus()));
            totalRegistroDTO.setQuantidade(registroContabilizados.getQuantidade());
            totalRegistrosDTO.add(totalRegistroDTO);

        }

        for (Registro registro : registros) {
            RegistroListDTO registroDTO = new RegistroListDTO();

            registroDTO.setTitulo(registro.getTitulo());
            String formatoDesejado = "dd/MM/yyyy HH:mm:ss";
            registroDTO.setDataCadastro(formatarLocalDateTime(registro.getDataCadastro(), formatoDesejado));

            registroDTO.setTextoStatus(formatarStatus(registro.getStatus()));

            if (registro.getStatus() == StatusRegistro.DIANA) {
                registroDTO.setCodigoStatus(0);
            } else if (registro.getStatus() == StatusRegistro.LIAM) {
                registroDTO.setCodigoStatus(1);
            } else {
                registroDTO.setCodigoStatus(2);
            }

            registrosDTO.add(registroDTO);
        }

        mv.addObject("totalQuantidadeStatus", totalRegistrosDTO);
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

    private static String formatarStatusLista(StatusRegistro status) {

        String textoStatus = "";

        textoStatus = switch (status) {
            case DIANA -> "Visitas na casa da Diana";
            case LIAM -> "Visitas na casa do Liam";
            default -> "Passeios juntos";
        };

        return textoStatus;

    }

}
