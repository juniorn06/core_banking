package com.core_banking.entities;

import com.core_banking.enums.EnumTipoTransacao;
import com.core_banking.enums.EnumTipoTransferencia;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Entity(name = "TRANSACAO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "TIPO_TRANSACAO")
    @Enumerated(EnumType.STRING)
    private EnumTipoTransacao tipoTransacao;

    @Column(name = "TIPO_TRANSFERENCIA")
    @Enumerated(EnumType.STRING)
    private EnumTipoTransferencia tipoTransferencia;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Builder.Default
    @Column(name = "DATA_HORA")
    private LocalDateTime dataHora = LocalDateTime.now();

    @ManyToOne
    private Conta conta;
}
