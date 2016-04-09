from rest_framework import serializers
from .models import DeliveryRequest, DeliveryPerson


class DeliveryPersonSerializer(serializers.ModelSerializer):

    class Meta:
        model = DeliveryPerson


class DeliveryRequestSerializer(serializers.ModelSerializer):

    class Meta:
        model = DeliveryRequest
