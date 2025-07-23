package net.justapie.majutsu.db.schema.book;

interface BookDisplayInterface extends DisplayInterface {
    String getType();
    String getLanguage();
    int getPageCount();
}
