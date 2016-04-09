from django.conf.urls import include, url

from django.contrib import admin
admin.autodiscover()

urlpatterns = [
    url(r'^admin/', include(admin.site.urls)),
    url(r'^', include('commodity.urls')),
    url(r'^', include('authen.urls')),
    url(r'^', include('wallet.urls')),
    url(r'^', include('delivery.urls')),
]

admin.site.site_header = 'Shopster Admin Panel'
