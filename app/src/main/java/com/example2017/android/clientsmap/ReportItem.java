package com.example2017.android.clientsmap;

/**
 * Created by M7moud on 04-Sep-18.
 */
public class ReportItem  {

    String ClientName,DrName,Reports,Orders,Time,TimeRecieved,latitude,longitude;
    String DeviceName,DeviceCompany,DeviceAdress,DeviceDepartment,DeviceSerial,DeviceType,DeviceNotice,Name,TimeFinish;
    String Adress,Mobile,Comment,OldUnit, Specialisty;

    public String getSpecialisty() {
        return Specialisty;
    }

    public String getAdress() {
        return Adress;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getComment() {
        return Comment;
    }

    public String getOldUnit() {
        return OldUnit;
    }

    public String getOrders() {
        return Orders;
    }

    public String getDeviceAdress() {
        return DeviceAdress;
    }

    public String getDeviceDepartment() {
        return DeviceDepartment;
    }

    public String getDeviceSerial() {
        return DeviceSerial;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public String getDeviceName() {
        return DeviceName;
    }

    public String getDeviceCompany() {
        return DeviceCompany;
    }

    public String getDeviceNotice() {
        return DeviceNotice;
    }

    public String getName() {
        return Name;
    }

    public String getTimeFinish() {
        return TimeFinish;
    }

    public String getTimeRecieved() {
        return TimeRecieved;
    }

    public ReportItem(String clientName, String drName, String reports, String time, String timeRecieved, String deviceName, String deviceCompany, String deviceAdress, String deviceSerial, String deviceDepartment, String deviceType, String name, String deviceNotice, String timeFinish) {
        ClientName = clientName;
        DrName = drName;
        Reports = reports;
        Time = time;
        TimeRecieved = timeRecieved;
        DeviceName = deviceName;
        DeviceCompany = deviceCompany;
        DeviceAdress = deviceAdress;
        DeviceSerial = deviceSerial;
        DeviceDepartment = deviceDepartment;
        DeviceType = deviceType;
        Name = name;
        DeviceNotice = deviceNotice;
        TimeFinish = timeFinish;
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
