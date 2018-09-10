package com.example2017.android.clientsmap;

/**
 * Created by M7moud on 04-Sep-18.
 */
public class ReportItem  {

    String ClientName,DrName,Reports,Time,TimeRecieved,latitude,longitude;

    public String getTimeRecieved() {
        return TimeRecieved;
    }

    public ReportItem(String clientName, String drName, String reports, String time, String latitude, String longitude, String Recieved) {
        ClientName = clientName;
        DrName = drName;
        Reports = reports;
        Time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.TimeRecieved=TimeRecieved;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public void setDrName(String drName) {
        DrName = drName;
    }

    public void setReports(String reports) {
        Reports = reports;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public ReportItem() {

    }

    public String getClientName() {
        return ClientName;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getTime() {
        return Time;
    }

    public String getDrName() {
        return DrName;
    }

    public String getReports() {
        return Reports;
    }
}
