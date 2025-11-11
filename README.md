# Simex (Simple Message Exchange) Messaging API
Simex (Simple Message Exchange) Messaging API is a method for two entities to communicate with each other over the network.

It is designed to manage software complexity by:

1. Simex message contains all necessary information for a service to act upon;
2. Simex message does not rely on network protocol or service meta-data;
3. Simex message is network protocol agnostic and can be transmitted from one network protocol to another without any transformation;
4. Simex messages have the same structure regardless of the data.

This software is distributed under **GNU General Public License Version 3.**

Please see the paper **[Managing Software Complexity and Security Research Paper](https://github.com/TheDiscProg/SIMEX-API)** for more 
information.

To use in a SBT project: 
libraryDependencies += "io.github.thediscprog" %% "simex-messaging" % "0.9.5"

## SIMEX Format
The message can be in any format, such as JSON, XML, binary, etc. The manner in which a Simex is transmitted 'across the wire'
is irrelevant. However, the format of the message is important. This library comes with JSON encoders and decoders.

In the following description of the message, an important concept to realise is that the message either originates from
or has the destination of an `endpoint` - that is a service that has made a request of a service that handles the request, equivalent
to a REST endpoint, except that the requester also has a defined endpoint.

The message has the following fields:
### destination
The `Endpoint` that will eventually handle the request. This `endpoint` acts as an `orchestrator` that will prepare the 
response to the `endpoint` that made the request, the `sourceEndpoint.`
The `Endpoint` defines four fields:
1. The resource - this is the unique identifier for the `endpoint` that handles the request.
2. The method - that defines the action that the endpoint should carry out
3. The entity (optional) - a business entity on which the action should be carried out on.
4. The version - defaults to 'v1' but allows the receiver to handle multiple versions of a request.

### client
The `Endpoint` that generated the request. In order for the right client to receive the message, and for the client to
identify the response, two fields are required: `clientId` which must be unique across all the system, and `requestId` that 
must be unique for each client.
In addition to the above two fields, two additional fields are defined:
1. `sourceEndpoint` - the endpoint that sent this request
2. `authorization` - the security token that authenticates the request

### originator
As the `client` can change as the request is handled across multiple endpoints, the `originator` defines the original client
that made this request. It has the following fields:
1. `clientId` - ID of the client; must be unique
2. `requestId` - ID of the original request
3. `sourceEndpoint` - the `endpoint` that generated the request
4. `originalToken` - the original security token that was included in request, can be same as the `client.authorization`
5. `security` - the security level of this message*
6. `messageTTL` - the time to keep the response, and not the request, alive before discarding it*

*These values can be set by the client but can also be overridden by the 'handling' endpoint.

Normally, the client that made the original request sets the `originator` fields. As other endpoints handle the request,
the `originator` field remains unchanged.

### data
This is a list of `datum`:

* `field` - the name of the field, i.e. `surname` etc.
* `check` (optional) - optional checks as determined by the application. An example would be `equal to` in a SQL query.
* `value` - Either a value, or another list of `datum` as defined by `Xor` (Exclusive OR), but not both.

This data should be sufficient for the destination endpoint to carry out the request and prepare the response.

## How to use

`Simex` class is the message used to communicate intention and data. Instances have the following helpful APIs:

* `getUsername`:- Gets an option of username defined in the data
* `getPassword`:- Gets an option of password defined in the data
* `getAuthorization`:- Gets the authorization token sent by the client
* `getRefreshToken`:- Gets an option the refresh token
* `replaceDatum(datum: Datum)`:- Replaces the datum in the data vector
* `extractDatumByFieldname(field: String)`:- Gets an option of the datum with the matching field name
* There is a `checkEndPointValidity` method in the class object to check for endpoint validity

`Simex` object class also has the following helpful methods:

* `serializeToString`:- Converts the message into a JSON formatted string
* `deSerializeFromString`:- Converts a JSON string back into a Simex message. The result is an `Either[ConversionError, Simex]`
* `checkEndPointValidity(message: Simex)`:- Checks that the endpoint has the minimum values defined
* Available in the class object are CIRCE JSON encoder and decoder

## Using Simex Messaging in your application
This is available in Github repository - see the Github page on how to include it in your application.

There is also a `SimexTestFixture` trait that can provide helpful methods:

* `endpoint`
* `client`
* `originator`
* `simexMessage`:- a basic message with no data defined
* `authenticationRequest`:- authentication request with endpoint entity defined as `authorization`
* `refreshTokenRequest`:- a refresh token request
* `getMessage(method: Method, entity: Option[String], data: Vector[Datum])`:- Uses `simexMessage`
* `badSimexJson`:- as the name says!

## Method
The Method class defines all the methods supported in SIMEX API and has the following helpful methods:
* `values`: a list of all Method objects
* `fromString(s: String)`: returns the corresponding Method object

## Security
The Security object defines the different security levels defined in SIMEX and has the following helpful methods:
* `values`: a list of all defined security levels
* `fromString(s: String)`: returns the corresponding Security object

The security object itself has the following values:
* `level`: a string of either "1", "2" or "3"