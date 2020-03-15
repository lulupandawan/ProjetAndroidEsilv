package fr.esilv.projetandroidesilv.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Random;

public class Recipe  implements Serializable {

    private String uri;
    private String label;
    private String imageUrl;
    private String desc;
    private String yield;
    private Double star;
    private Boolean isFavorite = false;

    public Recipe(String id, String name, String imageUrl, String desc, String yield, Boolean isLoved){
        this.uri = id;
        this.label = name;
        this.imageUrl = imageUrl;
        this.desc = desc;
        this.yield = yield;
        this.isFavorite = isLoved;
        this.star = generateStar(3, 5);
    }

    private Double generateStar(int min, int max){
        Random r = new Random();
        double random = min + r.nextDouble() * (max - min);
        return random;
    }

    // getters and setters

    public String getStarFormated(){
        return new DecimalFormat("##.#").format(this.getStar());
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public String getId() {
        return uri;
    }

    public void setId(String id) {
        this.uri = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



    public Boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean fav) {
        isFavorite = fav;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

}
