from django.conf.urls import url

import views

urlpatterns = [
    url(r'^users/new', views.new_user),
]
