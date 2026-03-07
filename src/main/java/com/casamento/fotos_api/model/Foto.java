package com.casamento.fotos_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_fotos")
public class Foto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "url_imagem", nullable = false, length = 1000)
    private String urlImagem;

    @Column(name = "nome_original")
    private String nomeOriginal;

    @Column(name = "data_upload")
    private LocalDateTime dataUpload;

    public Foto() {}

    public Foto(String urlImagem, String nomeOriginal, LocalDateTime dataUpload) {
        this.urlImagem = urlImagem;
        this.nomeOriginal = nomeOriginal;
        this.dataUpload = dataUpload;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrlImagem() { return urlImagem; }
    public void setUrlImagem(String urlImagem) { this.urlImagem = urlImagem; }

    public String getNomeOriginal() { return nomeOriginal; }
    public void setNomeOriginal(String nomeOriginal) { this.nomeOriginal = nomeOriginal; }

    public LocalDateTime getDataUpload() { return dataUpload; }
    public void setDataUpload(LocalDateTime dataUpload) { this.dataUpload = dataUpload; }
}