package com.buscador.buscador.model.db;

import com.buscador.buscador.utils.Consts;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Document(indexName = "peliculas", createIndex = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Pelicula {
	
	@Id
	private String id;
	
	@Field(type = FieldType.Text, name = Consts.FIELD_TITLE)
	private String title;

	@Field(type = FieldType.Double, name = Consts.FIELD_BUDGET)
	private Double budget;

	@Field(type = FieldType.Keyword, name = Consts.FIELD_ORIGINAL_LANGUAGE)
	private String originalLanguage;
	
	@Field(type = FieldType.Text, name = Consts.FIELD_ORIGINAL_TITLE)
	private String originalTitle;
	
	@Field(type = FieldType.Date, format = DateFormat.date, name = Consts.FIELD_RELEASE_DATE)
	private LocalDate releaseDate;

	@Field(type = FieldType.Double, name = Consts.FIELD_REVENUE)
	private Double revenue;

	@Field(type = FieldType.Integer, name = Consts.FIELD_RUN_TIME)
	private String runTime;

	@Field(type = FieldType.Text, name = Consts.FIELD_TAG_LINE)
	private String tagLine;

	@Field(type = FieldType.Double, name = Consts.FIELD_VOTE_AVERAGE)
	private Double voteAverage;


	@Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_OVERVIEW)
	private String overview;

	@Field(type = FieldType.Integer, name = Consts.FIELD_ID_FILM)
	private String idFilm;

	@Field(type = FieldType.Text, name = Consts.FIELD_BACKDROP_PATH)
	private String backdropPath;

	@Field(type = FieldType.Text, name = Consts.FIELD_POSTER_PATH)
	private String posterPath;

	@Field(type = FieldType.Search_As_You_Type, name = Consts.FIELD_GENEROS)
	private String generos;

	@Field(type = FieldType.Text, name = Consts.FIELD_VIDEO)
	private String video;


}
