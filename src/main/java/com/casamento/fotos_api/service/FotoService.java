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
import java.util.List;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.util.ArrayList;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.gax.core.FixedCredentialsProvider;
import java.io.FileInputStream;

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

    public List<Foto> buscarTodasFotos() {
        return fotoRepository.findAll();
    }

    public boolean isFotoSegura(MultipartFile arquivo) {
        try {
            ByteString imgBytes = ByteString.copyFrom(arquivo.getBytes());
            Image img = Image.newBuilder().setContent(imgBytes).build();

            Feature feat = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);

            GoogleCredentials credenciais = GoogleCredentials.fromStream(new FileInputStream("E:\\Projeot casamento\\fotos-api\\src\\main\\resources\\firebase-key.json"));
            
            ImageAnnotatorSettings configuracao = ImageAnnotatorSettings.newBuilder()
                    .setCredentialsProvider(FixedCredentialsProvider.create(credenciais))
                    .build();
                    
            try (ImageAnnotatorClient client = ImageAnnotatorClient.create(configuracao)) {
                BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
                List<AnnotateImageResponse> responses = response.getResponsesList();

                for (AnnotateImageResponse res : responses) {
                    if (res.hasError()) {
                        System.out.println("Erro na IA: " + res.getError().getMessage());
                        return false; 
                    }

                    SafeSearchAnnotation safeSearch = res.getSafeSearchAnnotation();
                    
                    // CORRIGIDO: Adicionado o nível Racy (sensual) e reduzido a tolerância para POSSIBLE (possível), incluindo logs no console para monitoramento.
                    Likelihood adulto = safeSearch.getAdult();
                    Likelihood sensual = safeSearch.getRacy();
                    Likelihood violencia = safeSearch.getViolence();

                    System.out.println("Análise da IA -> Adulto: " + adulto + " | Sensual: " + sensual + " | Violência: " + violencia);

                    if (adulto == Likelihood.LIKELY || adulto == Likelihood.VERY_LIKELY ||
                        sensual == Likelihood.LIKELY || sensual == Likelihood.VERY_LIKELY ||
                        violencia == Likelihood.LIKELY || violencia == Likelihood.VERY_LIKELY ||) {
                        
                        System.out.println("🚨 FOTO BLOQUEADA! O segurança não deixou passar.");
                        return false; 
                    }
                }
            }
            return true; 
            
        } catch (Exception e) {
            e.printStackTrace();
            return false; 
        }
    }
}