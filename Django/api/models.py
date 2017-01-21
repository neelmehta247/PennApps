from __future__ import unicode_literals

import uuid

from django.contrib.auth.models import User
from django.db import models


# Create your models here.
class Session(models.Model):
    session_token = models.CharField(max_length=36, default=uuid.uuid4)
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE)


class UserProfile(models.Model):
    user = models.ForeignKey(User, unique=True)
    university = models.ForeignKey(University)


class University(models.Model):
    name = models.TextField()
