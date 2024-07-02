package com.buscador.buscador.model.response;


import com.buscador.buscador.model.db.Pelicula;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeliculasQueryResponse {
    private List<Pelicula> peliculas;
    private Map<String, List<AggregationDetails>> aggs;
}
