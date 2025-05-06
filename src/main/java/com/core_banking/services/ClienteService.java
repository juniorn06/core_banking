package com.core_banking.services;

import com.core_banking.DTO.ResponseDTO;
import com.core_banking.entities.Cliente;
import com.core_banking.exceptions.ValidarException;
import com.core_banking.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ResponseEntity<ResponseDTO> cadastrarCliente(Cliente novoCliente) {
        try {
            validarCliente(novoCliente);
            Cliente clienteSalvo = clienteRepository.save(novoCliente);
            String MsgDTO = "Cliente cadastrado com sucesso!";
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDTO(clienteSalvo, MsgDTO));
        } catch (ValidarException e) {
            String msgResponseDTO = e.getMessage();
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseDTO(null, msgResponseDTO));
        } catch (Exception e) {
            String msgResponseDTO = "Erro inesperado. Entre em contato com o Administrador"; //como retornar uma mensagem de erro para o front
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ResponseDTO(null, msgResponseDTO));
        }
    }

    public Cliente obterClientePorId(Long id) { // TODO devolver 400 not found quando nao encontrar
        return clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente não encontrado"));
    }

    public List<Cliente> obterClientes(){
        List<Cliente> listarCliente = clienteRepository.findAll();
        return listarCliente;
    }

    public List<Cliente> obterClientePorNome(String nome){
        List<Cliente> clienteList = clienteRepository.findByNomeIsContainingIgnoreCase(nome);
        if (clienteList.isEmpty()){
            throw new RuntimeException("Cliente não encontrado!");
        }
        return clienteList;
    }

    public Cliente alterarCliente (Cliente clienteAlterado, Long id){
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNome(clienteAlterado.getNome());
            cliente.setCpf(clienteAlterado.getCpf());
            cliente.setTelefone(clienteAlterado.getTelefone());
            cliente.setEmail(clienteAlterado.getEmail());
            cliente.setEndereco(clienteAlterado.getEndereco());
            cliente.setCidade(clienteAlterado.getCidade());
            cliente.setEstado(clienteAlterado.getEstado());
            cliente.setCep(clienteAlterado.getCep());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
    }

    public void deletarCliente(Long id){
        clienteRepository.deleteById(id);
    }

    private void validarCliente(Cliente cliente) {

        if (cliente.getNome() == null) {
            throw new RuntimeException("Campo Nome não pode ser nulo!");
        }
        if (cliente.getCpf() == null) {
            throw new RuntimeException("Campo CPF não pode ser nulo!");
        }
        if (cliente.getTelefone() == null) {
            throw new RuntimeException("Campo Telefone não pode ser nulo!");
        }
        if (cliente.getEndereco() == null) {
            throw new RuntimeException("Campo Endereço não pode ser nulo!");
        }
        if (cliente.getCidade() == null) {
            throw new RuntimeException("Campo Endereço não pode ser nulo!");
        }
        if (cliente.getEstado() == null) {
            throw new RuntimeException("Campo Endereço não pode ser nulo!");
        }
        if (cliente.getCep() == null) {
            throw new RuntimeException("Campo Cep não pode ser nulo!");
        }
    }
}
