package in.co.shopster.shopster.rest.models;

/**
 * Created by vikram on 9/4/16.
 */
public class Product {

//     {
//        "pid": 1,
//        "price": 21,
//        "name": "Maggi",
//        "hash_token": "79GqgP4XNvpRmY3Adbw2nwnaOlzj6LB8JQZ2KeME",
//        "quantity": 23,
//        "category_id": null,
//        "status" : "O"
//    }
    //@TODO: Create order-item model, modify CartAdapter to use order-item model
    public long pid, quantity, category_id, max_quantity;
    public float price;
    public String name, hash_token;
    public char status;

    public Product(long pid, String name, String hashToken, float price, long quantity,long categoryId, char status) {
        this.pid = pid;
        this.quantity = quantity;
        this.category_id = categoryId;
        this.price = price;
        this.name = name;
        this.hash_token = hashToken;
        this.max_quantity = quantity; // bhai yeh kya hai ?? yr ye tune daal rkhi hai usmai product response mai jo quantity aati hai available wali wo aati hai, bhai par wo to initial jo quantitiy mein aayega wo hi hoga, chodd abhi isse baad mein dekhenge
        this.status = status;
    }

    public String toString() {
        return "Product : \n"+
                "PID : "+this.pid+"\n"+
                "Quantity : "+this.quantity+"\n"+
                "Category ID : "+this.category_id+"\n"+
                "Price : "+this.price+"\n"+
                "Name : "+this.name+"\n"+
                "Hash token : "+this.hash_token+"\n"+
                "Max quantity : "+this.max_quantity+"\n"+
                "Status  : "+this.status+"\n";
    }


    public String getName() { return this.name; }

    public long getQuantity() { return this.quantity; }

    public float getPrice() { return this.getPrice(); }

    public void setQuantity(long quantity) { this.quantity = quantity;  }
}
