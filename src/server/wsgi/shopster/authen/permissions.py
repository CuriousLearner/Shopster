from rest_framework import permissions
from django.contrib import auth


class AdminOnlyPermission(permissions.BasePermission):
    """
    Global permission check for blacklisted IPs.
    """

    def has_permission(self, request, view):
        print(request.user)
        print(request.user.groups)
        # OWNER
        print()
        if str(request.user.groups) == 'auth.Group.OWNER':
            return request.user
