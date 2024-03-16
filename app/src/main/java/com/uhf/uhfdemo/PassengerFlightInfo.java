package com.uhf.uhfdemo;

public class PassengerFlightInfo {
    private String pnr;
    private String flightNo;

    // Constructor
    public PassengerFlightInfo(String pnr, String flightNo) {
        this.pnr = pnr;
        this.flightNo = flightNo;
    }

    // Getters and setters
    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }
}
