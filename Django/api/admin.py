from django.contrib import admin

from .models import UserProfile, Session, University

# Register your models here.
admin.site.register(UserProfile)
admin.site.register(Session)
admin.site.register(University)
