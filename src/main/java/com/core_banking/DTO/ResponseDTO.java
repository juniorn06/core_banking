package com.core_banking.DTO;

import jakarta.transaction.Transactional;
import lombok.Data;

@Data
@Transactional
public class ResponseDTO {
    private Object objeto;
    private String msg;

    public ResponseDTO(Object objeto, String msg) {
        this.objeto = objeto;
        this.msg = msg;
    }

    public ResponseDTO(String msg) {
        this.msg = msg;
    }
}
