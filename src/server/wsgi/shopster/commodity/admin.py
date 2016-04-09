from django.contrib import admin
from .models import Product, Order, Order_Item, Category

# Register your models here.


class ProductAdmin(admin.ModelAdmin):
    list_display = ('pid', 'price', 'name', 'hash_token', 'quantity')


class OrderAdmin(admin.ModelAdmin):
    list_display = ('order_id', 'get_products',
                    'price', 'ordered_on', 'ordered_by')

    # def save_model(self, request, obj, form, change):
    #     # custom stuff here
    #     self.price = sum([p.price for p in self.get_products])
    #     obj.save()


class Order_ItemAdmin(admin.ModelAdmin):
    list_display = ('order_item_id', 'order_id',
                    'product_id', 'quantity', 'status')


class CategoryAdmin(admin.ModelAdmin):
    list_display = ('cat_id', 'cat_name')


admin.site.register(Product, ProductAdmin)
admin.site.register(Order, OrderAdmin)
admin.site.register(Order_Item, Order_ItemAdmin)
admin.site.register(Category, CategoryAdmin)
