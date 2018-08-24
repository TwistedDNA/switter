
##Switter

#####General description 

A twitter-like clone with limited functionality.
No registration, authentication or any security.
User gets registered as soon, as he posts first Swit.
Made using Spring Boot and Spring 4 style data flow.
Could have used reactive streams approach but with such thread-unsafe primitive storage 
there is no benefit in non-blocking I/O.
Didn't cover everything with unit tests for sake of time.


Before API explaining, here is Postman collection for ease of testing.
https://www.getpostman.com/collections/fb48f6bd9279813a8ebb


####API

**To post a Swit(message):**
````
POST http://localhost:8080/swits/post
````
Expected body payload example:
```json
{
	"id":"",
	"authorUsername":"twisteddna",
	"text":"I am a practice swit",
	"posted":"2018-08-24T10:57:14.726+0000"
}
```

**To get user wall(last Swits first)**

````
GET http://localhost:8080/users/{username}/wall
````

**To follow another user**

````
GET http://localhost:8080/users/{follower}/follow/{idol}
````

**To get all messages of people you followed**
````
GET http://localhost:8080/users/{username}/timeline
````