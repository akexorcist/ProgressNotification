package com.akexorcist.progressnotification.data

/**
 * Created by Akexorcist on 8/25/2017 AD.
 */

class ApplyPremiumAccountException : NullPointerException {
    var userId: String? = null

    constructor(userId: String) {
        this.userId = userId
    }

    constructor(s: String, userId: String) : super(s) {
        this.userId = userId
    }
}
