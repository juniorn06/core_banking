package com.core_banking.controllers;

import com.core_banking.entities.Conta;
import com.core_banking.repositories.ContaRepository;
import com.core_banking.services.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping(path = "/cadastrarConta")
    public ResponseEntity<Conta> cadastrarConta(@RequestBody Conta conta) {
        contaService.cadastrarConta(conta);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(conta.getNumeroConta()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/obterContas")
    public ResponseEntity<List> obterContas() {
        return ResponseEntity.ok().body(contaService.obterContas());
    }

    @GetMapping(path = "/obterContaPorId/{id}")
    public ResponseEntity<Conta> obterContaPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(contaService.obterContaPorId(id));
    }

    @PutMapping(path = "/alterarConta/{id}")
    public ResponseEntity<Conta> alterarConta(@PathVariable Long id, @RequestBody Conta conta) {
        return ResponseEntity.ok().body(contaService.alterarConta(conta, id));
    }
}