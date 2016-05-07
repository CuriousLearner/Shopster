package in.co.shopster.shopster.rest.models;

import java.util.ArrayList;

/**
 * Created by ayush on 7/5/16.
 */
public class CustomerLogin {

    /**
     * {
     "id": 2,
     "first_name": "",
     "last_name": "",
     "email": "admin@shopster.in",
     "age": null,
     "gender": "M",
     "address": null,
     "phone": null,
     "is_staff": true,
     "is_active": true,
     "groups": [],
     "password": "pbkdf2_sha256$24000$VyEAh0UoH1rc$DRHmPG1rwnbtkLIHsk/JTA7cnMvEc22mx+6lCLVsGVo=",
     "uhash_token": "qJ1xNlVZ6nAvBo7pQgDO3DRWdK8bLk9j3Pze40mr"
     }
     */

    String firstName, lastName, email, password,uhash_token;
    byte age;
    char gender;
    ArrayList groups;
    boolean is_staff,is_active;
    Address address;
    long phone,id;


    public CustomerLogin(
            long id,
            String firstName,
            String lastName,
            String email,
            byte age,
            char gender,
            Address address,
            long phone,
            boolean isStaff,
            boolean isActive,
            ArrayList groups,
            String password,
            String uhash
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.uhash_token = uhash;
        this.groups = groups;
        this.is_active = isActive;
        this.is_staff = isStaff;
    }


    public String toString() {
        return "ID : " +id+"\n"+
                "First Name : "+firstName+"\n"+
                "Last Name : "+lastName+"\n"+
                "Email : "+email+"\n"+
                "Password : "+password+"\n"+
                "Age : "+age+"\n"+
                "Gender : "+gender+"\n"+
                "Address : "+address.toString()+"\n"+
                "Phone : "+phone+"\n";
    }
    public long getId() { return this.id; }
    public String getUhash() { return this.uhash_token; }


}
