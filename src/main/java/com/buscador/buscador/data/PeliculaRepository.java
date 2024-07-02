package com.buscador.buscador.data;

import com.buscador.buscador.model.db.Pelicula;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PeliculaRepository extends ElasticsearchRepository<Pelicula, String> {
	List<Pelicula> findAll();
}
