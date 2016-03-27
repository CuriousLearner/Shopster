from django.conf.urls import url
from wallet import views

urlpatterns = [
    url(r'^api/pay/$', views.pay_for_order),
    url(r'^api/recharge/$', views.recharge_wallet),
    url(r'^api/wallet/(?P<pk>[0-9]+)/$', views.get_wallet_details),
]
