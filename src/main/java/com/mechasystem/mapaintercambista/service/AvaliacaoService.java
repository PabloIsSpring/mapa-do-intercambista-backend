package com.mechasystem.mapaintercambista.service;

import com.mechasystem.mapaintercambista.dto.request.CreateAvaliacaoRequest;
import com.mechasystem.mapaintercambista.dto.response.AvaliacaoResponse;
import com.mechasystem.mapaintercambista.exception.ConflictException;
import com.mechasystem.mapaintercambista.exception.NotFoundException;
import com.mechasystem.mapaintercambista.model.Avaliacao;
import com.mechasystem.mapaintercambista.model.Destino;
import com.mechasystem.mapaintercambista.model.Intercambista;
import com.mechasystem.mapaintercambista.repository.AvaliacaoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final DestinoService destinoService;
    private final IntercambistaService intercambistaService;


    public AvaliacaoService (AvaliacaoRepository avaliacaoRepository, DestinoService destinoService,
                             IntercambistaService intercambistaService) {
        this.avaliacaoRepository = avaliacaoRepository;
        this.intercambistaService = intercambistaService;
        this.destinoService = destinoService;
    }

    @Transactional
    public AvaliacaoResponse salvarAvaliacao(CreateAvaliacaoRequest req) {
        avaliacaoRepository.findByIntercambistaUsernameAndDestinoIdAndDeletedAtIsNull(req.username(), req.idDestino()).
                ifPresent(avaliacao -> {
            throw new ConflictException("Este intercambista já avaliou esse destino");
        });

        Destino d = destinoService.findDestinoById(req.idDestino());
        Intercambista i = intercambistaService.findByUsername(req.username());

        Avaliacao a = new Avaliacao();

        a.setComentario(req.comentario());
        a.setDestino(d);
        a.setIntercambista(i);
        a.setNota(req.nota());

        return mapperAvaliacaoResponse(avaliacaoRepository.save(a));
    }

    public List<AvaliacaoResponse> listarAvaliacoesPorDestino(UUID idDestino) {
        return avaliacaoRepository.findByDestinoIdAndDeletedAtIsNull(idDestino)
                .stream()
                .map(this::mapperAvaliacaoResponse)
                .toList();
    }

    public Double buscarMediaPorDestino(UUID idDestino) {
        Double media = avaliacaoRepository.buscarMediaPorDestino(idDestino);

        if (media == null) {
            return 0.0;
        }

        return media;
    }

    @Transactional
    public void deletarAvaliacao(UUID id) {
        Avaliacao avaliacao = avaliacaoRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new NotFoundException("Avaliação não encontrada"));

        avaliacao.setDeletedAt(LocalDateTime.now());

        avaliacaoRepository.save(avaliacao);
    }

    private AvaliacaoResponse mapperAvaliacaoResponse(Avaliacao avaliacao) {
        return new AvaliacaoResponse(
                avaliacao.getId(),
                destinoService.mapperDestinoResponse(avaliacao.getDestino()),
                intercambistaService.mapperEntity(avaliacao.getIntercambista()),
                avaliacao.getComentario(),
                avaliacao.getNota(),
                avaliacao.getCreatedAt()
        );
    }
}
