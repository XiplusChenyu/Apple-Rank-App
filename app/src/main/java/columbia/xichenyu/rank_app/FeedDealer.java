package columbia.xichenyu.rank_app;

public class FeedDealer {

    private String title;
    private String artist;
    private String date;
    private String right;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    private String price;
    private String imageURL;


    @Override
    public String toString() {
        return "title = " + title + '\n' +
                "artist = " + artist + '\n' +
                "right = " + right + '\n' +
                "date = " + date + '\n' +
                "price  = " + price + '\n' +
                "imageURL = " + imageURL + '\n';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String album) {
        this.right = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }




}
