package models;

public class Website {
    private Integer id;
    private String url;

    public Website(String url) {
        this.url = url;
    }

    public Website(Integer website_id, String url) {
        this.id = website_id;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
