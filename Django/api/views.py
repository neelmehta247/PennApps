from django.core import serializers
from django.http import JsonResponse
from django.http import HttpResponse

from .models import University


# Create your views here.


def new_user(request):
    return ""


def all_universities(request):
    #return JsonResponse(serializers.serialize('json', University.objects.all()))
    return HttpResponse(len(University.objects.all()))
