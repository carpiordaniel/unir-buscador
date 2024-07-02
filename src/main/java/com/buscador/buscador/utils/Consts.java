package com.buscador.buscador.utils;

public final class Consts {

    private Consts() {
        throw new IllegalStateException("Utility class");
    }

    //Nombres de campos

    public static final String FIELD_TITLE = "Title";
    public static final String FIELD_BUDGET = "Budget";
    public static final String FIELD_ORIGINAL_LANGUAGE = "OriginalLanguage";
    public static final String FIELD_ORIGINAL_TITLE = "OriginalTitle";
    public static final String FIELD_RELEASE_DATE = "ReleaseDate";
    public static final String FIELD_REVENUE = "Revenue";
    public static final String FIELD_RUN_TIME = "RunTime";
    public static final String FIELD_TAG_LINE = "TagLine";
    public static final String FIELD_VOTE_AVERAGE = "VoteAvarage";
    public static final String FIELD_OVERVIEW = "OverView";

    public static final String FIELD_ID_FILM = "IdFilm";
    public static final String FIELD_BACKDROP_PATH = "BackdropPath";
    public static final String FIELD_POSTER_PATH = "PosterPath";
    public static final String FIELD_GENEROS = "Genero";
    public static final String FIELD_VIDEO = "Video";

    //Nombres de agregaciones

    //********************
    public static final String AGG_KEY_ORIGINAL_LANGUAGE = "originalLanguageValues";
    public static final String AGG_KEY_RUN_TIME = "runTimeValues";
    public static final String AGG_KEY_RUN_TIME_0 = "-60";
    public static final String AGG_KEY_RUN_TIME_1 = "60-90";
    public static final String AGG_KEY_RUN_TIME_2 = "90-";

    public static final String AGG_KEY_BUDGET = "budgetValues";
    public static final String AGG_KEY_BUDGET_0 = "-2000000";
    public static final String AGG_KEY_BUDGET_1 = "2000000-10000000";
    public static final String AGG_KEY_BUDGET_2 = "10000000-";

    public static final String AGG_KEY_POPULARITY = "popularityValues";
    public static final String AGG_KEY_POPULARITY_0 = "-100";
    public static final String AGG_KEY_POPULARITY_1 = "100-200";
    public static final String AGG_KEY_POPULARITY_2 = "200-";

    public static final String AGG_KEY_VOTE_AVERAGE = "voteAverageValues";
    public static final String AGG_KEY_VOTE_AVERAGE_0 = "-5";
    public static final String AGG_KEY_VOTE_AVERAGE_1 = "5-7";
    public static final String AGG_KEY_VOTE_AVERAGE_2 = "10-";
    public static final String AGG_KEY_VOTE_COUNT = "voteCountValues";
    public static final String AGG_KEY_VOTE_COUNT_0 = "-10000";
    public static final String AGG_KEY_VOTE_COUNT_1 = "10000-20000";
    public static final String AGG_KEY_VOTE_COUNT_2 = "20000-";

}
