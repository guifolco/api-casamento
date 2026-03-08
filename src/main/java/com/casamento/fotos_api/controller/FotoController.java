package com.casamento.fotos_api.controller;

import com.casamento.fotos_api.model.Foto;
import com.casamento.fotos_api.service.FotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/fotos")
@CrossOrigin(origins = "*") 
public class FotoController {

    @Autowired
    private FotoService fotoService;

    @PostMapping("/upload")
    public ResponseEntity<String> receberFoto(@RequestParam("arquivo") MultipartFile arquivo) {

        System.out.println("🚨 [ALERTA] ALGUÉM CLICOU NO BOTÃO E A FOTO CHEGOU NO CONTROLLER!");

        if (arquivo.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nenhuma foto foi enviada.");
        }

        try {
            Foto fotoSalva = fotoService.processarESalvarFoto(arquivo);

            return ResponseEntity.ok("Sucesso! Foto salva. ID no banco: " + fotoSalva.getId());

        } catch (Exception e) {

            System.out.println("❌ [ERRO GRAVE] O JAVA TENTOU PROCESSAR E EXPLODIU:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Ops! Erro ao salvar a foto: " + e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalFotos() {
        long total = fotoService.contarFotos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Foto>> listarTodasAsFotos() {
        List<Foto> fotos = fotoService.buscarTodasFotos(); 
        return ResponseEntity.ok(fotos);
    }

    
    
}