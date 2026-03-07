package com.casamento.fotos_api.service;

import com.casamento.fotos_api.model.Foto;
import com.casamento.fotos_api.repository.FotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FotoService {

    @Autowired
    private FotoRepository fotoRepository;

    public Foto processarESalvarFoto(MultipartFile arquivo) {
        String nomeOriginal = arquivo.getOriginalFilename();
        String urlGeradaPelaNuvem = fazerUploadParaNuvem(arquivo);

        Foto novaFoto = new Foto();
        novaFoto.setNomeOriginal(nomeOriginal);
        novaFoto.setUrlImagem(urlGeradaPelaNuvem);
        novaFoto.setDataUpload(LocalDateTime.now()); 

        return fotoRepository.save(novaFoto);
    }

    private String fazerUploadParaNuvem(MultipartFile arquivo) {
        try {
            String nomeArquivo = UUID.randomUUID().toString() + "-" + arquivo.getOriginalFilename();

            Bucket bucket = StorageClient.getInstance().bucket();
            bucket.create(nomeArquivo, arquivo.getBytes(), arquivo.getContentType());

            String urlPublica = "https://firebasestorage.googleapis.com/v0/b/casamento-api-cf767.firebasestorage.app/o/" 
                    + URLEncoder.encode(nomeArquivo, StandardCharsets.UTF_8) + "?alt=media";

            return urlPublica;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer upload para o Firebase: " + e.getMessage());
        }
    }

    @GetMapping("/total")
    public ResponseEntity<Long> getTotalFotos() {
        long total = fotoRepository.count(); 
        return ResponseEntity.ok(total);
    }

    public long contarFotos() {
        return fotoRepository.count();
    }
}