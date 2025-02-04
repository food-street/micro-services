package com.imthath.food_street.user_service.message_central;

class Constants {
    static final String CUSTOMER_ID = System.getenv("MC_CUSTOMER_ID");
    static final String KEY = System.getenv("MC_KEY");
    static final String SCOPE = "NEW";
    static final String COUNTRY_CODE = "91"; // For India
    static final String FLOW_TYPE = "SMS";
    static final int OTP_LENGTH = 5;
}
