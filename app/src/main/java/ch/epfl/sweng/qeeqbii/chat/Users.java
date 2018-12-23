package ch.epfl.sweng.qeeqbii.chat;


public class Users {

    public String name;
    public String image;
    public String status;
    public String thumb_image;
    public String age;
    public String allergies;
    public String degout;



    public Users(){

    }
    public Users(String name){
        this.name = name;
    }

    public Users(String name, String image, String status, String thumb_image, String age, String allergies, String degout) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.thumb_image = thumb_image;
        this.age = age;
        this.allergies = allergies;
        this.degout = degout;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergie) {
        this.allergies = allergie;
    }

    public String getDegout() {
        return degout;
    }

    public void setDegout(String degout) {
        this.degout = degout;
    }

}
