from __future__ import unicode_literals

import uuid

from django.contrib.auth.models import User
from django.db import models


# Create your models here.
class University(models.Model):
    name = models.TextField()

    def __str__(self):
        return self.name

    def json(self):
        return {'id': self.pk, 'name': self.name}


class UserProfile(models.Model):
    user = models.OneToOneField(User)
    university = models.ForeignKey(University)
    id = models.CharField(max_length=36, default=uuid.uuid4, primary_key=True)

    def __str__(self):
        return self.user.username

    def json(self):
        return {'username': self.user.username, 'email': self.user.email, 'name': self.user.first_name,
                'id': str(self.id), 'university': self.university.__str__(), 'university_id': self.university.pk}


class Session(models.Model):
    session_token = models.CharField(max_length=36, default=uuid.uuid4)
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE)

    def __str__(self):
        return str(self.session_token) + ' ' + str(self.user.__str__())

    def json(self):
        return {'session_token': str(self.session_token), 'user': self.user.json()}

    def is_authenticated(self):
        sessions = Session.objects.filter(session_token=self.session_token)

        return len(sessions) == 1
