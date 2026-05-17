package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.exception.NotFileTypeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImagenService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    public String salvarImagem(MultipartFile image, String pasta) {
        try {
            if (image.isEmpty()) {
                throw new NotFileTypeException("É obrigatório enviar uma imagem");
            }

            String contentType = image.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new NotFileTypeException("O arquivo precisa ser uma imagem");
            }

            Path upload = Paths.get(uploadDir, pasta);
            Files.createDirectories(upload);

            String nomeOriginal = image.getOriginalFilename();

            if (nomeOriginal == null || !nomeOriginal.contains(".")) {
                throw new NotFileTypeException("Arquivo sem extensão válida");
            }

            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String nomeNovoArquivo = UUID.randomUUID() + extensao;

            Path caminhoCompleto = upload.resolve(nomeNovoArquivo);

            Files.copy(
                    image.getInputStream(),
                    caminhoCompleto,
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "/uploads/" + pasta + "/" + nomeNovoArquivo;

        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar imagem", e);
        }
    }
}
