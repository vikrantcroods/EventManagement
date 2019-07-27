package com.croods.eventmanagement.model;

public class MaterialSendListResponse
{
    private String prefix,status,jobCode,materialOutwardDate;
    private int materialOutwardNo,outquantity;


    public int getOutquantity() {
        return outquantity;
    }

    public void setOutquantity(int outquantity) {
        this.outquantity = outquantity;
    }

    public String getMaterialOutwardDate() {
        return materialOutwardDate;
    }

    public void setMaterialOutwardDate(String materialOutwardDate) {
        this.materialOutwardDate = materialOutwardDate;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public int getMaterialOutwardNo() {
        return materialOutwardNo;
    }

    public void setMaterialOutwardNo(int materialOutwardNo) {
        this.materialOutwardNo = materialOutwardNo;
    }
}
