package com.croods.eventmanagement.model;

public class CustomerListResponse
{
    private int contactId;
    private String companyName,companyMobileno,cityName;

    public CustomerListResponse(int contactId, String companyName, String companyMobileno, String cityName) {
        this.contactId = contactId;
        this.companyName = companyName;
        this.companyMobileno = companyMobileno;
        this.cityName = cityName;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyMobileno() {
        return companyMobileno;
    }

    public void setCompanyMobileno(String companyMobileno) {
        this.companyMobileno = companyMobileno;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
