from django.conf.urls import url

import views

urlpatterns = [
    url(r'^users/new/', views.new_user),
    url(r'^users/login/', views.login),
    url(r'^events/(?P<event_id>[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/interest/add/',
        views.add_interest),
    url(r'^events/(?P<event_id>[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/going/add/',
        views.add_going),
    url(r'^events/(?P<event_id>[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})/delete/',
        views.delete_event),
    url(r'^events/organizing/', views.all_organizing),
    url(r'^events/create/', views.create_event),
    url(r'^events/going/', views.all_going),
    url(r'^events/', views.get_events),
    url(r'^events/interested/', views.all_interested),
    url(r'^universities/all/', views.all_universities),
]
