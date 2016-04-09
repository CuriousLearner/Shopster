from django.contrib import admin
from .models import Coupon, EWallet, Transaction

# Register your models here.


class CouponAdmin(admin.ModelAdmin):
    list_display = ('coupon_id', 'coupon_code', 'price', 'added_on', 'used_on', 'used_by')


class EWalletAdmin(admin.ModelAdmin):
    list_display = ('owner', 'amount')


class TransactionAdmin(admin.ModelAdmin):
    list_display = ('t_id', 'wallet_id', 'amount', 'order_id')


admin.site.register(Coupon, CouponAdmin)
admin.site.register(EWallet, EWalletAdmin)
admin.site.register(Transaction, TransactionAdmin)
