package ch.epfl.sweng.qeeqbii.chat;

/**
 * Created by nicol on 25.11.2017.
 */

public class Request {
    public String date;

    public Request(){

    }

    public Request(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
