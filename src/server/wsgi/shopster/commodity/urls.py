from django.conf.urls import url
from commodity import views

urlpatterns = [
    url(r'^api/products/$', views.product_list),
    url(r'^api/products/(?P<pk>.+)/$', views.product_detail),
    url(r'^api/orders/$', views.order_list),
    url(r'^api/orders/(?P<pk>[0-9]+)/$', views.order_detail),
    url(r'^api/checkout/$', views.checkout),
]
