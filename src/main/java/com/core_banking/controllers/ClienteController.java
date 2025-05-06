package com.core_banking.controllers;

import com.core_banking.DTO.ResponseDTO;
import com.core_banking.entities.Cliente;
import com.core_banking.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    public ClienteService clienteService;

    @PostMapping(path = "/cadastrarCliente")
    public ResponseEntity<ResponseDTO> cadastrarCliente(@RequestBody Cliente cliente){
        clienteService.cadastrarCliente(cliente);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping(path = "/obterClientes")
    public ResponseEntity<List> obterClientes(){
        return ResponseEntity.ok().body(clienteService.obterClientes());
    }

    @GetMapping(path = "/obterClientePorId/{id}")
    public ResponseEntity<Cliente> obterClientePorId(@PathVariable Long id){
        return ResponseEntity.ok().body(clienteService.obterClientePorId(id));
    }

    @GetMapping(path = "/obterClientePorNome/{nome}")
    public ResponseEntity<List> findByNome(@PathVariable String nome){
        return ResponseEntity.ok().body(clienteService.obterClientePorNome( nome));
    }

    @PutMapping(path = "/alterarCliente/{id}")
    public ResponseEntity<Cliente> alteraCliente(@RequestBody Cliente cliente, @PathVariable Long id){
        return ResponseEntity.ok().body(clienteService.alterarCliente(cliente, id));
    }

    @DeleteMapping
    public void deletarCliente(@RequestBody Cliente cliente){
        clienteService.deletarCliente(cliente.getId());
    }
}