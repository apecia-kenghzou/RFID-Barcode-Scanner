package com.uhf.uhfdemo;

public class PassengerFlightInfo {
    private String processed_pnr_id;
    private String products_air_segment_operating_flight_designator_carrier_code;

    // Constructor
    public PassengerFlightInfo(String pnr, String flightNo) {
        this.processed_pnr_id = pnr;
        this.products_air_segment_operating_flight_designator_carrier_code = flightNo;
    }

    // Getters and setters
    public String getPnr() {
        return processed_pnr_id;
    }

    public void setPnr(String pnr) {
        this.processed_pnr_id = pnr;
    }

    public String getFlightNo() {
        return products_air_segment_operating_flight_designator_carrier_code;
    }

    public void setFlightNo(String flightNo) {
        this.products_air_segment_operating_flight_designator_carrier_code = flightNo;
    }
}
