# Simex (Simple Message Exchange) Messaging API
SIMEX (Simple Message Exchange) Messaging is part of the Event Driven Architecture (EPA) using Simex API. 

This software is distributed under **GNU General Public License Version 3.**

Please see the paper **Event Driven Architecture (EDA) using Simple Message Exchange API** for more 
information.

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

There is also a `simex.test.SimexTestFixture` trait that can provide helpful methods:

* `endpoint`
* `client`
* `originator`
* `simexMessage`:- a basic message with no data defined
* `authenticationRequest`:- authentication request with endpoint entity defined as `authorization`
* `refreshTokenRequest`:- a refresh token request
* `getMessage(method: Method, entity: Option[String], data: Vector[Datum])`:- Uses `simexMessage`
* `badSimexJson`:- as the name says!