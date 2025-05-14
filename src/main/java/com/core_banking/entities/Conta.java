package com.core_banking.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "conta")
public class Conta {

    @Id
    @Column(name = "NUMERO_CONTA")
    private Integer numeroConta;

    @Column(name = "TITULAR_CONTA")
    private String titularConta;

    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @Column(name = "SALDO")
    private BigDecimal saldo;

    @Column(name = "LIMITE")
    private Double limite;

    @OneToMany(mappedBy = "contaOrigem")
    private List<Transacao> transacoesEnviadas;

    @OneToMany(mappedBy = "contaDestino")
    private List<Transacao> transacoesRecebidas;
}