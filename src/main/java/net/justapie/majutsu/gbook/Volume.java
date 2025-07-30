package net.justapie.majutsu.gbook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Volume {
    protected String id;
    protected String title;
    protected String subtitle;
    protected ArrayList<String> authors;
    protected String description;
    protected ArrayList<IndustryIdentifier> industryIdentifiers;
    protected int pageCount;
    protected String mainCategory;
    protected ArrayList<String> categories;
    protected PrintType printType;
    protected String language;
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
        return Collections.unmodifiableList(this.authors);
    }

    public String getSubtitle() {
        return this.subtitle;
    }

    public String getTitle() {
        return this.title;
    }

    public String getId() {
        return this.id;
    }
}
