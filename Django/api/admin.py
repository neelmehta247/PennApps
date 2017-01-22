from django.contrib import admin

from .models import UserProfile, Session, University, Event, Interest

# Register your models here.
admin.site.register(UserProfile)
admin.site.register(Session)
admin.site.register(University)
admin.site.register(Interest)
admin.site.register(Event)
