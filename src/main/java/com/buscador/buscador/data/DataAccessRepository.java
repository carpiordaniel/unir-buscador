package com.buscador.buscador.data;

import com.buscador.buscador.model.db.Pelicula;
import com.buscador.buscador.model.response.AggregationDetails;
import com.buscador.buscador.model.response.PeliculasQueryResponse;
import com.buscador.buscador.utils.Consts;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    // Esta clase (y bean) es la unica que usan directamente los servicios para
    // acceder a los datos.
    private final PeliculaRepository peliculaRepository;
    private final ElasticsearchOperations elasticClient;

    private final String[] over_view_fields = {"OverView", "OverView._2gram", "OverView._3gram"};
    private final String[] generos_fields = {"Genero", "Genero._2gram", "Genero._3gram"};

    @SneakyThrows
    public PeliculasQueryResponse findPeliculas(
            List<String> titleValues,
            List<String> budgetValues, //rango
            List<String> originalLanguageValues,
            List<String> originalTitleValues,
            String releaseDate,
            List<String> revenue,
            List<String> runTime,
            String tagLine,
            List<String> voteAverage,
            String overview,
            String generosValues,
            String idFilm,
            String page) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        // Si el usuario ha seleccionado algun valor relacionado con el genero, lo añadimos a la query
        if (titleValues != null && !titleValues.isEmpty()) {
            titleValues.forEach(
                    title -> querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_TITLE, title))
            );
        }

        if (budgetValues != null && !budgetValues.isEmpty()) {
            budgetValues.forEach(
                    runT -> {
                        String[] budgetRange = runT != null && runT.contains("-") ? runT.split("-") : new String[]{};
                        if (budgetRange.length == 2) {
                            if ("".equals(budgetRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_BUDGET).to(budgetRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_BUDGET).from(budgetRange[0]).to(budgetRange[1]).includeUpper(false));
                            }
                        }
                        if (budgetRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_BUDGET).from(budgetRange[0]));
                        }
                    }
            );
        }

        if (voteAverage != null && !voteAverage.isEmpty()) {
            voteAverage.forEach(
                    runT -> {
                        String[] voteRange = runT != null && runT.contains("-") ? runT.split("-") : new String[]{};
                        if (voteRange.length == 2) {
                            if ("".equals(voteRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_VOTE_AVERAGE).to(voteRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_VOTE_AVERAGE).from(voteRange[0]).to(voteRange[1]).includeUpper(false));
                            }
                        }
                        if (voteRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_VOTE_AVERAGE).from(voteRange[0]));
                        }
                    }
            );
        }

        if (originalLanguageValues != null && !originalLanguageValues.isEmpty()) {
            originalLanguageValues.forEach(
                    originalLanguage -> querySpec.must(QueryBuilders.termQuery(Consts.FIELD_ORIGINAL_LANGUAGE, originalLanguage))
            );
        }

        if (originalTitleValues != null && !originalTitleValues.isEmpty()) {
            originalTitleValues.forEach(
                    originalTitle -> querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_ORIGINAL_TITLE, originalTitle))
            );
        }

        if (!StringUtils.isEmpty(tagLine)) {
            querySpec.must(QueryBuilders.matchQuery(Consts.FIELD_TAG_LINE, tagLine));
        }

        if (!StringUtils.isEmpty(overview)) {
            querySpec.must(QueryBuilders.multiMatchQuery(overview, over_view_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        if (runTime != null && !runTime.isEmpty()) {
            runTime.forEach(
                    runT -> {
                        String[] runTRange = runT != null && runT.contains("-") ? runT.split("-") : new String[]{};

                        if (runTRange.length == 2) {
                            if ("".equals(runTRange[0])) {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_RUN_TIME).to(runTRange[1]).includeUpper(false));
                            } else {
                                querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_RUN_TIME).from(runTRange[0]).to(runTRange[1]).includeUpper(false));
                            }
                        }
                        if (runTRange.length == 1) {
                            querySpec.must(QueryBuilders.rangeQuery(Consts.FIELD_RUN_TIME).from(runTRange[0]));
                        }
                    }
            );
        }

        if (!StringUtils.isEmpty(generosValues)) {
            querySpec.must(QueryBuilders.multiMatchQuery(generosValues, generos_fields).type(MultiMatchQueryBuilder.Type.BOOL_PREFIX));
        }

        if (!StringUtils.isEmpty(idFilm)) {
            querySpec.must(QueryBuilders.termQuery(Consts.FIELD_ID_FILM, idFilm));
        }
        //Si no se ha seleccionado ningun filtro, se añade un filtro por defecto para que la query no sea vacia

        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        //Construimos la query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        //Se incluyen las Agregaciones
        //Se incluyen las agregaciones de termino para los campos genero, designacion y estado civil
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .terms(Consts.AGG_KEY_ORIGINAL_LANGUAGE)
                .field(Consts.FIELD_ORIGINAL_LANGUAGE).size(10000));

        //Se incluyen las agregaciones de rango para los campos edad y salario
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.AGG_KEY_RUN_TIME)
                .field(Consts.FIELD_RUN_TIME)
                .addUnboundedTo(Consts.AGG_KEY_RUN_TIME_0, 60)
                .addRange(Consts.AGG_KEY_RUN_TIME_1, 60, 90)
                .addUnboundedFrom(Consts.AGG_KEY_RUN_TIME_2, 90));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.AGG_KEY_BUDGET)
                .field(Consts.FIELD_BUDGET)
                .addUnboundedTo(Consts.AGG_KEY_BUDGET_0, 2000000)
                .addRange(Consts.AGG_KEY_BUDGET_1, 2000000, 10000000)
                .addUnboundedFrom(Consts.AGG_KEY_BUDGET_2, 10000000));

        nativeSearchQueryBuilder.addAggregation(AggregationBuilders
                .range(Consts.AGG_KEY_VOTE_AVERAGE)
                .field(Consts.FIELD_VOTE_AVERAGE)
                .addUnboundedTo(Consts.AGG_KEY_VOTE_AVERAGE_0, 5)
                .addRange(Consts.AGG_KEY_VOTE_AVERAGE_1, 5, 7)
                .addUnboundedFrom(Consts.AGG_KEY_VOTE_AVERAGE_2, 7));

        //Se establece un maximo de 5 resultados, va acorde con el tamaño de la pagina
        nativeSearchQueryBuilder.withMaxResults(20);

        //Podemos paginar los resultados en base a la pagina que nos llega como parametro
        //El tamaño de la pagina es de 5 elementos (pero el propio llamante puede cambiarlo si se habilita en la API)
        int pageInt = Integer.parseInt(page);
        if (pageInt >= 0) {
            nativeSearchQueryBuilder.withPageable(PageRequest.of(pageInt, 5));
        }

        //Se construye la query
        Query query = nativeSearchQueryBuilder.build();
        // Se realiza la busqueda
        SearchHits<Pelicula> result = elasticClient.search(query, Pelicula.class);
        return new PeliculasQueryResponse(getResponseEmployees(result), getResponseAggregations(result));
    }

    /**
     * Metodo que convierte los resultados de la busqueda en una lista de
     * empleados.
     *
     * @param result Resultados de la busqueda.
     * @return Lista de empleados.
     */
    private List<Pelicula> getResponseEmployees(SearchHits<Pelicula> result) {
        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

    /**
     * Metodo que convierte las agregaciones de la busqueda en una lista de
     * detalles de agregaciones. Se ha de tener en cuenta que el tipo de
     * agregacion puede ser de tipo rango o de tipo termino.
     *
     * @param result Resultados de la busqueda.
     * @return Lista de detalles de agregaciones.
     */
    private Map<String, List<AggregationDetails>> getResponseAggregations(SearchHits<Pelicula> result) {

        //Mapa de detalles de agregaciones
        Map<String, List<AggregationDetails>> responseAggregations = new HashMap<>();

        //Recorremos las agregaciones
        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = result.getAggregations().asMap();

            //Recorremos las agregaciones
            aggs.forEach((key, value) -> {

                //Si no existe la clave en el mapa, la creamos
                if (!responseAggregations.containsKey(key)) {
                    responseAggregations.put(key, new LinkedList<>());
                }

                //Si la agregacion es de tipo termino, recorremos los buckets
                if (value instanceof ParsedStringTerms parsedStringTerms) {
                    parsedStringTerms.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKey().toString(), (int) bucket.getDocCount()));
                    });
                }

                //Si la agregacion es de tipo rango, recorremos tambien los buckets
                if (value instanceof ParsedRange parsedRange) {
                    parsedRange.getBuckets().forEach(bucket -> {
                        responseAggregations.get(key).add(new AggregationDetails(bucket.getKeyAsString(), (int) bucket.getDocCount()));
                    });
                }
            });
        }
        return responseAggregations;
    }
}
