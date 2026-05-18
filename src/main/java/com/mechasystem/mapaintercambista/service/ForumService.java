package com.mechasystem.mapaintercambista.service;


import com.mechasystem.mapaintercambista.dto.request.CreateForumRequest;
import com.mechasystem.mapaintercambista.dto.response.ForumResponse;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Forum;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.repository.ForumRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ForumService {

    private final ForumRepository forumRepository;
    private final ImagenService imagenService;
    private final IntercambistaService intercambistaService;

    public ForumService(ForumRepository forumRepository, ImagenService imagenService,
                        IntercambistaService intercambistaService) {
        this.forumRepository = forumRepository;
        this.imagenService = imagenService;
        this.intercambistaService = intercambistaService;
    }

    @Transactional
    public ForumResponse saveForum(CreateForumRequest req) {
        Forum f = new Forum();
        Intercambista i = intercambistaService.findByUsername(req.username());
        String urlFoto = imagenService.salvarImagem(req.image(), "foruns");

        f.setIntercambista(i);
        f.setUrlFotoForum(urlFoto);
        f.setComentario(req.comentario());
        f.setTitulo(req.titulo());
        f.setCreatedAt(LocalDateTime.now());
        f.setLikes(0);
        f.setDislikes(0);

        return mapperForumResponse(forumRepository.save(f));
    }

    public List<ForumResponse> listarForuns() {
        return forumRepository.findByDeletedAtIsNullOrderByCreatedAtDesc()
                .stream()
                .map(this::mapperForumResponse)
                .toList();
    }

    public Forum buscarForumAtivo(UUID id) {
        return forumRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NotFoundException("Fórum não encontrado"));
    }

    public ForumResponse buscarForumPorId(UUID id) {
        Forum forum = buscarForumAtivo(id);

        return mapperForumResponse(forum);
    }

    @Transactional
    public ForumResponse curtirForum(UUID id) {
        Forum forum = buscarForumAtivo(id);

        forum.setLikes(forum.getLikes() + 1);

        Forum forumSalvo = forumRepository.save(forum);

        return mapperForumResponse(forumSalvo);
    }

    @Transactional
    public ForumResponse descurtirForum(UUID id) {
        Forum forum = buscarForumAtivo(id);

        forum.setDislikes(forum.getDislikes() + 1);

        Forum forumSalvo = forumRepository.save(forum);

        return mapperForumResponse(forumSalvo);
    }

    @Transactional
    public void deletarForum(UUID id) {
        Forum forum = buscarForumAtivo(id);

        forum.setDeletedAt(LocalDateTime.now());

        forumRepository.save(forum);
    }

    public ForumResponse mapperForumResponse(Forum f){
        return new ForumResponse(
                f.getId(),
                intercambistaService.mapperEntity(f.getIntercambista()),
                f.getComentario(),
                f.getTitulo(),
                f.getUrlFotoForum(),
                f.getLikes(),
                f.getDislikes(),
                f.getCreatedAt()
        );
    }

}
