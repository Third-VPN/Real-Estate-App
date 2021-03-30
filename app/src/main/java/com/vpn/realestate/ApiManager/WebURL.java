package com.vpn.realestate.ApiManager;

public class WebURL {


    private static final String IP_ADDRESS = "192.168.43.244";
    public static final String MAIN_URL = "http://" + IP_ADDRESS + "/Real Estate Admin Panel/Real Estate Admin Panel/APIs/";

    //registration api url
    public static final String SIGN_UP_URL = MAIN_URL + "signup.php";

    //city api url
    public static final String CITY_URL = MAIN_URL + "city.php";

}
