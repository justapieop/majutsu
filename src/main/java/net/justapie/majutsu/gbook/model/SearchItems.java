package net.justapie.majutsu.gbook.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchItems {
    @Expose
    private ArrayList<Volume> items;

    public List<Volume> getItems() {
        return Collections.unmodifiableList(this.items);
    }
}
