package com.fiticket.fitnesspro.constants;

/**
 * Created by InFinItY on 12/3/2015.
 */
public class Constant {
    public static final String BASE_URL="http://fitdev.cloudapp.net:8080/FitnessPro/services/";
    public static final String LOGIN_URL = BASE_URL+"auth/v1/login";
    public static final String GET_CUSTOMER_URL = BASE_URL+"customers/v1/getCustomers";
    public static final String CREATE_CUSTOMER_URL =BASE_URL+"customers/v1/createCustomer/";
    public static final String UPDATE_CUSTOMER_URL =BASE_URL+"customers/v1/updateCustomer/" ;
    public static final String DELETE_CUSTOMER_URL = BASE_URL+"customers/v1/deleteCustomer/";
    public static final String GET_VENUE_URL = BASE_URL+"venues/v1/getVenues";
    public static final String UPDATE_VENUE_URL=BASE_URL+"venues/v1/updateVenue";
    public static final String CREATE_VENUE_URL = BASE_URL+"venues/v1/createVenue";
    public static final String DELETE_VENUE_URL = BASE_URL+"venues/v1/deleteVenue/";
    public static final String DELETE_CLASS_URL = BASE_URL+"classes/v1/deleteClass/";
    public static final String GET_CLASS_URL = BASE_URL+"classes/v1/getClasses";
    public static final String UPDATE_CLASS_URL = BASE_URL+"classes/v1/updateClass";
    public static final String CREATE_CLASS_URL = BASE_URL+"classes/v1/createClass";
    public static final String CREATE_SCHEDULE_URL=BASE_URL+"timeslots/v1/createTimeslots";
    public static final String GET_TIMESLOTS_URL = BASE_URL+"timeslots/v1/getTimeslots/";
    public static final String UPDATE_SCHEDULE_URL = BASE_URL+"timeslots/v1/updateTimeslots/";
    public static final String DELETE_TIMESLOT_URL = BASE_URL+"timeslots/v1/deleteTimeslots/";
    public static final String GET_USER_URL = BASE_URL + "users/v1/getUsers";
    public static final String CREATE_USER_URL = BASE_URL + "users/v1/createUser";
    public static final String DELETE_USER_URL = BASE_URL + "users/v1/deleteUser/";
    public static final String UPDATE_USER_URL = BASE_URL + "users/v1/updateUser";

    //Shared Pref Keys
    public static final String USER_TENANT_ID = "USER_TENANT_ID";
    public static final String USER_CONTACT_NUMMBER = "USER_CONTACT_NUMMBER";
    public static final String USER_ADDRESS = "USER_ADDRESS";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_USER_ID = "USER_USER_ID";
    public static final String USER_ROLE_NAME = "USER_ROLE_NAME";
    public static final String USER_VENUE_NAME = "USER_VENUE_NAME";
    public static final String USER_TENANT_NAME = "USER_TENANT_NAME";
    public static final String USER_ROLE_ID = "USER_ROLE_ID";
    public static final String PREF = "PREFERENCES";


    public static final String GET_CUSTOMER_BY_CLASSID_URL = BASE_URL+"classes/v1/getCustomersOfClass/";
    public static final String ENROLL_CUSTOMER_BY_CLASSID_URL = BASE_URL + "classes/v1/enrollCustomer";
    public static final String REMOVE_CUSTOMER_FROM_CLASS_URL = BASE_URL + "classes/v1/removeCustomerFromClass";


    public static final int ROLE_TEACHER = 3;
    public static final int ROLE_EMPLOYEE = 4;
    public static final String GET_CLASS_TYPES_URL = BASE_URL+"classTypes/v1/getClassTypes";
    public static final String EXPIRY_DATE = "EXPIRY_DATE";
    public static final String SESSION_TOKEN = "SESSION_TOKEN";
    public static final String USER_NAME = "USER_NAME";
    public static final String DISPLAY_NAME = "DISPLAY_NAME";
    public static String CAPTURED_IMAGE="CAPTURED_IMAGE";
}
