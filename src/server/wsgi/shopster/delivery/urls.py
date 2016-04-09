from django.conf.urls import url
from . import views

urlpatterns = [
    url(r'^api/delivery/$', views.check_delivery_or_inhand),
    url(r'^api/delivery/verify/$', views.delivery_verification),
    url(r'^api/deliveryperson/$', views.delivery_person_list),
    url(r'^api/deliveryperson/(?P<pk>.+)/$', views.delivery_person_detail),
]
