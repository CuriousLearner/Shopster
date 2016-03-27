package in.co.shopster.shopster.rest.models;

import java.util.Date;

/**
 * Created by vikram on 19/3/16.
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
    String firstName, lastName, email, password;
    byte age;
    char gender;
    Address address;
    int phone;


    public Customer(
            String firstName,
            String lastName,
            String email,
            String password,
            byte age,
            char gender,
            Address address,
            int phone
            ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
    }


}
