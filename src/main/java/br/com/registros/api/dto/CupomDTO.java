package br.com.registros.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CupomDTO {
	
	private String numSerie;
	private String numCupom;

}
