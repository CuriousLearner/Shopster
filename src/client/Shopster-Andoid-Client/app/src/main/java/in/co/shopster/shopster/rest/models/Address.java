package in.co.shopster.shopster.rest.models;

/**
 * Created by vikram on 19/3/16.
 */
public class Address {
    /**
     * "id": 7,                 ← Which ID is this @sanyam ??
     "line1": "54/23 B",
     "line2": "DDA FLats",
     "city": "Delhi",
     "state": "New Delhi",
     "zipcode": "110001"
     */
    int id, zipCode;
    String line1, line2, city, state;

    public Address(int id, String line1, String line2, String city, String state, int zipCode) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }
}
