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
    long zipcode;
    String line1, line2, city, state;

    public Address(String line1, String line2, String city, String state, long zipCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipcode = zipCode;
    }

    public String toString() {
        return  "Line one : \n"+line1+"\n"+
                "Line two : "+line2+"\n"+
                "City : "+city+"\n"+
                "State : "+state+"\n"+
                "Zip Code : "+zipcode+"\n";
    }
}
