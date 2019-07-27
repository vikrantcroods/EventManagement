package com.croods.eventmanagement.model;

import java.util.List;

public class SendMaterialRequest
{
    private int eventId;
        private String driverMobileNo,driverName,employeeIds,materialOutwardDate,note,transportId,vehicleNo;
    private List<Long> materialOutwardItemVos;

    public SendMaterialRequest(int eventId,String driverMobileNo, String driverName, String employeeIds, String materialOutwardDate, String note, String transportId, String vehicleNo, List<Long> materialOutwardItemVos) {
        this.eventId = eventId;
        this.driverMobileNo = driverMobileNo;
        this.driverName = driverName;
        this.employeeIds = employeeIds;
        this.materialOutwardDate = materialOutwardDate;
        this.note = note;
        this.transportId = transportId;
        this.vehicleNo = vehicleNo;
        this.materialOutwardItemVos = materialOutwardItemVos;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getDriverMobileNo() {
        return driverMobileNo;
    }

    public void setDriverMobileNo(String driverMobileNo) {
        this.driverMobileNo = driverMobileNo;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }

    public String getMaterialOutwardDate() {
        return materialOutwardDate;
    }

    public void setMaterialOutwardDate(String materialOutwardDate) {
        this.materialOutwardDate = materialOutwardDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public List<Long> getMaterialOutwardItemVos() {
        return materialOutwardItemVos;
    }

    public void setMaterialOutwardItemVos(List<Long> materialOutwardItemVos) {
        this.materialOutwardItemVos = materialOutwardItemVos;
    }
}
