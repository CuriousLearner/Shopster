from django.shortcuts import render
from django.http import HttpResponse
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from rest_framework.decorators import api_view, authentication_classes, permission_classes
from rest_framework.authentication import TokenAuthentication
from rest_framework.permissions import IsAuthenticated

from commodity.models import Order
from .models import DeliveryRequest, DeliveryPerson
from authen.models import User

from .serializers import DeliveryPersonSerializer

# Create your views here.


class JSONResponse(HttpResponse):
    """
    An HttpResponse that renders its content into JSON.
    """

    def __init__(self, data, **kwargs):
        content = JSONRenderer().render(data)
        kwargs['content_type'] = 'application/json'
        super(JSONResponse, self).__init__(content, **kwargs)


@csrf_exempt
@api_view(['POST', ])
@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated,))
def check_delivery_or_inhand(request):
    '''
    When packaged:
        Ask Customer, deliver or in-hand
        if home delivery:
            Add these paid orders to Delivery QUEUE
            Check Delivery Person Idle
            Assign it to order
            Make Delivery Person Delivering
            Order State In-Transit

    '''
    if request.method == 'POST':
        data = JSONParser().parse(request)
        order_id = data["order_id"]
        try:
            order = Order.objects.get(order_id=order_id)
        except Order.DoesNotExist:
            content = {"Message": "Order does not exist"}
            return JSONResponse(content, status=404)
        # Check if order already delivered
        if order.status == 'D':
            content = {"Message": "Order already delivered"}
            return JSONResponse(content, status=200)
        # Check if Order is Packaged
        if order.status == 'P':
            delivery_type = data["delivery_type"]
            is_delivered = False
            delivery_request = DeliveryRequest(order_id=order,
                                               delivery_type=delivery_type,
                                               is_delivered=is_delivered)
            delivery_request.save()
            # If home delivery, then find idle delivery person and
            # assign him the order
            if delivery_type == 'H':  # Home Delivery
                # Getting all Delivery Person that are IDLE
                delivery_person_qs = DeliveryPerson.objects.filter(status='I')
                idle_delivery_persons = list(delivery_person_qs[:1])
                if idle_delivery_persons:
                    delivery_person = idle_delivery_persons[0]
                else:
                    delivery_person = None
                    content = {"Message": "No Delivery Person available"}
                    return JSONResponse(content, status=200)
                if delivery_person:
                    delivery_request.delivered_by = delivery_person
                    delivery_request.save()
                    order.status = 'T'  # In-Transit
                    order.save()
                    delivery_person.status = 'D'  # Delivering
                    delivery_person.save()
                    content = {"Message": "Delivery Person Assigned"}
                    return JSONResponse(content, status=200)
            else:
                # Delivery type is not home delivery
                content = {"Message": "No Delivery Person\n InHand Delivery"}
                return JSONResponse(content, status=200)
        else:
            content = {"Message": "Order is not packaged yet!"}
            return JSONResponse(content, status=403)


@csrf_exempt
@api_view(['POST', ])
@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated,))
def delivery_verification(request):
    '''
    Verification:
    User hash Customer
    If order_by == user_hash:
        order state delivered
    else:
        order state failed
    '''
    if request.method == 'POST':
        data = JSONParser().parse(request)
        order_id = data["order_id"]
        uhash_token = data["uhash_token"]
        dp = data["delivery_person"]
        order = Order.objects.get(order_id=order_id)
        user_mail = str(order.ordered_by)
        user = order.ordered_by
        delivery_person = DeliveryPerson.objects.get(id=dp)
        ordered_by_user_hash = user.uhash_token
        if uhash_token == ordered_by_user_hash:
            # Delivery Confirmed
            order.status = 'D'  # Delivered
            delivery_person.status = 'I'  # Idle
            order.save()
            delivery_person.save()
            content = {"Message": "Delivered"}
            return JSONResponse(content, status=200)
        else:
            content = {"Message": "Customer not matched"}
            return JSONResponse(content, status=200)


@csrf_exempt
@api_view(['GET', 'POST'])
@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated,))
def delivery_person_list(request):
    """
    List all products, or create a new product.
    """
    if request.method == 'GET':
        delivery_person = DeliveryPerson.objects.all()
        serializer = DeliveryPersonSerializer(delivery_person, many=True)
        return JSONResponse(serializer.data)

    elif request.method == 'POST':
        data = JSONParser().parse(request)
        serializer = DeliveryPersonSerializer(data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data, status=201)
        return JSONResponse(serializer.errors, status=400)


@csrf_exempt
@api_view(['GET', 'PUT', 'DELETE'])
@authentication_classes((TokenAuthentication,))
@permission_classes((IsAuthenticated,))
def delivery_person_detail(request, pk):
    """
    Retrieve, update or delete a Product.
    """
    try:
        delivery_person = DeliveryPerson.objects.get(hash_token=pk)
    except DeliveryPerson.DoesNotExist:
        return HttpResponse(status=404)

    if request.method == 'GET':
        serializer = DeliveryPersonSerializer(delivery_person)
        return JSONResponse(serializer.data)

    elif request.method == 'PUT':
        data = JSONParser().parse(request)
        serializer = DeliveryPersonSerializer(delivery_person, data=data)
        if serializer.is_valid():
            serializer.save()
            return JSONResponse(serializer.data)
        return JSONResponse(serializer.errors, status=400)

    elif request.method == 'DELETE':
        delivery_person.delete()
        return HttpResponse(status=204)
