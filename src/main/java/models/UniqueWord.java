package models;

public class UniqueWord {
    private Integer id;
    private String text;
    private Integer amount;
    private Integer websiteId;

    public UniqueWord(String text, Integer amount, Integer websiteId) {
        this.text = text;
        this.amount = amount;
        this.websiteId = websiteId;
    }

    public UniqueWord(Integer id, String text, Integer amount, Integer websiteId) {
        this.id = id;
        this.text = text;
        this.amount = amount;
        this.websiteId = websiteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }
}
