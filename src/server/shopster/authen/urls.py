from django.conf.urls import url
from authen import views
from rest_framework.authtoken.views import obtain_auth_token

urlpatterns = [
    url(r'^api/user/(?P<pk>[0-9]+)/$', views.person_detail),
    url(r'^api/add_group/(?P<pk>[0-9]+)/$', views.add_group),
    url(r'^api/user/$', views.person_list),
    url(r'^$', views.home),
    url(r'^api/add_owner/$', views.add_owner),
    url(r'^api/login/$', obtain_auth_token),
]
