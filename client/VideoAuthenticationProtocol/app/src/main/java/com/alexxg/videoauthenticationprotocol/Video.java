package com.alexxg.videoauthenticationprotocol;

import com.strongloop.android.loopback.Model;

import java.math.BigDecimal;


/**
 * Created by gallowaa on 6/7/2015.
 */

import com.strongloop.android.loopback.Model;

import java.math.BigDecimal;

/**
 * Videos recorded by the device.
 */
public class Video extends Model {

    private String name;
    private BigDecimal price;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
