package com.ttt.chat_module.models.message_models;

public class LocationMessage extends BaseMessage {
    private double lat;
    private double lon;
    private String address;

    public LocationMessage(String ownerID, double lat, double lon, String address) {
        super(ownerID, BaseMessage.LOCATION_MESSAGE);
        this.lat = lat;
        this.lon = lon;
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
