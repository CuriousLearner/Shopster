from django.contrib import admin
from .models import DeliveryRequest, DeliveryPerson

# Register your models here.


class DeliveryRequestAdmin(admin.ModelAdmin):
    list_display = ('queue_id', 'order_id', 'is_delivered',
                    'delivery_type', 'delivered_by')


class DeliveryPersonAdmin(admin.ModelAdmin):
    list_display = ('first_name', 'last_name', 'email', 'status',)


admin.site.register(DeliveryRequest, DeliveryRequestAdmin)
admin.site.register(DeliveryPerson, DeliveryPersonAdmin)
