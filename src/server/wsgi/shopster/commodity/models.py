from django.db import models
from django.utils import timezone
from django.db.models.signals import pre_save
from django.contrib.auth.models import User
from django.utils.translation import ugettext_lazy as _
from django.dispatch import receiver
from django.db.models.signals import post_save
# from authen.models import User
from shopster import settings
from hashids import Hashids

hashid = Hashids(salt="SDK89nnskUDmsndas", min_length=40)

# Create your models here.
STATUS_CHOICES = (
    ('D', 'Delivered'),
    ('T', 'In-Transit'),
    ('P', 'Packaged'),
    ('O', 'Ordered'),
    ('F', 'Failed'),
)


class Product(models.Model):
    pid = models.AutoField(primary_key=True)
    price = models.PositiveIntegerField()
    name = models.CharField(max_length=100)
    hash_token = models.CharField(max_length=60, blank=True)
    quantity = models.PositiveIntegerField()
    category_id = models.ForeignKey(
        'Category', models.SET_NULL, null=True, blank=True)

    def __str__(self):
        return str(self.name) + ': ' + str(self.price)

    def product_json(self):
        return {
            'pid': self.pid,
            'price': self.price,
            'name': self.name,
            'hash_token': self.hash_token,
            'quantity': self.quantity
        }


@receiver(post_save, sender=Product)
def create_hash_token(sender, instance=None, created=False, **kwargs):
    if created:
        hashed_code = hashid.encode(instance.pid)
        instance.hash_token = str(hashed_code)
        instance.save()



class Order_Item(models.Model):
    order_item_id = models.AutoField(primary_key=True)
    order_id = models.ForeignKey('Order', models.SET_NULL, null=True, blank=True)
    product_id = models.ForeignKey('Product', models.SET_NULL, null=True, blank=True)
    price = models.IntegerField()
    name = models.CharField(max_length=100)
    quantity = models.IntegerField()
    status = models.CharField(choices=STATUS_CHOICES, max_length=1)

    class Meta:
        verbose_name = _('Ordered Item')
        verbose_name_plural = _('Ordered Items')

    def __str__(self):
        return "{} {}".format(self.product_id, self.name)


class Order(models.Model):
    order_id = models.AutoField(primary_key=True)
    # Make this a list of products
    # products = models.ForeignKey('Product', models.SET_NULL, null=True, blank=True)
    products = models.ManyToManyField(Order_Item)
    # products = models.('Product')
    price = models.PositiveIntegerField(default=0)
    ordered_on = models.DateTimeField(default=timezone.now)
    ordered_by = models.ForeignKey(
        settings.AUTH_USER_MODEL, models.SET_NULL, null=True, blank=True)
    status = models.CharField(choices=STATUS_CHOICES, max_length=1, default="O")
    is_completed = models.BooleanField(default=False)

    def get_products(self):
        return ", ".join([str(p) for p in self.products.all()])

    def __str__(self):
        return str(self.order_id) + ': ' + str(self.ordered_by)

    # def save(self, *args, **kwargs):
    #     try:
    #         self.price = sum([p.price for p in self.products.all()])
    #     except:
    #         pass
    #     save_order = super(Order, self).save(*args, **kwargs)

    def product(self):
        prod_list = [p.product_json() for p in self.products.all()]
        return prod_list


# def update_total_price_of_products(**kwargs):
#     # Do something with the Subscription model
#     print(self.price)
#     Order.price = sum([p.price for p in Order.products])


# pre_save.connect(update_total_price_of_products, sender=Order,
#                  dispatch_uid="update_total_price_of_products")




class Category(models.Model):
    cat_id = models.AutoField(primary_key=True)
    cat_name = models.CharField(max_length=20)

    class Meta:
        verbose_name = _('Category')
        verbose_name_plural = _('Categories')

    def __str__(self):
        return '{}: {}'.format(self.cat_id, self.cat_name)
