package com.vpn.realestate.ApiManager;

public class WebURL {

    //hosting url
    private static final String IP_ADDRESS = "192.168.43.244";
    public static final String MAIN_URL = "http://" + IP_ADDRESS + "/Real Estate Admin Panel/Real Estate Admin Panel/APIs/";

    //registration api insert
    public static final String SIGN_UP_URL = MAIN_URL + "signup.php";

    //log in api url select
    public static final String SIGN_IN_URL = MAIN_URL + "signin.php";

    //city api url select
    public static final String CITY_URL = MAIN_URL + "city.php";

    //property type api select
    public static final String PROPERTY_TYPE_URL = MAIN_URL + "propertyType.php";

    //area api url select
    public static final String AREA_URL = MAIN_URL + "area.php";

    //property details api select
    public static final String PROPERTY_DETAILS_URL = MAIN_URL + "propertyDetails.php";
    public static final String PROPERTY_IMAGES_URL = MAIN_URL + "/images/";
    public static final String DISPLAY_PROPERTY_IMAGES_URL = MAIN_URL + "propertyView.php";

    //user profile api select
    public static final String PROFILE_URL = MAIN_URL + "profile.php";

    //post property api insert
    public static final String POST_PROPERTY_URL = MAIN_URL + "postProperty.php";

    //property data api select
    public static final String PROPERTY_DATA_URL = MAIN_URL + "propertyData.php";

    //property photo api insert
    public static final String PROPERTY_PHOTO_URL = MAIN_URL + "propertyPhoto.php";

    //property contact api insert
    public static final String PROPERTY_CONTACT_URL = MAIN_URL + "propertyContact.php";

    //profile update api update
    public static final String PROFILE_UPDATE_URL = MAIN_URL + "profileUpdate.php";

    //password change api update
    public static final String PASSWORD_CHANGE_URL = MAIN_URL + "passwordChange.php";

    //my property api select
    public static final String MY_PROPERTY_DETAILS_URL = MAIN_URL + "myPropertyDetails.php";

    //bookmark api insert
    public static final String BOOKMARK_ADD_URL = MAIN_URL + "bookmarkAdd.php";

    //bookmark api insert
    public static final String BOOKMARK_URL = MAIN_URL + "bookmark.php";

    //bookmark property api select
    public static final String BOOKMARK_PROPERTY_URL = MAIN_URL + "bookmarkProperty.php";

    //bookmark api delete
    public static final String BOOKMARK_REMOVE_URL = MAIN_URL + "bookmarkRemove.php";

    //area by city api select
    public static final String CITY_AREA_URL = MAIN_URL + "cityArea.php";

    //property contact details api select
    public static final String PROPERTY_CONTACT_DETAILS_URL = MAIN_URL + "propertyContactDetails.php";

    //delete property api delete
    public static final String PROPERTY_DELETE_URL = MAIN_URL + "propertyDelete.php";

    //update property api update
    public static final String PROPERTY_UPDATE_URL = MAIN_URL + "propertyUpdate.php";

    //get buyers list api select
    public static final String BUYER_URL = MAIN_URL + "buyerList.php";

    //send owner details to buyer api insert
    public static final String SEND_OWNER_DETAILS_URL = MAIN_URL + "sendOwnerDetails.php";

    //get owner details api select
    public static final String GET_OWNER_DETAILS_URL = MAIN_URL + "getOwnerDetails.php";

    //send feedback api insert
    public static final String FEEDBACK_URL = MAIN_URL + "feedback.php";

}
