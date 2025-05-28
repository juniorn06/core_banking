package com.core_banking.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "CONTA")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NUMERO_CONTA")
    private String numeroConta;

    @Column(name = "TIPO_CONTA")
    private String tipoConta;

    @Column(name = "SALDO")
    private BigDecimal saldo;

    @Column(name = "LIMITE")
    private BigDecimal limite;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transacao> transacoes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    @JsonIgnore
    private Cliente cliente;
}