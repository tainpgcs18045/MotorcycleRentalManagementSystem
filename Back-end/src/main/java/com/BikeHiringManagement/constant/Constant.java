package com.BikeHiringManagement.constant;

public class Constant {
    public static final String SYSTEM_ERROR = "SYSTEM ERROR!!!";

    /* ----------------------- ERROR CODE -------------------------- */
    public static final Integer SYSTEM_ERROR_CODE = -1;
    public static final Integer SUCCESS_CODE = 1;
    public static final Integer LOGIC_ERROR_CODE = 0;

    /* ----------------------- PERMISSION FLAG -------------------------- */
    public static final String USER_PAGE = "";
    public static final String CLIENT_CONTEXTPATH = "http://localhost:3000";

    /* ----------------------- CHECK EXIST ENTITY -------------------------- */
    public static final Integer BIKE_CATEGORY = 1;
    public static final Integer BIKE_COLOR = 2;
    public static final Integer BIKE_IMAGE = 3;
    public static final Integer BIKE_MANUFACTURER = 4;
    public static final Integer BIKE = 5;
    public static final Integer ORDER = 6;
    public static final Integer MAINTAIN = 7;

    public static final Integer FORMULA = 8;

    /* ----------------------- HISTORY -------------------------- */
    public static final String HISTORY_LOGIN = "LOGIN";
    public static final String HISTORY_CREATE = "CREATE";
    public static final String HISTORY_UPDATE = "UPDATE";
    public static final String HISTORY_DELETE = "DELETE";

    /* ----------------------- FORMULA -------------------------- */
    public static final Long FORMULA_BIKE_HIRING_CALCULATION = (long) 1;

    /* ----------------------- TIME -------------------------- */
    public static final double MILLI_TO_HOUR = 1000 * 60 * 60;

    /* ----------------------- STATUS -------------------------- */
    // BIKE
    public static final String STATUS_BIKE_HIRED = "HIRED";
    public static final String STATUS_BIKE_AVAILABLE = "AVAILABLE";

    // ORDER
    public static final String STATUS_ORDER_IN_CART = "IN CART";
    public static final String STATUS_ORDER_PENDING = "PENDING";
    public static final String STATUS_ORDER_CANCEL = "CANCEL";
    public static final String STATUS_ORDER_CLOSED = "CLOSED";

    // MAINTAIN
    public static final String STATUS_MAINTAIN_GENERAL = "GENERAL";
    public static final String STATUS_MAINTAIN_BIKE = "BIKE";

    /* ----------------------- BIKE CATEGORY -------------------------- */
    public static final Long BIKE_AUTO = (long) 1;
    public static final Long BIKE_MANUAL = (long) 2;
}