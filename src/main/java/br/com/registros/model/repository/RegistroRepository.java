package br.com.registros.model.repository;

import br.com.registros.model.entity.Registro;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RegistroRepository extends JpaRepository<Registro, Long> {

}
