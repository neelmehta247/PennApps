from __future__ import print_function

from datetime import datetime as dt
import json
import uuid

import boto3
from botocore.client import Config
from django.contrib.auth import authenticate
from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist
from django.http import HttpResponse, HttpResponseBadRequest, HttpResponseServerError

from .models import University, UserProfile, Session, Event, Interest


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

    send_data = {'data': data}

    return HttpResponse(json.dumps(send_data),
                        content_type='application/json; charset=utf8')


def add_interest(request, event_id):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

        event = Event.objects.get(id=event_id)

        try:
            interest = Interest.objects.get(event=event, user=user)
        except ObjectDoesNotExist:
            interest = None

        if interest is not None:
            interest.delete()

        interest = Interest(event=event, user=user, level='in')
        interest.save()

    return HttpResponse(json.dumps({'meta': 'success'}), content_type='application/json; charset=utf8')


def add_going(request, event_id):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

        event = Event.objects.get(id=event_id)

        try:
            interest = Interest.objects.get(event=event, user=user)
        except ObjectDoesNotExist:
            interest = None

        if interest is not None:
            interest.delete()

        interest = Interest(event=event, user=user, level='go')
        interest.save()

    return HttpResponse(json.dumps({'meta': 'success'}), content_type='application/json; charset=utf8')


def all_organizing(request):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

        events = Event.objects.filter(organizer=user)

        data = []

        for event in events:
            data.append(event.json())

        send_data = {'data': data}

        return HttpResponse(json.dumps(send_data),
                            content_type='application/json; charset=utf8')


def get_events(request):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

        events = Event.objects.filter(university=user.university)

        data = []

        for event in events:
            data.append(event.json())

        send_data = {'data': data}

        return HttpResponse(json.dumps(send_data),
                            content_type='application/json; charset=utf8')


def all_going(request):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

    events = map((lambda x: x.event), Interest.objects.filter(user=user))

    data = []

    for event in events:
        if event.level == 'go':
            data.append(event.json())

    send_data = {'data': data}

    return HttpResponse(json.dumps(send_data),
                        content_type='application/json; charset=utf8')


def all_interested(request):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

    events = map((lambda x: x.event), Interest.objects.filter(user=user))

    data = []

    for event in events:
        if event.level == 'in':
            data.append(event.json())

    send_data = {'data': data}

    return HttpResponse(json.dumps(send_data),
                        content_type='application/json; charset=utf8')


def delete_event(request, event_id):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

    event = Event.objects.get(id=event_id)

    if event.organizer is not user:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'you are not the organiser'}), status=631,
                            content_type='application/json; charset=utf8')

    event.delete()
    return HttpResponse(json.dumps({'meta': 'success', 'message': 'deleted'}),
                        content_type='application/json; charset=utf8')


def create_event(request):
    session_token = request.META.get('HTTP_SESSION_TOKEN', False)

    if not session_token:
        return HttpResponse(json.dumps({'meta': 'failed', 'message': 'no session token'}), status=391,
                            content_type='application/json; charset=utf8')

    else:
        try:
            user = (Session.objects.get(session_token=session_token)).user
        except ObjectDoesNotExist:
            return HttpResponse(json.dumps({'meta': 'failed', 'message': 'session token not valid'}), status=728,
                                content_type='application/json; charset=utf8')

    time = request.POST.get('time', False)
    latitude = request.POST.get('latitude', False)
    longitude = request.POST.get('longitude', False)
    location_name = request.POST.get('location_name', False)
    description = request.POST.get('description', False)
    title = request.POST.get('title', False)
    image_file = request.FILES.get('image', False)

    if not time or not latitude or not longitude or not location_name or not description or not title or not image_file:
        return HttpResponseBadRequest()

    image_uuid = str(uuid.uuid4()) + '.png'

    s3 = boto3.resource('s3', config=Config(signature_version='s3v4'))
    s3.Bucket('pennapps-17-us-standard').put_object(Key=image_uuid, Body=image_file)

    s3 = boto3.client('s3', config=Config(signature_version='s3v4'))
    image = s3.generate_presigned_url(
        ClientMethod='get_object',
        Params={
            'Bucket': 'pennapps-17-us-standard',
            'Key': image_uuid,
        })

    event = Event(time=dt.fromtimestamp(int(time)), university=user.university, organizer=user,
                  description=description, title=title, location_name=location_name, location_latitude=latitude,
                  location_longitude=longitude, image=image)
    event.save()

    return HttpResponse(json.dumps(event.json()),
                        content_type='application/json; charset=utf8')
