# Conductor

A load testing tool inspired by orchestra conductors.

"The primary duties of the conductor are to unify performers, set the tempo, execute clear preparations and beats, and to listen critically and shape the sound of the ensemble."

## Objective

The main objective of Conductor is to execute load tests against REST APIs.

## How to install

1. Make a git clone of this repository: `git clone https://github.com/julianespinel/conductor.git`
1. Go to the following directory: conductor/scripts
1. Execute the script named: build-and-run.sh (It will start a new process using the port 9001)
1. Test the service is alive making a GET request to `http://localhost:9001/conductor/admin/ping`
1. If you get `pong` as response that means Conductor is up and running

## How to use

In order to execute a load test, you should define a job request. A job request is a JSON that specificates the load test we want to execute, here is an example:

```json 
{
    "creatorEmail": "yourname@domain.com",
    "concurrencySpecs": {
        "totalCalls": 10,
        "concurrentCalls": 5
    },
    "httpRequestSpecs": {
        "httpMethod": "POST",
        "url": "http://104.131.6.14:4001/services",
        "httpHeaders": {
            "Authorization": "tkn123"
        },
        "httpPayload": {
            "serviceName": "auth",
            "host": "104.131.6.14",
            "port": "8000",
            "protocol": "http",
            "prefix": "auth"
        }
    },
    "payloadKeysToModify": [
        "serviceName",
        "port",
        "prefix"
    ]
}
```

As you can see a JobRequest is composed of 4 main parts:

1. creatorEmail

	Is the email where the load test results will be sent.

2. concurrencySpecs

	Defines the concurrecy specifications of the load test. You have to define 2 things here: the total number of HTTP calls you want Conductor to execute, and how many of them should be executed at the same time.
    
    In this example we wanted to execute 10 HTTP requests but in sets of 5 concurrent calls. It means that the hole test will be executed in two rounds:
    
    1. Round 1: 5 concurrent calls
    1. Round 2: 5 concurrent calls.
       
1. httpRequestSpecs

	Defines the HTTP call that Conductor should execute. It defines:
    
    1. httpMethod: GET, POST, PUT, DELETE.
    1. url: The URL where the service we want to test is located.
    1. httpHeaders: The headers of the HTTP request.
    1. httpPayload: The payload or body of the HTTP request. The current version (v0.1) of Conductor supports JSON payloads only.
    
    It is important to note that if no `httpHeaders` are provided, the system will add `Accept` and `ContentType` headers with value `application/json` by default.

1. payloadKeysToModify

	If we are testing a service that creates an object into a DB (for example), we can't send the same payload in each HTTP request because probably the service will tell us that the object is already created. For this reason is imporant that Conductor generates a different HTTP payload for each request. How do we tell Conductor to do it?
	
    In "payloadKeysToModify" we should define an array that references the keys of the "httpPayload" we want to change on each request. In the example above the array was: 
    
    ```json
    [
        "serviceName",
        "port",
        "prefix"
    ]
    ```
    
    So we wanted to modify the "httpPayload", but only the values of the keys: "serviceName", "port" and "prefix". Conductor will generate the following payloads: 
    
    For the 1st HTTP request:
    
    ```json
    {
        "serviceName": "auth0",
        "host": "104.131.6.14",
        "port": "80000",
        "protocol": "http",
        "prefix": "auth0"
    }
    ```
    
    For the 2nd HTTP request:
    
    ```json
    {
        "serviceName": "auth1",
        "host": "104.131.6.14",
        "port": "80001",
        "protocol": "http",
        "prefix": "auth1"
    }
    ```
    
    For the 3th HTTP request:
    
    ```json
    {
        "serviceName": "auth2",
        "host": "104.131.6.14",
        "port": "80002",
        "protocol": "http",
        "prefix": "auth2"
    }
    ```
    
    And so on until the last call.
    
    As you can see all the generated payloads are the same except for the values of the keys referenced in the "payloadKeysToModify" array: "serviceName", "port" and "prefix". 
    
    How does Conductor modify those values? Currently we only get the original values and append a number to them. This will change in upcoming versions. We will identify data types (strings, numbers, booleans, etc) and make modifications that are more appropriate for each type.

## API

We offer an API with one REST service to create load tests. We will evolve this API in further versions in order to be able to automate load tests creation, management and reporting.

1. Create job request

<pre>
HTTP method: POST
URL:  http://localhost:9001/conductor/api/jobs
Payload: job request json
</pre>

### Examples

#### GET and DELETE requests

HTTP method: `POST`

URL:  `http://localhost:9001/conductor/api/jobs`

Payload:

```json
{
    "creatorEmail": "julianespinel@gmail.com",
    "concurrencySpecs": {
        "totalCalls": 10,
        "concurrentCalls": 5
    },
    "httpRequestSpecs": {
        "httpMethod": "GET",
        "url": "http://104.131.6.14:4001/services",
        "httpHeaders": {
            "Authorization": "tkn123"
        }
    }
}
```

#### POST and PUT requests

HTTP method: `POST`

URL:  `http://localhost:9001/conductor/api/jobs`

Payload:

```json
{
    "creatorEmail": "julianespinel@gmail.com",
    "concurrencySpecs": {
        "totalCalls": 10,
        "concurrentCalls": 5
    },
    "httpRequestSpecs": {
        "httpMethod": "POST",
        "url": "http://104.131.6.14:4001/services",
        "httpHeaders": {
            "Authorization": "tkn123"
        },
        "httpPayload": {
            "serviceName": "auth",
            "host": "104.131.6.14",
            "port": "8000",
            "protocol": "http",
            "prefix": "auth"
        }
    },
    "payloadKeysToModify": [
        "serviceName",
        "port",
        "prefix"
    ]
}
```
