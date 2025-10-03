package com.boxvisoft.motos.model;


import java.util.Date;

public class License {
    private String key;
    private Date expirationDate;
    private boolean active;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}