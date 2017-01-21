import json

from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django.core import serializers
from django.http import HttpResponse, HttpResponseBadRequest, HttpResponseServerError

from .models import University, UserProfile, Session


# Create your views here.


def new_user(request):
    name = request.POST.get('name', False)
    email = request.POST.get('email', False)
    password = request.POST.get('password', False)
    university = request.POST.get('university', False)

    if not name or not email or not password or not university:
        return HttpResponseBadRequest()

    else:
        user = User.objects.create_user(email, email, password, first_name=name)
        user.save()

        user_profile = UserProfile.objects.create(user=user, university=University.objects.get(pk=university))
        user_profile.save()

        user = authenticate(username=email, password=password)

        if user is not None:
            session = Session(user=user_profile)
            session.save()

            return HttpResponse(json.dumps(session.json()),
                                content_type='application/json; charset=utf8')
        else:
            return HttpResponseServerError


def login(request):
    username = request.POST.get('username', False)
    password = request.POST.get('password', False)

    if not username or not password:
        return HttpResponseBadRequest()

    else:
        user = authenticate(username=username, password=password)
        user_profile = UserProfile.objects.get(user=user)

        if user is not None:
            session = Session(user=user_profile)
            session.save()

            return HttpResponse(json.dumps(session.json()),
                                content_type='application/json; charset=utf8')
        else:
            return HttpResponseServerError


def all_universities(request):
    universities = University.objects.all()
    data = []

    for university in universities:
        data.append(university.json())

    return HttpResponse(json.dumps(data),
                        content_type='application/json; charset=utf8')
