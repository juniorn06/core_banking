package com.core_banking.repositories;

import com.core_banking.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeIsContainingIgnoreCase(String nome);
}
