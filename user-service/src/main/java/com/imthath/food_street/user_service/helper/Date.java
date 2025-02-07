package com.imthath.food_street.user_service.helper;

import java.util.concurrent.TimeUnit;

public class Date extends java.util.Date {
    static public final Date now = new Date();
    
    Date() { super(); }
    
    Date(long time) {
        super(time);
    }
    
    public Date adding(int duration, TimeUnit timeUnit) {
        return new Date(getTime() + timeUnit.toMillis(duration));
    }
}
