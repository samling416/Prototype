package io.example.peanutbutter.prototype;

import java.io.Serializable;

/**
 * Created by Samuel on 28/03/2017.
 */

public class Peanut implements Serializable {
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
