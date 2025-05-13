package com.core_banking.services;

import com.core_banking.DTO.ResponseDTO;
import com.core_banking.entities.Conta;
import com.core_banking.exceptions.ValidarException;
import com.core_banking.repositories.ContaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContaService {

    private ContaRepository contaRepository;

    public ResponseEntity<ResponseDTO> cadastrarConta(Conta novaConta) {
        try {
            validarConta(novaConta);
            Conta contaSalva = contaRepository.save(novaConta);
            String MsgDTO = "Conta cadastrada com sucesso!";
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(contaSalva, MsgDTO));
        } catch (ValidarException e) {
            String MsgDTO = e.getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseDTO(null, MsgDTO));
        } catch (Exception e){
            String MsgDTO = "Erro inesperado. Entre em contato com o Administrador.";
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseDTO(null, MsgDTO));
        }
    }

    public void deposito(Long contaId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("Valor do depósito deve ser positivo.");
        }
        Conta conta = contaRepository.findById(contaId).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        BigDecimal novoSaldo = conta.getSaldo().add(valor);
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
    }

    public void sacar(Long contaId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("Valor do saque deve ser positivo.");
        }
        Conta conta = contaRepository.findById(contaId).orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        BigDecimal novoSaldo = conta.getSaldo().subtract(valor);
        conta.setSaldo(novoSaldo);
        contaRepository.save(conta);
    }

    private void validarConta(Conta conta) {
        if (conta.getNumeroConta() == null) {
            throw new RuntimeException("Número da conta não pode ser nulo!");
        }
        if (conta.getNumeroConta() < 0) {
            throw new RuntimeException("Número da conta não pode ser negativo!");
        }
        if (conta.getTitularConta() == null) {
            throw new RuntimeException("Titular da conta não pode ser nulo!");
        }
        if (conta.getTipoConta() == null) {
            throw new RuntimeException("Tipo da conta não pode ser nulo!");
        }
        if (conta.getLimite() == null || conta.getLimite() < 0) {
            throw new RuntimeException("Limite da conta não pode ser nulo ou negativo!");
        }
    }
}
