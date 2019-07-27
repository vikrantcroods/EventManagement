package com.croods.eventmanagement.model;

import java.util.List;

public class ReceivedMaterialRequest {
    private int eventId,storeId;
    private String driverMobileNo, driverName, employeeIds, materialInwardDate, note, transportId, vehicleNo;
    private List<Long> materialInwardItemVos;

    public ReceivedMaterialRequest(int eventId, String driverMobileNo, String driverName, String employeeIds, String materialInwardDate, int storeId,String note, String transportId, String vehicleNo, List<Long> materialInwardItemVos) {
        this.eventId = eventId;
        this.driverMobileNo = driverMobileNo;
        this.driverName = driverName;
        this.employeeIds = employeeIds;
        this.materialInwardDate = materialInwardDate;
        this.storeId = storeId;
        this.note = note;
        this.transportId = transportId;
        this.vehicleNo = vehicleNo;
        this.materialInwardItemVos = materialInwardItemVos;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
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

    public String getMaterialInwardDate() {
        return materialInwardDate;
    }

    public void setMaterialInwardDate(String materialInwardDate) {
        this.materialInwardDate = materialInwardDate;
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

    public List<Long> getMaterialInwardItemVos() {
        return materialInwardItemVos;
    }

    public void setMaterialInwardItemVos(List<Long> materialInwardItemVos) {
        this.materialInwardItemVos = materialInwardItemVos;
    }
}
