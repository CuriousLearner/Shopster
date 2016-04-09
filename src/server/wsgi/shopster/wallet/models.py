from django.db import models
from django.utils import timezone
from commodity.models import Order
from shopster import settings

# Create your models here.
class Coupon(models.Model):
    coupon_id = models.AutoField(primary_key=True)
    price = models.PositiveIntegerField()
    added_on = models.DateTimeField(default=timezone.now)
    used_on = models.DateTimeField(default=timezone.now)
    used_by = models.ForeignKey(settings.AUTH_USER_MODEL, blank=True, null=True)
    coupon_code = models.CharField(max_length=50)
    is_used = models.BooleanField(default=False)

    def __str__(self):
        return str(self.coupon_code) + ': ' + str(self.price)

class EWallet(models.Model):
    owner = models.OneToOneField(settings.AUTH_USER_MODEL)
    amount = models.PositiveIntegerField()
    coupons_used = models.ManyToManyField(Coupon, blank=True)
    orders = models.ManyToManyField(Order, blank=True)

    def get_coupons_used(self):
        return ", ".join([str(p) for p in self.coupons_used.all()])

    def get_orders(self):
        return ", ".join([str(p) for p in self.orders.all()])

    def __str__(self):
        return str(self.owner) + ': ' + str(self.amount)


class Transaction(models.Model):
    t_id = models.AutoField(primary_key=True)
    amount = models.PositiveIntegerField()
    order_id = models.ForeignKey('commodity.Order')
    wallet_id = models.ForeignKey('EWallet')
    is_refunded = models.BooleanField(default=False)

