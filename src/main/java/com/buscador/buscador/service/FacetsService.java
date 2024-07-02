package com.buscador.buscador.service;

import com.buscador.buscador.data.DataAccessRepository;
import com.buscador.buscador.model.response.PeliculasQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacetsService {

	private final DataAccessRepository repository;

	public PeliculasQueryResponse getPeliculas(
			List<String> titleValues,
			List<String> budgetValues,
			List<String> originalLanguageValues,
			List<String> originalTitleValues,
			String releaseDate,
			List<String> revenue,
			List<String> runTime,
			String tagLine,
			List<String> voteAverage,
			String overview,
			String generosValues,
			String page) {

		return repository.findPeliculas(
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
				page);
	}
        
}
