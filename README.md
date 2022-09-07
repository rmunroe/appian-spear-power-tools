# Appian SPEaR Team - Power Tools

Brought to you by the Strategic Presales Execution and Readiness (SPEaR) Team in Appian Solutions Consulting


# Introduction

This plugin is the results of over a decade of experience working in the Appian Expression language. 
While the Expression language is extremely powerful and capable as-is, there are some edge cases that "power users"
may run into that require some sophisticated Expression rules to overcome. We have taken some of these sophisticated 
Expression rules and reimplemented them as a plugin for ease of use and speed, both in execution as well as 
time-to-market.


## Issues or Feedback

If you encounter any issues with the plugin, please post in the Appian Community's App Market page for this plugin. 
Additionally, please let us know if there is any additional functionality you would like to see in this plugin and
we will see about getting it added.


# Functions

This section discusses all Expression language functions included in the plugin.


## Date and Time Functions


### SPT_DateTime_ToEpoch
Returns the number of seconds since the standard base time known as "the epoch", namely January 1, 1970, 00:00:00 GMT.

| Parameter | Description |
| ----------- | ----------- |
| dateTime | The Date and Time to get the epoch time for |

### Example
```
a!localVariables(
  local!time: datetime(1976, 8, 11, 8, 30, 0, 10),
  spt_datetime_toepoch(local!time) == 208600200
)
```
Returns `true`


### SPT_DateTime_FromEpoch
Converts the epoch value (the number of seconds since January 1, 1970, 00:00:00 GMT) to a Date and Time.

| Parameter | Description |
| ----------- | ----------- |
| epoch | The epoch time in seconds to get a Date and Time for |

### Example
```
spt_datetime_fromepoch(208600200) = datetime(1976, 8, 11, 8, 30)
```
Returns `true`


### SPT_DateTime_TimeAgo
Returns a text description of the relative duration a given Date and Time was or is from Now.

