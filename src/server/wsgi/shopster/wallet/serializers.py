from rest_framework import serializers
from .models import EWallet


class EWalletSerializer(serializers.ModelSerializer):

    class Meta:
        model = EWallet
        fields = ('owner', 'amount')
