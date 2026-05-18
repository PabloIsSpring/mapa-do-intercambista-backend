package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.IntercambistaUpdtRequest;
import com.mechasystem.mapaintercambista.dto.request.RegisterUserRequest;
import com.mechasystem.mapaintercambista.dto.response.IntercambistaResponse;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.IntercambistaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class IntercambistaService {

    private final IntercambistaRepository intercambistaRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public IntercambistaService (IntercambistaRepository intercambistaRepository) {
        this.intercambistaRepository = intercambistaRepository;
    }

    public Intercambista findByUsername(String username) {
        return intercambistaRepository.findByUsername(username).
                orElseThrow(() -> new NotFoundException("Não foi encontrado usuário com esse nome"));
    }

    public IntercambistaResponse getIntecambistaByUsername (String username) {
        Intercambista u = findByUsername(username);

        return mapperEntity(u);
    }

    public IntercambistaResponse saveIntercambista(RegisterUserRequest request, User u) {
        if(intercambistaRepository.findByUsername(request.username()).isPresent()) {
            throw new ConflictException("Username já está em uso");
        }

        Intercambista nInt = new Intercambista();
        nInt.setNome(request.nome());
        nInt.setSobrenome(request.sobrenome());
        nInt.setUser(u);
        nInt.setUsername(request.username());
        nInt.setIdade(request.idade());
        nInt.setUrlFotoPerfil("/uploads/intercambista/default-profile/png");

        intercambistaRepository.save(nInt);

        return mapperEntity(nInt);
    }

    public IntercambistaResponse uploadFotoPerfil(String username, MultipartFile imagem) {
        Intercambista i = findByUsername(username);

        try {
            if(imagem.isEmpty()) {
                throw new RuntimeException("É obrigatório enviar uma imagem");
            }

            String contentType = imagem.getContentType();

            if(contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Mande um arquivo do tipo PNG ou JPEG");
            }

            Path upload = Paths.get(uploadDir, "intercambista");
            Files.createDirectories(upload);
            String nomeArquivoOriginal = imagem.getOriginalFilename();

            String extensao = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
            String nomeNovoArquivo = UUID.randomUUID() + extensao;

            Path caminhoCompleto = upload.resolve(nomeNovoArquivo);

            Files.copy(
                    imagem.getInputStream(),
                    caminhoCompleto,
                    StandardCopyOption.REPLACE_EXISTING
            );

            String urlFotoPerfil = "/uploads/intercambista/" + nomeNovoArquivo;

            i.setUrlFotoPerfil(urlFotoPerfil);
            return mapperEntity(intercambistaRepository.save(i));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload da imagem",e);
        }
    }

    public IntercambistaResponse updateUsername(IntercambistaUpdtRequest u) {
        Intercambista nInt = findByUsername(u.username());
        if(intercambistaRepository.findByUsername(u.nUsername()).isPresent()) {
            throw new ConflictException("Username já está em uso");
        }

        nInt.setUsername(u.nUsername());

        intercambistaRepository.save(nInt);

        return mapperEntity(nInt);
    }

    @Transactional
    public void deleteIntercambista(String username) {
        Intercambista i = findByUsername(username);

        i.getUser().setDeletedAt(LocalDate.now());

        intercambistaRepository.save(i);
    }

    public IntercambistaResponse mapperEntity(Intercambista i) {
        return new IntercambistaResponse(
                i.getUsername(),
                i.getNome(),
                i.getIdade(),
                i.getUrlFotoPerfil()
        );
    }
}
