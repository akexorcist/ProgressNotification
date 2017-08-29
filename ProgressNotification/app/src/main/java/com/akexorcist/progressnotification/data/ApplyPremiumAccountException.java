package com.akexorcist.progressnotification.data;

/**
 * Created by Akexorcist on 8/25/2017 AD.
 */

public class ApplyPremiumAccountException extends NullPointerException {
    private String userId;

    public ApplyPremiumAccountException(String userId) {
        this.userId = userId;
    }

    public ApplyPremiumAccountException(String s, String userId) {
        super(s);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
