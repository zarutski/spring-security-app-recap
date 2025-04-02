spring-security-basics-recap
==================
Simple web application for security concepts recap. 

Switch between different branches to find different ways of security configuartion. 

Every example will start with empty DB structure.

[mz-420011] 
==================
Simple web example. Entry points for registrations/login: 
* http://localhost:8080/auth/registration
* http://localhost:8080/auth/login

[mz-420011-jwt] 
==================
REST API example using JWT token:
* POST http://localhost:8080/auth/registration
```
{
    "username": "userlame",
    "yearOfBirth": "1980",
    "password": "lastword"
}
```
* POST http://localhost:8080/auth/login
```
{
    "username": "userlame",
    "password": "lastword"
}
```
Response example: 
```
{
    "jwt-token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJVc2VyIGRldGFpbHMiLCJ1c2VybmFtZSI6InVzZXJsYW1lIiwiaWF0IjoxNzQzNjA2NzkyLCJpc3MiOiJzZWNfcmVjYXBfYXBwIiwiZXhwIjoxNzQzNjEwMzkyfQ.fSpF7XBnGoUjN2I1-d6dnGH__KAD9fODMq27JgFctiM"
}
```

* GET http://localhost:8080/show-user-info

To make such request a new header with the key “Authorization” should be added. Value should be “Bearer YOUR_TOKEN”, replaced “YOUR_TOKEN” with the actual bearer token obtained previously.
