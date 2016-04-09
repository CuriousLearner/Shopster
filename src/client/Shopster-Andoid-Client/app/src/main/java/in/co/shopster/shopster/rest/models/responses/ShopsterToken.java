package in.co.shopster.shopster.rest.models.responses;

/**
 * Created by vikram on 7/4/16.
 */
public class ShopsterToken {

    private String token;

    public ShopsterToken(String token) {
        this.token = token;
    }

    public String toString() {
        return "Token : "+token;
    }

    public String getToken() { return this.token; }
}
