package br.com.registros.model.entity;

import br.com.registros.model.enums.StatusRegistro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 150)
    private String titulo;

    @Column(name = "data_cadastro", updatable = false)
    private LocalDateTime dataCadastro;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private StatusRegistro status;

    @PrePersist
    public void prePersist() {
        setDataCadastro(LocalDateTime.now());
    }

}
