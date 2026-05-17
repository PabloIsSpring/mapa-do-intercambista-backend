package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.AgenciaUpdate;
import com.mechasystem.mapaintercambista.dto.request.CreateAgenciaRequest;
import com.mechasystem.mapaintercambista.dto.response.AgenciaResponse;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.NotFileTypeException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Agencia;
import com.mechasystem.mapaintercambista.model.User;
import com.mechasystem.mapaintercambista.repository.AgenciaRepository;
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
public class AgenciaService {

    private final AgenciaRepository agenciaRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    public AgenciaService (AgenciaRepository agenciaRepository) {
        this.agenciaRepository = agenciaRepository;
    }

    public AgenciaResponse saveAgencia (CreateAgenciaRequest req, User u) {
        if(agenciaRepository.findAgenciaByCnpjOrRazaoSocialOrUsername(
                req.cnpj(),
                req.razaoSocial(),
                req.username()
        ).isPresent()){
            throw new ConflictException("Os dados já estão em uso");
        }

        Agencia a = new Agencia();
        a.setCnpj(req.cnpj());
        a.setUsername(req.username());
        a.setRazaoSocial(req.razaoSocial());
        a.setNomeFantasia(req.nomeFantasia());
        a.setUser(u);

        return mapperAgenciaResponse(agenciaRepository.save(a));
    }

    @Transactional
    public void deleteAgenciaByUsername(String username) {
        Agencia a = findAgenciaByUsername(username);

        a.getUser().setDeletedAt(LocalDate.now());

        agenciaRepository.save(a);
    }

    public AgenciaResponse uploadFotoPerfil (String username, MultipartFile image) {
        Agencia a = findAgenciaByUsername(username);

        try {
            if(image.isEmpty()) {
                throw new NotFileTypeException("É obrigatório enviar um tipo de imagem");
            }

            String contentType = image.getContentType();

            if(contentType == null || !contentType.startsWith("image/")) {
                throw new NotFileTypeException("É obrigatório mandar um arquivo do tipo JPEG ou PNG");
            }

            Path upload = Paths.get(uploadDir, "/agencias");
            Files.createDirectories(upload);
            String nomeArquivoOriginal = image.getOriginalFilename();

            String extensao = nomeArquivoOriginal.substring(nomeArquivoOriginal.lastIndexOf("."));
            String nomeNovoArquivo = UUID.randomUUID() + extensao;

            Path caminhoCompleto = upload.resolve(nomeNovoArquivo);

            Files.copy(
                  image.getInputStream(),
                  caminhoCompleto,
                  StandardCopyOption.REPLACE_EXISTING
            );

            String urlFotoPerfil = "/uploads/agencias/"+nomeNovoArquivo;

            a.setUrlFotoAgencia(urlFotoPerfil);
            return mapperAgenciaResponse(agenciaRepository.save(a));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao fazer upload na imagem",e);
        }
    }

    public AgenciaResponse updateAgenciaByUsername(AgenciaUpdate req) {
        if(agenciaRepository.findAgenciaByCnpjOrRazaoSocialOrUsername(
                req.cnpj(), req.razaoSocial(), req.newUsername()).isPresent()) {
            throw new ConflictException("Alguns dos atributos para update já estão em uso");
        }

        Agencia a = findAgenciaByUsername(req.username());

        a.setUsername(req.newUsername());
        a.setCnpj(req.cnpj());
        a.setRazaoSocial(req.razaoSocial());
        a.setNomeFantasia(req.nomeFantasia());

        return mapperAgenciaResponse(agenciaRepository.save(a));
    }

    public AgenciaResponse getAgenciaByUsername (String username) {
        Agencia a = findAgenciaByUsername(username);
        if (a.getDeletedAt() != null) {
            throw new NotFoundException("Essa agência não existe");
        }

        return mapperAgenciaResponse(a);
    }

    public Agencia findAgenciaByUsername(String username) {
        return agenciaRepository.findAgenciaByUsername(username)
                .orElseThrow(() -> new NotFoundException("Esse username de Agência não existe"));
    }

    public AgenciaResponse mapperAgenciaResponse (Agencia a) {
        return new AgenciaResponse(
                a.getUser().getEmail(),
                a.getNomeFantasia(),
                a.getCnpj(),
                a.getUsername(),
                a.getRazaoSocial(),
                a.getUrlFotoAgencia()
        );
    }
}
