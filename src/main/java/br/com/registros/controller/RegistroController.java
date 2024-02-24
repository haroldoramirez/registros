package br.com.registros.controller;

import br.com.registros.exception.RegraNegocioException;
import br.com.registros.model.dto.RegistroDTO;
import br.com.registros.model.dto.RegistroListDTO;
import br.com.registros.model.dto.TotalRegistrosDTO;
import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
import br.com.registros.model.enums.StatusRegistro;
import br.com.registros.service.RegistroService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/registros")
public class RegistroController {

    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "Titulo,Status,DataCadastro,Latitude,Longitude";

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

    @GetMapping("/exportar")
    public String paginaExportarRegistros() {
        return "registros/exportar";
    }

    @GetMapping("/exportarRegistros")
    public ResponseEntity<InputStreamResource> exportarRegistros() {

        try {

            List<Registro> registros = registroService.listarRegistrosPorDataDecrescente();

            // Criar o arquivo CSV em memória
            StringBuilder csvContent = new StringBuilder();
            csvContent.append(FILE_HEADER).append(NEW_LINE_SEPARATOR);

            for (Registro registro : registros) {
                csvContent.append(registro.getTitulo()).append(COMMA_DELIMITER)
                        .append(registro.getStatus().name()).append(COMMA_DELIMITER)
                        .append(registro.getDataCadastro()).append(COMMA_DELIMITER)
                        .append(registro.getLatitude()).append(COMMA_DELIMITER)
                        .append(registro.getLongitude()).append(NEW_LINE_SEPARATOR);
            }

            // Converter o conteúdo CSV em um InputStream
            InputStream inputStream = new ByteArrayInputStream(csvContent.toString().getBytes());

            // Configurar o cabeçalho da resposta HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "registros.csv");

            // Retornar a resposta com o arquivo CSV
            return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
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

    /**
     * metodo responsavel por modificar o titulo do arquivo
     *
     * @param str identificador
     * @return a string formatada
     */
    private static String formatarTitulo(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" ","-")
                .replaceAll(",", "-")
                .replaceAll("!", "")
                .replaceAll("/", "-")
                .replaceAll("[?]", "")
                .replaceAll("[%]", "")
                .replaceAll("[']", "")
                .replaceAll("[´]", "")
                .replaceAll("[`]", "")
                .replaceAll("[:]", "")
                .toLowerCase();
    }

}
