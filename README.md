# DAPEX Messaging
DAPEX (Data Access and Process Execution) Messaging is part of the Event Driven Architecture (EPA) using DAPEX. 

This software is distributed under **GNU General Public License Version 3.**

Please see the paper **Event Driven Architecture (EDA) using Data Access and Process Execution (DAPEX) Message** for more 
information.

## How to use

`DapexMessage` has the following methods:

* `serializeToString`: Converts the message into a JSON formatted string for
* `deSerializeFromString`: Converts a JSON string back into a Dapex message. The result is an `Either[ConversionError, DapexMessage]`
* There are a number of validity check methods but perhaps the most important is `isValid(message: DapexMessage)`
* Available in the object are CIRCE JSON encoder and decoder.

