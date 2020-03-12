package fr.esilv.projetandroidesilv.model;

public class Recipe {

    private String uri;
    private String label;
    private String imageUrl;
    private String desc;
    private String yield;
    private Boolean isFavorite = false;

    public Recipe(String id, String name, String imageUrl, String desc, String yield, Boolean isLoved){
        this.uri = id;
        this.label = name;
        this.imageUrl = imageUrl;
        this.desc = desc;
        this.yield = yield;
        this.isFavorite = isLoved;
    }


    // getters and setters

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
