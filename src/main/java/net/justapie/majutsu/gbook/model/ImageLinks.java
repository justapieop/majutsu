package net.justapie.majutsu.gbook.model;

import com.google.gson.annotations.Expose;

public class ImageLinks {
    @Expose
    private String small;

    @Expose
    private String thumbnail;

    @Expose
    private String medium;

    @Expose
    private String large;

    @Expose
    private String extraLarge;

    public String getSmall() {
        return this.small;
    }

    public String getThumbnail() {
        return this.thumbnail;
    }

    public String getMedium() {
        return this.medium;
    }

    public String getLarge() {
        return this.large;
    }

    public String getExtraLarge() {
        return this.extraLarge;
    }
}
