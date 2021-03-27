# Testing Spring Security

## Summary

This is sample project for testing securing an application
with [Spring Security](https://docs.spring.io/spring-security/site/docs/5.4.5/reference/html5/). The project is setup as
a [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) project. There is also a
official [Getting Started Guide](https://spring.io/guides/gs/securing-web/) for such an application,

An H2 in memory database is used over JPA to persist users with their roles and permissions. H2-console allows to
connect to this database (configured in `application.properties`)

* JDBC URL: jdbc:h2:mem
* User: sa
* Password: sa if you run the following statements there, you get an overview of all users, roles and permissions:

```
SELECT * FROM SEC_USER;
SELECT * FROM SEC_ROLE;
SELECT * FROM SEC_PERMISSION;
```

A simple HTML-Frontend (no additional Frameworks) is provided to simulate communication with a Single Page Application.
In the default configuration the url for this frontend is: http://localhost:8080/frontend/index.html

The jjwt library is used for generating Json Web Tokens (JWTs) to communicate authentication und authorization state to
the frontend. Some configuration properties for JWT handling are configured in `application.properties`

## Roles / Permissions / Authorities

Spring Security uses the generic term authorities to reference roles and permissions. In this
example [`Roles`](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/security/Role.java)
and [`Permissions`](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/security/Permission.java)
are modelled as enum, where
a [`User`](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/security/User.java)
has a collection of roles and a collection of permissions.

Spring Security expects the user model to conform to the interface `UserDetails` with its authorities. Therefore roles
and permissions are mapped to authorities. As a role is itself a collection of permissions, the permissions of all roles
and the individual permissions of a user are aggregated to build the authorities.

### Security Configuration

Most of the security related configuration is done in the
class [WebSecurityConfig](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/WebSecurityConfig.java)
. It configures:

* endpoint related authorization
* additional custom filters for authentication and authorization by JWT.
* customized exception handling
* disabling of cookie based session handling as JWTs are used instead
* disabling of csrf (unnecessary when using JWTs)
* configuring a password encoder (a dummy no ops version is currently configured)
* extended security logging for testing.

Additionally, a Spring
Bean [`MyUserDetailsService`](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/security/MyUserDetailsService.java)
performs the loading of a user from database during authentication and `DelegatingPasswordEncoder` is configured
implementation for password encoding.

### JWT usage

### Sample Users

The
class [UserPopulator](https://github.com/thofis/twrsprsec/blob/main/src/main/java/com/example/thofis/twrsprsec/UserPopulator.java)
creates sample users during startup.


