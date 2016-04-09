from django.shortcuts import render
from django.http import HttpResponse
from datetime import datetime
from django.views.decorators.csrf import csrf_exempt
from rest_framework.renderers import JSONRenderer
from rest_framework.parsers import JSONParser
from rest_framework.decorators import api_view


from .models import Order, EWallet, Transaction, Coupon
from authen.models import User
from .serializers import EWalletSerializer

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
def pay_for_order(request):
    '''
    Pay for the given order
    Does the following:
    -Make order.is_completed True
    -create transaction
    -deduct balance from wallet
    '''
    if request.method == 'POST':
        data = JSONParser().parse(request)
        '''
        {
            "order_id": 17,
            "ordered_by": 1
        }
        '''
        order_id = data["order_id"]
        ordered_by = data["ordered_by"]
        content = {"Message": ""}
        user = User.objects.get(id=ordered_by)
        # print(user)
        wallet = EWallet.objects.get(owner=user)
        order = Order.objects.get(order_id=order_id)
        if order.is_completed:
            content = {"Message": "Payment already done for this order"}
            return JSONResponse(content, status=200)
        # print(order)
        amount_to_deduct = order.price
        # print(wallet.amount)
        if wallet.amount < order.price:
            content = {"Message": "Wallet balance low. Please re-charge"}
            return JSONResponse(content,status=200)
        else:
            # Create Transaction and deduct balance
            Transaction.objects.create(wallet_id=wallet, amount=amount_to_deduct, order_id=order)
            wallet.amount -= order.price
            wallet.save()
            order.is_completed = True
            order.save()
            content = {"Message": "Balance deducted"}
            return JSONResponse(content, status=200)
        # print(amount_to_deduct)
        # print(wallet)
        # print(data)
        return JSONResponse(content,status=200)


@csrf_exempt
def recharge_wallet(request):
    if request.method == 'POST':
        data = JSONParser().parse(request)
        content = {"Message": ""}
        coupon_code = data["coupon_code"]
        ordered_by = data["ordered_by"]
        try:
            coupon = Coupon.objects.get(coupon_code=coupon_code)
        except:
            content = {"Message": "Coupon does not exist"}
            return JSONResponse(content,status=200)
        if coupon.is_used:
            content = {"Message": "Coupon already used"}
            return JSONResponse(content,status=200)
        user = User.objects.get(id=ordered_by)
        wallet = EWallet.objects.get(owner=user)
        wallet.amount += coupon.price
        wallet.save()
        coupon.used_by = user
        coupon.used_on = datetime.now()
        coupon.price = 0
        coupon.is_used = True
        coupon.save()
        content = {"Message": "Recharged"}
        return JSONResponse(content, status=200)


@csrf_exempt
def get_wallet_details(request, pk):
    if request.method == 'GET':
        try:
            wallet = EWallet.objects.get(owner=pk)
        except EWallet.DoesNotExist:
            return HttpResponse(status=404)

        if request.method == 'GET':
            serializer = EWalletSerializer(wallet)
            return JSONResponse(serializer.data)

