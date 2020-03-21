package fr.esilv.projetandroidesilv.model;

public class HealthLabel {
    private String label = "";
    private Boolean isSelected = false;

    public HealthLabel(String label){
        this.label = label;
    }

    public String getLabel(){
        return this.label;
    }

    public Boolean isSelected(){
        return this.isSelected;
    }

    public void setIsSelected(Boolean selected){
        this.isSelected = selected;
    }

}
