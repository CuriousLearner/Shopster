package in.co.shopster.shopster.rest.models;

/**
 * Created by ayush on 11/4/16.
 */
public class Recharge {
    String ordered_by, coupon_code;

    public Recharge(String id, String code) {
        this.ordered_by = id;
        this.coupon_code = code;
    }
}
