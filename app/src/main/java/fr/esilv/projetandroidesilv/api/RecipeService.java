package fr.esilv.projetandroidesilv.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {
    @GET("search?")
    Call<RecipeSearchResponse> search(@Query("q") String query, @Query("app_key") String app_key, @Query("app_id") String app_id,
                                      @Query("from") Integer from, @Query("to") Integer to);
}