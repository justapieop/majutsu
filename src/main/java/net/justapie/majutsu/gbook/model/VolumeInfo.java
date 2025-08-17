package net.justapie.majutsu.gbook.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VolumeInfo {
    @Expose
    protected String title;

    @Expose
    protected String subtitle;

    @Expose
    protected ArrayList<String> authors;

    @Expose
    protected String description;

    @Expose
    protected ArrayList<IndustryIdentifier> industryIdentifiers;

    @Expose
    protected int pageCount;

    @Expose
    protected String mainCategory;

    @Expose
    protected ArrayList<String> categories;

    @Expose
    protected PrintType printType;

    @Expose
    protected String language;

    @Expose
    protected ImageLinks imageLinks;

    public ImageLinks getImageLinks() {
        return this.imageLinks;
    }

    public String getLanguage() {
        return this.language;
    }

    public List<String> getCategories() {
        return Collections.unmodifiableList(this.categories);
    }

    public PrintType getPrintType() {
        return this.printType;
    }

    public String getMainCategory() {
        return this.mainCategory;
    }

    public int getPageCount() {
        return this.pageCount;
    }

    public List<IndustryIdentifier> getIndustryIdentifiers() {
        return Collections.unmodifiableList(this.industryIdentifiers);
    }

    public String getDescription() {
        return this.description;
    }

    public List<String> getAuthors() {
        if (Objects.isNull(this.authors)) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.authors);
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getTitle() {
        return this.title;
    }
}
