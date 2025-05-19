package com.core_banking.controllers;

import com.core_banking.entities.Conta;
import com.core_banking.repositories.ContaRepository;
import com.core_banking.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private ContaService contaService;

    @PostMapping(path = "/cadastrarConta")
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        contaService.cadastrarConta(conta);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(conta.getNumeroConta()).toUri();
        return ResponseEntity.created(uri).build();
    }
}