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


class Event(models.Model):
    time = models.DateTimeField('event time')
    location_latitude = models.DecimalField(max_digits=12, decimal_places=7)
    location_longitude = models.DecimalField(max_digits=12, decimal_places=7)
    location_name = models.TextField()
    description = models.TextField()
    title = models.TextField()
    image = models.TextField()
    university = models.ForeignKey(University, on_delete=models.CASCADE)
    organizer = models.ForeignKey(UserProfile)
    id = models.CharField(max_length=36, default=uuid.uuid4, primary_key=True)

    def __str__(self):
        return self.title

    def json(self):
        interest = Interest.objects.filter(event=self)

        return {'time': str(self.time), 'location_latitude': str(self.location_latitude), 'image': self.image,
                'location_longitude': str(self.location_longitude), 'location_name': self.location_name,
                'title': self.title, 'description': self.description, 'id': str(self.id),
                'orgranizer': self.organizer.json(), 'num_interested': len(interest)}


class Interest(models.Model):
    event = models.ForeignKey(Event, on_delete=models.CASCADE)
    user = models.ForeignKey(UserProfile, on_delete=models.CASCADE)
    level = models.CharField(max_length=2, default='go')

    def __str__(self):
        return self.user.user.username + ' ' + self.level + ' ' + self.event.title
