from django.core import serializers
from django.http import HttpResponse

from .models import University


# Create your views here.


def new_user(request):
    return ''


def all_universities(request):
    return HttpResponse(serializers.serialize('json', University.objects.all()),
                        content_type='application/json; charset=utf8')
