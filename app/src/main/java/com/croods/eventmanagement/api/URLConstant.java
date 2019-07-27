package com.croods.eventmanagement.api;

public class URLConstant
{
    public  static final String LocalUrl = "http://savalia.croodstech.com/api/";
    //public  static final String LocalUrl = "http://192.168.2.93:8080/api/";
    public  static final String LoginUrl = LocalUrl + "login";
    public  static final String EnquiryList = LocalUrl + "enquiryList";
    public  static final String ServiceList = LocalUrl + "serviceList";
    public  static final String ServiceDetail = LocalUrl + "service";
    public  static final String ServiceSubmit = LocalUrl + "saveserviceactivitys";
    public  static final String JobCount = LocalUrl + "countservicejob";
    public  static final String SaveReference = LocalUrl + "savereferences";
    public  static final String SearchCustomer = LocalUrl + "customersearch";
    public  static final String SearchRefName = LocalUrl + "searchreferencename?";
    public  static final String SearchProduct = LocalUrl + "productList?";
    public  static final String NewEnquiry = LocalUrl + "newenquiry";
    public  static final String SaveEnquiry = LocalUrl + "saveenquiry";
    public  static final String updateenquiry = LocalUrl + "updateenquiry";
    public  static final String dashboard = LocalUrl + "dashboard";
    public  static final String checkpermission = LocalUrl + "checkpermission";
    public  static final String countenquiry = LocalUrl + "countenquiry";


    public static APIInterface getAPIService(){
        return APIClient.getClient(LocalUrl).create(APIInterface.class);
    }

}
