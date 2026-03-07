package com.casamento.fotos_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void inicializarFirebase() {
        try {
            InputStream serviceAccount;
            
            // 1. Tenta pegar a chave da Variável de Ambiente (quando estiver no Render)
            String firebaseEnv = System.getenv("FIREBASE_JSON");
            
            if (firebaseEnv != null && !firebaseEnv.isEmpty()) {
                serviceAccount = new ByteArrayInputStream(firebaseEnv.getBytes(StandardCharsets.UTF_8));
            } else {
                // 2. Se não achar, pega do arquivo local (quando estiver no seu PC)
                serviceAccount = getClass().getClassLoader().getResourceAsStream("firebase-key.json");
            }

            if (serviceAccount == null) {
                throw new RuntimeException("Chave do Firebase não encontrada!");
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setStorageBucket("casamento-api-cf767.firebasestorage.app")
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}