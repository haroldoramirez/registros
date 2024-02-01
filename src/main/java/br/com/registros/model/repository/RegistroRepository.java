package br.com.registros.model.repository;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.enums.StatusRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query(value = "select * from registro as r where r.status = ?1", nativeQuery = true)
    List<Registro> findByStatus(String status);

}
