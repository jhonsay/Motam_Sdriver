package com.cryptoull.motam_sdriver;

/**
 * Created by jhonsay on 2/6/18.
 */

public class Ubicacion {

    public double lat;
    public double lon;
    //private String uid;
    private long timeStamp;
    //public Timestamp time;

    public Ubicacion() {

    }

    public Ubicacion(double lat, double lon, long timeStamp) {
        //this.uid = uid;
        this.lat = lat;
        this.lon = lon;
        this.timeStamp = timeStamp;
        //this.time = new Timestamp(timeStamp);
    }


    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setTimestamp(long timestamp) {
        this.timeStamp = timestamp;
        //this.time = new Timestamp(timeStamp);
    }

    public String getTimeStringStamp() {

        return String.valueOf(timeStamp);
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    /*
    public String getUid() {
        return uid;
    }*/
}
