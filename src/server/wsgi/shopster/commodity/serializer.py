from rest_framework import serializers
from rest_framework import serializers
from .models import Product, Order, Order_Item, Category


class ProductSerializer(serializers.ModelSerializer):

    class Meta:
        model = Product
        # fields = ('id', 'title', 'code', 'linenos', 'language', 'style')


class OrderItemSerializer(serializers.ModelSerializer):

    class Meta:
        model = Order_Item


# class OrderSerializer(serializers.ModelSerializer):
#     class Meta:
#         model = Order
#         fields = ('order_id', 'product', 'price', 'ordered_on', 'ordered_by')
#         # fields = ('id', 'title', 'code', 'linenos', 'language', 'style')

#     # Create OderedItems and add it Order

class OrderSerializer(serializers.ModelSerializer):
    products = OrderItemSerializer(many=True)

    class Meta:
        model = Order
        fields = ('order_id', 'products', 'price', 'ordered_on',
                  'ordered_by', 'is_completed', 'status')

    def create(self, validated_data):
        # print(validated_data)
        ordered_products = validated_data.pop('products')
        # print(ordered_products)
        order = Order.objects.create(**validated_data)
        order.price = 0
        ordered_items_list = list()
        for product in ordered_products:
            order.price += product["price"]
            # print(product)
            ordered_item = Order_Item.objects.create(**product)
            ordered_item.save()
            ordered_items_list.append(ordered_item)
            order.products.add(ordered_item)
        order.save()
        # print(order.order_id)
        for ordered_item in ordered_items_list:
            ordered_item.order_id = Order.objects.get(order_id=order.order_id)
            ordered_item.save()
        return order

    def update(self, instance, validated_data):
        # Update the Order instance
        print(validated_data)
        instance.status = validated_data['status']
        instance.save()

        # Update status of each product
        for item in validated_data['products']:
            product = Order_Item.objects.get(order_id=instance)
            product.status = instance.status
            product.save()

        return instance


class PostOrderSerializer(serializers.ModelSerializer):
    pass


class OrderItemSerializer(serializers.ModelSerializer):

    class Meta:
        model = Order_Item
        fields = ('product_id', 'ordered_item', 'price',
                  'name', 'quantity', 'status')


class CategorySerializer(serializers.ModelSerializer):

    class Meta:
        model = Category
