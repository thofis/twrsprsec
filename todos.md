* permission and roles as enums/enumsets
* custom user model with set of permissions and roles
* persisting custom user model in DB
* jwt support (https://grobmeier.solutions/de/spring-security-5-jwt-basic-auth.html)
    * store authorities in JWT during authentication to avoid accessing db on every authorization
