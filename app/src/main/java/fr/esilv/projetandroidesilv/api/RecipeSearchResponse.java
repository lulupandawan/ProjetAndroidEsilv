package fr.esilv.projetandroidesilv.api;

import java.util.ArrayList;
import java.util.List;

import fr.esilv.projetandroidesilv.model.Recipe;

public class RecipeSearchResponse {

    private String q;
    private Integer from;
    private Integer to;
    private Boolean more;
    private Integer count;


    private ArrayList<Hits> hits;

    public ArrayList<Hits> getHits() {
        return hits;
    }
}
