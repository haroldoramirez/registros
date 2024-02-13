package br.com.registros.model.repository;

import br.com.registros.model.entity.Registro;
import br.com.registros.model.entity.TotalRegistros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;


public interface RegistroRepository extends JpaRepository<Registro, Long> {

    @Query(value = "select * from registro as r where r.status = ?1 and DATE(r.data_cadastro) = ?2", nativeQuery = true)
    Registro findByStatusAndDataCadastro(String status, Date dataCadastro);

    @Query("SELECT NEW br.com.registros.model.entity.TotalRegistros(r.status as status, COUNT(*) as quantidade) FROM Registro as r GROUP BY r.status ORDER BY r.status ASC")
    List<TotalRegistros> contarRegistrosPorStatus();

    @Query(value = "select * from registro as r ORDER BY r.data_cadastro DESC", nativeQuery = true)
    List<Registro> listarRegistrosPorDataDecrescente();

}
