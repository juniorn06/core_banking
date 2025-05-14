package com.core_banking.repositories;

import com.core_banking.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.contaOrigem.id = :contaId OR t.contaDestino.id = :contaId ORDER BY t.dataHora DESC")
    List<Transacao> findByConta(@Param("contaId") Long contaId);
}
