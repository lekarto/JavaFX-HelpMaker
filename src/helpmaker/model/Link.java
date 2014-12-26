package helpmaker.model;

public class Link {
    private String caption;
    private String link;

    public Link(String link, String caption) {
        this.link = link;
        this.caption = caption;
    }

    public Link(String caption) {
        this.link = "";
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean linkValid() {
        return !(caption.equals("") || link.equals(""));
    }
}
