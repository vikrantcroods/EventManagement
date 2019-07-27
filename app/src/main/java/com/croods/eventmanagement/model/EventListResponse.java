package com.croods.eventmanagement.model;

public class EventListResponse {

    private int eventId,inquantity,outquantity;
    private String jobCode,startDate,workStart,companyName,status;

    private int customerId,employeeId;
    private  String cityCode,stateCode,eventDate,venue,eventName;

    public EventListResponse(int customerId, int employeeId, String cityCode, String stateCode, String eventDate, String venue, String eventName) {
        this.customerId = customerId;
        this.employeeId = employeeId;
        this.cityCode = cityCode;
        this.stateCode = stateCode;
        this.eventDate = eventDate;
        this.venue = venue;
        this.eventName = eventName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getInquantity() {
        return inquantity;
    }

    public void setInquantity(int inquantity) {
        this.inquantity = inquantity;
    }

    public int getOutquantity() {
        return outquantity;
    }

    public void setOutquantity(int outquantity) {
        this.outquantity = outquantity;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getWorkStart() {
        return workStart;
    }

    public void setWorkStart(String workStart) {
        this.workStart = workStart;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
