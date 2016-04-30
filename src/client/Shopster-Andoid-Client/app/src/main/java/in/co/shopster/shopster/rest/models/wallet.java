package in.co.shopster.shopster.rest.models;

/**
 * Created by ayush on 11/4/16.
 */
public class wallet {
    String owner, amount;

    public wallet(String owner,String amount) {
        this.owner = owner;
        this.amount= amount;
    }
    public String getBalance() { return this.amount; }
}
