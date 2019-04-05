package com.example.cst2355_final_19w;

public class Eachflight {
    private String flightNo;
    private String airport;

    private long id;

    public Eachflight(String flightNo) {
        setflightNo(flightNo);
    }

    public Eachflight(String message, String airport) {
        setflightNo(message);
        setairport(airport);
    }
    public Eachflight(String message, String airport, long id){
        setflightNo(message);
        setairport(airport);
        setId(id);
    }

    public String getflightNo() {
        return this.flightNo;
    }
    public void setflightNo(String flightNo){
        this.flightNo=flightNo;
    }
    public String getairport() {
        return this.airport;
    }
    public void setairport(String airport){
        this.airport=airport;
    }


    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}