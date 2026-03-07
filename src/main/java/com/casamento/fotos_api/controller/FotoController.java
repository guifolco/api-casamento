package com.casamento.fotos_api.controller;

import com.casamento.fotos_api.model.Foto;
import com.casamento.fotos_api.service.FotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/fotos")
@CrossOrigin(origins = "*") 
public class FotoController {

    // Injetando o Service para o Controller poder usá-lo
    @Autowired
    private FotoService fotoService;

    @PostMapping("/upload")
    public ResponseEntity<String> receberFoto(@RequestParam("arquivo") MultipartFile arquivo) {
        
        // 1. Verificação de segurança: O arquivo veio vazio?
        if (arquivo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma foto foi enviada.");
        }

        try {
            // 2. Passa a "batata quente" pro Service fazer o trabalho pesado
            Foto fotoSalva = fotoService.processarESalvarFoto(arquivo);

            // 3. Devolve uma resposta de sucesso para a tela do celular do convidado
            return ResponseEntity.ok("Sucesso! Foto salva. ID no banco: " + fotoSalva.getId());

        } catch (Exception e) {
            // 4. Se der qualquer erro (ex: falha na nuvem ou no banco), avisa o usuário e não quebra o app
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Ops! Erro ao salvar a foto: " + e.getMessage());
        }
    }
}