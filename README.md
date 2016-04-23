# Shopster
NFC/RFID/QR Code based shoppping

## General flow for the app hitting different routes:


Registeration:

`POST` /api/user/

Response: User Object

Login: 

`POST` /api/login/

Response: Token for authorization

Use this token in all subsequent requests

Add Product: 

`POST` /api/products/


Customer chooses the products, add to cart, and then create order by:

`POST` /api/checkout/

with every product and order give it's status as "O" - Ordered


Staff marks product as packaged by Updating Order:

`PUT` /api/orders/<order_id>/

give status as "P" - Packaged here

Once packaged, the customer can now get his product by giving delivery choice

"I" - In-hand delivery or "H" - Home Delivery by hitting following URL:

`POST` /api/delivery/

If delivery person is available then he would be alloted to the order or else appropriate status would be returned as response.

Now once the goods are delivered, the delivery person can scan the uhash code from customers app for verification and then hit

`POST` /api/delivery/verify/

which would verify that delivery is done, makes delivery person state idle again and mark ordered as delivered.
