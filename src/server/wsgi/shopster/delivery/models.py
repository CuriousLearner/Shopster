from __future__ import unicode_literals

from django.db import models
from authen.models import User


# Create your models here.
STATUS_CHOICES = (
    ('D', 'Delivering'),
    ('A', 'Absent'),
    ('I', 'Idle')
)

DELIVERY_TYPE_CHOICES = (
    ('H', 'Home-Delivery'),
    ('I', 'In-Hand Delivery'),
)


class DeliveryRequest(models.Model):
    queue_id = models.AutoField(primary_key=True)
    order_id = models.ForeignKey(
        'commodity.Order', models.SET_NULL, null=True, blank=True)
    delivery_type = models.CharField(choices=DELIVERY_TYPE_CHOICES,
                                     max_length=1)
    delivered_by = models.ForeignKey(
        'DeliveryPerson', models.SET_NULL, null=True, blank=True)
    is_delivered = models.BooleanField(default=False)


class DeliveryPerson(User):
    status = models.CharField(choices=STATUS_CHOICES, max_length=1)


