package fr.esilv.projetandroidesilv.api;

import java.util.ArrayList;
import java.util.List;

import fr.esilv.projetandroidesilv.model.Recipe;

public class Hits {

    private Recipe recipe;
    private Boolean bookmarked;
    private Boolean bought;

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public Boolean getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(Boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public Boolean getBought() {
        return bought;
    }

    public void setBought(Boolean bought) {
        this.bought = bought;
    }

}