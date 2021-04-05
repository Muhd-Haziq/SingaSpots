package sg.edu.rp.c346.s19047241.singaspots;

public class food {
    private String name;
    private String thumbnail;
    private String ingredients;
    private String methods;
    private String marinade;
    private boolean favourite;
    private String vidId;

    public food() {

    }

    public food(String name, String thumbnail, String ingredients, String methods, String marinade) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.ingredients = ingredients;
        this.methods = methods;
        this.marinade = marinade;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethods() {
        return methods;
    }

    public void setMethods(String methods) {
        this.methods = methods;
    }

    public String getMarinade() {
        return marinade;
    }

    public void setMarinade(String marinade) {
        this.marinade = marinade;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getVidId() {
        return vidId;
    }

    public void setVidId(String vidId) {
        this.vidId = vidId;
    }
}
