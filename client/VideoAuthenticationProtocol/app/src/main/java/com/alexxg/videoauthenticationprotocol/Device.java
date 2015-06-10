package com.alexxg.videoauthenticationprotocol;

/**
 * Created by gallowaa on 6/7/2015.
 */

import com.strongloop.android.loopback.Model;

import java.math.BigDecimal;

/**
 * A device that is used to record video.
 */
public class Device extends Model {

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