| Parameter | Description                                                          |
| ----------- |----------------------------------------------------------------------|
| dateTime | The Date and Time to describe                                        |
| locale | Optional locale abbreviation supported by [the PrettyTime library](https://github.com/ocpsoft/prettytime/tree/master/core/src/main/java/org/ocpsoft/prettytime/i18n) |

### Examples
```
a!localVariables(
  local!time: now() - 100,
  spt_datetime_timeago(local!time)
)
```
Returns `"3 months ago"`
```
a!localVariables(
  local!time: now() - 100,
  spt_datetime_timeago(local!time, "de")
)
```
Returns `"vor 3 Monaten"`

## Document Functions

These functions are related to working with Documents in Appian's internal content management system

Both [SPT_Docs_GetUuid](#SPT_Docs_GetUuid) and [SPT_Docs_FromUuid](#SPT_Docs_FromUuid) come from the need for creating 
demonstration Applications that need to be ported to many different Appian instances - something that is rather rare in 
typical Appian production environments - but may be valuable in your use cases as well. When moving Documents from one 
Appian instance to another, the integer Document ID is *not* consistent. Therefore, saving the integer ID to a 
database, then porting that data and the Documents to another instance and using the stored ID to retrieve the Document 
will likely not produce the correct Document, as IDs are assigned at import time. To avoid this, the Document's UUID 
can be used as a unique identifier (as is the intention of a UUID) to quickly retrieve the Document. UUIDs of Documents 
do not ever change, regardless of how many different Appian instances they are deployed to (assuming you are not 
re-importing the raw document file). Consider retrieving a Document's UUID and storing that in a VARCHAR(50) field in 
the database instead. Then use that UUID to fetch a Document instance and use it as you would when storing an integer.


### SPT_Docs_FromUuid
Returns the Appian Document that has the given UUID. Returns null if no Document is found for the given UUID.

| Parameter | Description |
| ----------- | ----------- |
| uuid | The Appian Document's UUID |

### Example
In this example, we are resolving a Document from its UUID as stored in a database and retrieved as a CDT. For further 
illustration we are building a display name from the actual, resolved Document.
```
a!localVariables(
  local!myCdt: rule!ABC_getDocumentCdtById(id: ri!docCdtId),
  local!document: spt_docs_fromuuid(local!myCdt.docUuid),
  local!docDisplay: document(local!document, "name") & "." & document(local!document, "extension"),
  ...
)
```
Returns `true`


### SPT_Docs_GetUuid
Returns the UUID for the given Appian Document

| Parameter | Description |
| ----------- | ----------- |
| document | The Appian Document |

### Example
In this example, we are populating a CDT used to store doc info in a database table with some metadata for quick access, 
and the provided Document's UUID. This CDT would then be saved to the database for later use.
```
a!localVariables(
  local!myCdt: 'type!{urn:com:appian:types:ABC}ABC_Document'(
    docName: document(ri!docToSave, "name"),
    docExt: document(ri!docToSave, "extension"),
    docSize: document(ri!docToSave, "size"),
    docUuid: spt_docs_getuuid(ri!docToSave)
  )
  
  ...
)
```
Returns `true`


## List Functions

These functions are for working with Lists in Appian, aka arrays. Several of them will have similar names to existing 
List functions, but will have specific caveats that help make Lists easier.

### SPT_List_AppendAny
Appends any object to a List without changing its type. If the type of the list and the value being appended do not match, the List is cast to a List of Variant.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to append to |
| value | The value to append |

### SPT_List_Count
Returns the element count (including null elements) in a list. If the passed in value is not a list, or the list is null or empty, returns 0.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to count |

### SPT_List_First
Returns the first element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

### SPT_List_HasDuplicates
Returns true if all items in the List are unique. If not a list or the list is null or empty, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to check |

### SPT_List_IsList
Returns true if the value passed in is a List type. If the passed in value is null, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The value to check |

### SPT_List_Last
Returns the last element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

### SPT_List_RandomElement
Returns a random element in the provided list. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to select from |
| count | The number of elements to include (optional; default is 1) |
| unique | If selecting multiple, ensure that the elements are unique. Will throw an error if count is greater than the number of elements in the array. |

### SPT_List_Randomize
Returns the provided list in a randomized order (shuffled). If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to randomize |

### SPT_List_RemoveNulls
Removes all null elements from the given list. If a List of Text (string) is passed it, removes empty strings ("") as well. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to remove nulls from |

### SPT_List_Slice
Returns a subset of the provided list, starting with and including startIndex and ending with and including endIndex. Returns null if list is not a List type.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to slice |
| startIndex | The first index to include in the slice |
| endIndex | The last index to include in the slice. If omitted, the rest of the list is included. |

### SPT_List_Unique
Returns the unique elements found in the provided list. If the list is null or empty, returns null. If not a List, returns what was passed in. By default, null elements are removed but can be kept by setting keepNulls to true.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to unique |
| keepNulls | The list to unique |


## Object Functions

These functions are for working with data structures in Appian, namely Dictionaries, Maps, and Custom Data Types (CDTs).


### SPT_Object_RemoveNullProperties
Removes properties from a Map or Dictionary where the value is null. If the passed in value is not a Map or Dictionary an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to remove nulls from |
| recursive | If true (default), will recurse into nested objects and remove nulls from them as well |

### SPT_Object_ToDictionary
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Dictionary, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Dictionary |

### SPT_Object_ToMap
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Map, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Map |


## UUID Generation Functions

These functions are for generating UUIDs in Appian.


### SPT_Uuid_Bulk
Creates a list of UUIDs in bulk. Best practice is to know the number of UUIDs to be generated and call this method once in any given Expression evaluation, in order to avoid Appian's caching. (I.e. do not call in a forEach using count of 1 or you will get the same UUID for each call.)

| Parameter | Description |
| ----------- | ----------- |
| count | The number of UUIDs to generate |

### SPT_Uuid_FromString
Creates a UUID using the given string as a seed. The UUID will always be the same for any given string input value.

| Parameter | Description |
| ----------- | ----------- |
| string | The string value to create the UUID from |

### SPT_Uuid_FromStrings
Creates a list of UUIDs using the given strings as a seed. The UUIDs will always be the same for any given string input value.

| Parameter | Description |
| ----------- | ----------- |
| strings | The list of string values to create the UUIDs from |

