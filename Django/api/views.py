from django.core import serializers
from django.http import JsonResponse

from .models import University


# Create your views here.


def new_user(request):
    return ""


def all_universities():
    return JsonResponse(serializers.serialize('json', University.objects.all()))
