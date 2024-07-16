package br.com.registros.api.resources;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/campanhas")
@CrossOrigin(origins="*")
public class CampanhasResources {
	
    //GET
    @GetMapping
    public ResponseEntity<String> campanhas(@RequestParam(value = "numSerie", required = true) String numSerie,
    		@RequestParam(value = "numCupom", required = true) String numCupom) {

        return ResponseEntity.ok("Ok Serie: " + numSerie + " Cupom: " + numCupom);
        
    }

}
