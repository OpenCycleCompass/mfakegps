package de.mytfg.jufo.mfakegps;

public class FakeLocation {
    public double latitude, longitude;
    public double altitude;
    public double speed;
    public double accuracy;

    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public double getAltitude(){
        return altitude;
    }
    public double getSpeed(){
        return speed;
    }
    public double getAccuracy(){
        return accuracy;
    }

    public void setLatitude(double v){
        latitude = v;
    }
    public void setLongitude(double v){
        longitude = v;
    }
    public void setAltitude(double v){
        altitude = v;
    }
    public void setSpeed(double v){
        speed = v;
    }
    public void setAccuracy(double v){
        accuracy = v;
    }
}