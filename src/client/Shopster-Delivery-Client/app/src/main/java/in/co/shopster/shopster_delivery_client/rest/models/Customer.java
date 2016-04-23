package in.co.shopster.shopster_delivery_client.rest.models;

/**
 * Created by vikram on 23/4/16.
 */
public class Customer {


    /**
     *
     {
     "first_name": "Sanyam",
     "last_name": "Khurana",
     "email": “email@example.com",
     "age": 21,
     "gender": "M",
     "address": {
     "id": 7,                 ← Which ID is this @sanyam ??
     "line1": "54/23 B",
     "line2": "DDA FLats",
     "city": "Delhi",
     "state": "New Delhi",
     "zipcode": "110001"
     },
     "phone": 49392200202,
     "password": "rawpass"
     }
     */
    String firstName, lastName, email, password, uhash_token;
    byte age;
    char gender;
    Address address;
    long phone, id;


    public Customer(
            String firstName,
            String lastName,
            String email,
            String password,
            byte age,
            char gender,
            Address address,
            long phone,
            long id,
            String uhash_token
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.id = id;
        this.uhash_token = uhash_token;
    }


    public String toString() {
        return "First Name : "+firstName+"\n"+
                "Last Name : "+lastName+"\n"+
                "Email : "+email+"\n"+
                "Password : "+password+"\n"+
                "Age : "+age+"\n"+
                "Gender : "+gender+"\n"+
                "Address : "+address.toString()+"\n"+
                "Phone : "+phone+"\n";
    }

    public long getId() { return this.id; }

    public String getUserHash() { return this.uhash_token; }

}

