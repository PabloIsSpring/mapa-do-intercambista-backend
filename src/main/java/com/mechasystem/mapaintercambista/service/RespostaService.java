package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.RespostaRequest;
import com.mechasystem.mapaintercambista.dto.response.RespostaResponse;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Forum;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.model.Resposta;
import com.mechasystem.mapaintercambista.repository.RespostaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class RespostaService {

    private final RespostaRepository respostaRepository;
    private final IntercambistaService intercambistaService;
    private final ForumService forumService;

    public RespostaService (RespostaRepository respostaRepository, IntercambistaService intercambistaService,
                            ForumService forumService) {
        this.respostaRepository = respostaRepository;
        this.intercambistaService = intercambistaService;
        this.forumService = forumService;
    }

    public RespostaResponse salvarResposta (UUID idForum, RespostaRequest req) {
        Forum f = forumService.buscarForumAtivo(idForum);
        Intercambista i = intercambistaService.findByUsername(req.username());

        Resposta r = new Resposta();

        r.setForum(f);
        r.setIntercambista(i);
        r.setComentario(req.comentario());
        r.setCreateAt(LocalDateTime.now());
        r.setLikes(0);
        r.setDislikes(0);

        return mapperRespostaResponse(respostaRepository.save(r));
    }

    public List<RespostaResponse> listarRespostasPorForum(UUID idForum) {
        return respostaRepository.findByForumIdAndDeletedAtIsNullOrderByCreateAtAsc(idForum)
                .stream()
                .map(this::mapperRespostaResponse)
                .toList();
    }

    public RespostaResponse buscarRespostaPorId(UUID id) {
        Resposta resposta = buscarRespostaAtiva(id);

        return mapperRespostaResponse(resposta);
    }

    @Transactional
    public RespostaResponse curtirResposta(UUID id) {
        Resposta resposta = buscarRespostaAtiva(id);

        resposta.setLikes(resposta.getLikes() + 1);

        Resposta respostaSalva = respostaRepository.save(resposta);

        return mapperRespostaResponse(respostaSalva);
    }

    @Transactional
    public RespostaResponse descurtirResposta(UUID id) {
        Resposta resposta = buscarRespostaAtiva(id);

        resposta.setDislikes(resposta.getDislikes() + 1);

        Resposta respostaSalva = respostaRepository.save(resposta);

        return mapperRespostaResponse(respostaSalva);
    }

    @Transactional
    public void deletarResposta(UUID id) {
        Resposta resposta = buscarRespostaAtiva(id);

        resposta.setDeletedAt(LocalDateTime.now());

        respostaRepository.save(resposta);
    }

    private Resposta buscarRespostaAtiva(UUID id) {
        return respostaRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NotFoundException("Resposta não encontrada"));
    }


    public RespostaResponse mapperRespostaResponse(Resposta r) {
        return new RespostaResponse(
                r.getId(),
                forumService.mapperForumResponse(r.getForum()),
                intercambistaService.mapperEntity(r.getIntercambista()),
                r.getComentario(),
                r.getLikes(),
                r.getDislikes(),
                r.getCreateAt()
        );
    }
}
