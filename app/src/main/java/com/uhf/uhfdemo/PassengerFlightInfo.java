package com.uhf.uhfdemo;

public class PassengerFlightInfo {

    private String pnr,first_name,last_name,flight_no,flight_date,origin,destination;


    // Constructor
    public PassengerFlightInfo(String pnr, String flightNo) {
        this.pnr = pnr;
        this.flight_no = flightNo;
    }
    public void renit(String pnr,String first_name,String last_name,String flight_no,String flight_date,String origin,String destination) {

        this.pnr = pnr;
        this.first_name = first_name;
        this.last_name = last_name;
        this.flight_no = flight_no;
        this.flight_date = flight_date;
        this.destination = destination;
    }
//    public void getDetail() {
//
//     return this;
//    }
    // Getters and setters
    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getFlightNo() {
        return flight_no;
    }

    public void setFlightNo(String flightNo) {
        this.flight_no = flightNo;
    }
}
