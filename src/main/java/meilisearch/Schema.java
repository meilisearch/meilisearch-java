package meilisearch;


public class Schema {
    String[] id;
    String[] title;
    String[] description;
    String[] release_date;
    String[] cover;

    public Schema(String[] id,
                  String[] title,
                  String[] description,
                  String[] release_date,
                  String[] cover) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.release_date = release_date;
        this.cover = cover;
    }
}
