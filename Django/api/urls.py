from django.conf.urls import url

import views

urlpatterns = [
    url(r'^users/new/', views.new_user),
    url(r'^universities/all/', views.all_universities),
]
