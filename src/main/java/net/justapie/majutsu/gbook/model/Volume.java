package net.justapie.majutsu.gbook.model;

import com.google.gson.annotations.Expose;

public class Volume {
    @Expose
    protected String id;

    @Expose
    protected VolumeInfo volumeInfo;

    public VolumeInfo getVolumeInfo() {
        return this.volumeInfo;
    }

    public String getId() {
        return this.id;
    }
}
