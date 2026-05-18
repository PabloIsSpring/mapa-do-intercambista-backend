package com.mechasystem.mapaintercambista.controller;

import com.mechasystem.mapaintercambista.dto.request.CreateForumRequest;
import com.mechasystem.mapaintercambista.dto.response.ForumResponse;
import com.mechasystem.mapaintercambista.service.ForumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/foruns")
public class ForumController {

    private final ForumService forumService;

    public ForumController (ForumService forumService) {
        this.forumService = forumService;
    }

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ForumResponse> saveForum(@ModelAttribute CreateForumRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(forumService.saveForum(req));
    }

    @GetMapping
    public ResponseEntity<List<ForumResponse>> listarForuns() {
        return ResponseEntity.ok(forumService.listarForuns());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForumResponse> buscarForumPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(forumService.buscarForumPorId(id));
    }

    @PutMapping("/{id}/like")
    public ResponseEntity<ForumResponse> curtirForum(@PathVariable UUID id) {
        return ResponseEntity.ok(forumService.curtirForum(id));
    }

    @PutMapping("/{id}/deslike")
    public ResponseEntity<ForumResponse> descurtirForum(@PathVariable UUID id) {
        return ResponseEntity.ok(forumService.descurtirForum(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarForum(@PathVariable UUID id) {
        forumService.deletarForum(id);
        return ResponseEntity.noContent().build();
    }
}
