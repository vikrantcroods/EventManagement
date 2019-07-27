package com.croods.eventmanagement.model;

public class CustomerAddRequest
{

    private long contactId;
    private String companyName;
    private String shortName;
    private String companyMobileno;
    private String companyEmail;
    private String ownerName;
    private String ownerMobileno;
    private String concernPersonName;
    private String concernPersonMobileno;
    private String concernPersonEmail;
    private String panNo;
    private String addressLine1;
    private String addressLine2;
    private String stateCode;
    private String cityCode;
    private String pinCode;

    public CustomerAddRequest(long contactId, String companyName, String shortName, String companyMobileno, String companyEmail, String ownerName, String ownerMobileno, String concernPersonName, String concernPersonMobileno, String concernPersonEmail, String panNo, String addressLine1, String addressLine2, String stateCode, String cityCode, String pinCode) {
        this.contactId = contactId;
        this.companyName = companyName;
        this.shortName = shortName;
        this.companyMobileno = companyMobileno;
        this.companyEmail = companyEmail;
        this.ownerName = ownerName;
        this.ownerMobileno = ownerMobileno;
        this.concernPersonName = concernPersonName;
        this.concernPersonMobileno = concernPersonMobileno;
        this.concernPersonEmail = concernPersonEmail;
        this.panNo = panNo;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.stateCode = stateCode;
        this.cityCode = cityCode;
        this.pinCode = pinCode;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompanyMobileno() {
        return companyMobileno;
    }

    public void setCompanyMobileno(String companyMobileno) {
        this.companyMobileno = companyMobileno;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerMobileno() {
        return ownerMobileno;
    }

    public void setOwnerMobileno(String ownerMobileno) {
        this.ownerMobileno = ownerMobileno;
    }

    public String getConcernPersonName() {
        return concernPersonName;
    }

    public void setConcernPersonName(String concernPersonName) {
        this.concernPersonName = concernPersonName;
    }

    public String getConcernPersonMobileno() {
        return concernPersonMobileno;
    }

    public void setConcernPersonMobileno(String concernPersonMobileno) {
        this.concernPersonMobileno = concernPersonMobileno;
    }

    public String getConcernPersonEmail() {
        return concernPersonEmail;
    }

    public void setConcernPersonEmail(String concernPersonEmail) {
        this.concernPersonEmail = concernPersonEmail;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }
}
