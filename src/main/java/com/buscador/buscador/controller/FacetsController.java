package com.buscador.buscador.controller;

import com.buscador.buscador.model.response.PeliculasQueryResponse;
import com.buscador.buscador.service.FacetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class FacetsController {

    private final FacetsService service;

    @GetMapping("/peliculas")
    public ResponseEntity<PeliculasQueryResponse> getPeliculas(
            @RequestParam(required = false) List<String> titleValues,
            @RequestParam(required = false) List<String> budgetValues,
            @RequestParam(required = false) List<String> originalLanguageValues,
            @RequestParam(required = false) List<String> originalTitleValues,
            @RequestParam(required = false) String releaseDate,
            @RequestParam(required = false) List<String> revenue,
            @RequestParam(required = false) List<String> runTime,
            @RequestParam(required = false) String tagLine,
            @RequestParam(required = false) List<String> voteAverage,
            @RequestParam(required = false) String overview,
            @RequestParam(required = false) String generosValues,
            @RequestParam(required = false) String idFilm,
            @RequestParam(required = false, defaultValue = "0") String page) {

        PeliculasQueryResponse response = service.getPeliculas(
                titleValues,
                budgetValues,
                originalLanguageValues,
                originalTitleValues,
                releaseDate,
                revenue,
                runTime,
                tagLine,
                voteAverage,
                overview,
                generosValues,
                idFilm,
                page);
        return ResponseEntity.ok(response);
    }
}
