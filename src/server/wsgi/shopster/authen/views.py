from django.shortcuts import render

from django.http import HttpResponse, HttpResponseRedirect
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from .models import User, USER_TYPE, Group
from .serializers import UserSerializer, OwnerSerializer, AddSerializer
import json
from rest_framework.authentication import TokenAuthentication, SessionAuthentication, BasicAuthentication
from rest_framework.permissions import IsAuthenticated
from rest_framework import status
from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.response import Response
from .forms import LoginForm
from django.contrib.auth import authenticate
from rest_framework.authtoken import views
from rest_framework.authtoken.models import Token

# from .permissions import AdminOnlyPermission
# Create your views here.


class JSONResponse(HttpResponse):

    """
    An HttpResponse that renders its content into JSON.
    """

    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)


@api_view(['GET', 'POST'])
@csrf_exempt
# @authentication_classes((TokenAuthentication, SessionAuthentication, BasicAuthentication))
# @permission_classes((IsAuthenticated))
def person_list(request):
    """
    List all code snippets, or create a new snippet.
    """
    if request.method == 'GET':
        params = {
            'is_active': True
        }
        if 'group' in request.GET:
            try:
                params['groups__name'] = USER_TYPE[request.GET['group']]
            except KeyError:
                return JSONResponse({'message': "invalid group"}, status=400)

        users = User.objects.filter(**params)
        serializer = UserSerializer(users, many=True)
        return JSONResponse(serializer.data)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        print(data)
        serializer = UserSerializer(data=data)
        # print(serializer)
        if serializer.is_valid():
            print(serializer)
            # serializer.address = address
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)


@api_view(['GET', 'DELETE'])
@csrf_exempt
# @authentication_classes((TokenAuthentication, SessionAuthentication, BasicAuthentication))
# @permission_classes((IsAuthenticated,))
def person_detail(request, pk):
    """
    Retrieve, update or delete a code snippet.
    """
    try:
        user = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = UserSerializer(user)
        return JSONResponse(serializer.data)

    elif request.method == 'DELETE':
        user.is_active = False
        user.save()
        return HttpResponse(status=204)


@api_view(['GET'])
def home(request):
    if request.method == 'GET':
        return HttpResponse("API is working")


@api_view(['POST'])
@csrf_exempt
# @authentication_classes((TokenAuthentication, SessionAuthentication, BasicAuthentication))
# @permission_classes((IsAuthenticated,))
def add_group(request, pk):
    try:
        u = User.objects.get(pk=pk)
    except User.DoesNotExist:
        return JSONResponse("User DoesNotExist", status=400)
    else:
        data = JSONParser().parse(request)
        if "group" in data and data['group'] in USER_TYPE:
            stat = u.add_to_group(data['group'])
            if stat:
                return JSONResponse("", status=200)

        return JSONResponse("Invalid Group. options are %s" % (','.join(USER_TYPE.keys())), status=400)


@api_view(['POST'])
@csrf_exempt
# @authentication_classes((TokenAuthentication, SessionAuthentication, BasicAuthentication))
# @permission_classes((IsAuthenticated,))
def add_owner(request):
    # creating like this in this stabdard method
    # need to change properly
    # takes usernmae, email, password
    # creates owner
    response, status = {}, 201

    serializer = OwnerSerializer(data=request.POST)

    if serializer.is_valid():
        try:
            u = serializer.save()
        except KeyError as e:
            response['message'] = str(e)
            status = 401
        except Exception as e:
            response['message'] = str(e)
            status = 400
        else:
            p = request.POST['password']
            u.set_password(p)
            u.save()
            response = {'id': u.id}
    else:
        response = serializer.errors
        status = 400

    return JSONResponse(response, status=status)


# @api_view(['POST'])
# # @authentication_classes((TokenAuthentication, SessionAuthentication, BasicAuthentication))
# # @permission_classes((IsAuthenticated,))
# def login(request, format=None):
#     if request.method == 'POST':
#         # print(request.data)
#         try:
#             username = request.data["username"].strip()
#             password = request.data["password"]
#             # print(type(username))
#         except:
#             username = None
#             password = None
#         try:
#             user = User.objects.get(email=str(username), password=check_password(password))
#         except User.DoesNotExist:
#             return JSONResponse({"Error": "User Does Not Exist"}, status=404)
#         if user:
#             token = Token.objects.get(user=user)
#             content = {
#                 "token": token.key
#             }
#             return JSONResponse(content, status=200)
#         else:
# return JSONResponse({"Error": "Invalid username/password"}, status=401)
