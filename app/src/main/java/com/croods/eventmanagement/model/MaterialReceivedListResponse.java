package com.croods.eventmanagement.model;

public class MaterialReceivedListResponse
{
    private String prefix,status,jobCode,materialInwardDate,inquantity;
    private int materialInwardNo,outquantity;
    private StoreVo storeVo;

    public String getInquantity() {
        return inquantity;
    }

    public void setInquantity(String inquantity) {
        this.inquantity = inquantity;
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

    public String getMaterialInwardDate() {
        return materialInwardDate;
    }

    public void setMaterialInwardDate(String materialInwardDate) {
        this.materialInwardDate = materialInwardDate;
    }

    public int getMaterialInwardNo() {
        return materialInwardNo;
    }

    public void setMaterialInwardNo(int materialInwardNo) {
        this.materialInwardNo = materialInwardNo;
    }

    public int getOutquantity() {
        return outquantity;
    }

    public void setOutquantity(int outquantity) {
        this.outquantity = outquantity;
    }

    public StoreVo getStoreVo() {
        return storeVo;
    }

    public void setStoreVo(StoreVo storeVo) {
        this.storeVo = storeVo;
    }
}
