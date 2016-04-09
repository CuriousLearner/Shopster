from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^api/delivery/$', views.check_delivery_or_inhand),
    url(r'^api/delivery/verify/$', views.delivery_verification),
]
