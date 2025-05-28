package com.core_banking.services;

import com.core_banking.DTO.ResponseDTO;
import com.core_banking.entities.Conta;
import com.core_banking.entities.Transacao;
import com.core_banking.enums.EnumTipoTransacao;
import com.core_banking.enums.EnumTipoTransferencia;
import com.core_banking.exceptions.ValidarException;
import com.core_banking.repositories.ContaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    @Autowired
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

    public List<Conta> obterContas(){
        List<Conta> listarContas = contaRepository.findAll();
        return listarContas;
    }

    public Conta obterContaPorId(Long id) {
        return contaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conta não encontrada"));
    }

    public Conta alterarConta(Conta contaAlterada, Long id) {
        return contaRepository.findById(id).map(conta -> {
            conta.setNumeroConta(contaAlterada.getNumeroConta());
            conta.setTipoConta(contaAlterada.getTipoConta());
            conta.setLimite(contaAlterada.getLimite());
            return contaRepository.save(conta);
        }).orElseThrow(() -> new RuntimeException("Conta não encontrada!"));
    }

    public void depositar(Long contaId, BigDecimal valor) {
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

    public BigDecimal consultarSaldo(Long contaId) {
        Conta conta = contaRepository.findById(contaId).orElseThrow(() -> new EntityNotFoundException("Conta inexistente" + contaId));
        return conta.getSaldo();
    }

    public Conta extrato(Long contaId) {
        return contaRepository.findById(contaId).orElseThrow(() -> new EntityNotFoundException("Conta inexistente"));
    }

    public BigDecimal transferirValor(Long contaOrigemId, Long contaDestinoId, BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0 ) {
            throw new IllegalArgumentException("Valor da transferência deve ser positivo.");
        }
        Conta contaOrigem = contaRepository.findById(contaOrigemId).orElseThrow(() -> new EntityNotFoundException("Conta de origem inexistente" + contaOrigemId));
        Conta contaDestino = contaRepository.findById(contaDestinoId).orElseThrow(() -> new EntityNotFoundException("Conta de origem inexistente" + contaDestinoId));

        if (valor.compareTo(contaOrigem.getSaldo()) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente na conta de origem.");
        }

        contaOrigem.setSaldo(valor.subtract(contaOrigem.getSaldo()));
        contaDestino.setSaldo(valor.add(contaOrigem.getSaldo()));

        Transacao transacaoSaida = buildTransacao(valor, contaOrigem, EnumTipoTransferencia.SAIDA, EnumTipoTransacao.TRANSFERENCIA);

        contaOrigem.getTransacoes().add(transacaoSaida);

        Transacao transacaoEntrada = buildTransacao(valor, contaDestino, EnumTipoTransferencia.ENTRADA, EnumTipoTransacao.TRANSFERENCIA);

        contaDestino.getTransacoes().add(transacaoEntrada);

        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        return valor;
    }

    private Transacao buildTransacao(BigDecimal valor, Conta conta, EnumTipoTransferencia tipoTransferencia, EnumTipoTransacao tipoTransacao) {
        return Transacao.builder()
                .conta(conta)
                .tipoTransferencia(tipoTransferencia)
                .valor(valor)
                .tipoTransacao(tipoTransacao)
                .build();
    }

    private void validarConta(Conta conta) {
        if (conta.getNumeroConta() == null) {
            throw new RuntimeException("O número da conta não pode ser nulo!");
        }
        if (conta.getTipoConta() == null) {
            throw new RuntimeException("Campo tipo da conta não pode ser nulo!");
        }
        if (conta.getLimite() == null || conta.getLimite().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Limite da conta não pode ser nulo ou negativo!");
        }
        if (conta.getCliente() == null) {
            throw new RuntimeException("Campo cliente não pode ser nulo!");
        }
    }
}
