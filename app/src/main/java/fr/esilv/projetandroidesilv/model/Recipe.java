package fr.esilv.projetandroidesilv.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Recipe  implements Serializable {

    private String uri;
    private String label;
    private String totalTime;

    @SerializedName("image")
    private String imageUrl;
    private ArrayList<String> dietLabels;
    private ArrayList<String> healthLabels;
    private ArrayList<String> allLabels = null;
    private ArrayList<HealthLabel> allLabelsObj = null;
    private String desc;
    private String yield;
    private Double star;
    private Boolean isFavorite = false;

    public Recipe(String uri, String name, String imageUrl, String desc, String yield, Boolean isLoved){
        this.uri = uri;
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
        if (this.star == null){
            this.star = generateStar(3, 5);
        }
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
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
        if(this.isFavorite == null)
            this.isFavorite = false;
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

    public ArrayList<String> getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(ArrayList<String> dietLabels) {
        this.dietLabels = dietLabels;
    }

    public ArrayList<String> getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(ArrayList<String> healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String getTotalTime() {
        return totalTime + " minutes";
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }


    public ArrayList<String> getAllLabels(){
        if(allLabels == null){
            allLabels =  new ArrayList<String>();
            allLabels.addAll(this.dietLabels);
            allLabels.addAll(this.healthLabels);
        }
        return allLabels;
    }

    public ArrayList<HealthLabel> getAllLabelsObj(){
        if(allLabelsObj == null){
            allLabelsObj = new ArrayList<HealthLabel>();
            for (String l: getAllLabels()){
                allLabelsObj.add(new HealthLabel(l));
            }
        }
        return allLabelsObj;
    }
}
