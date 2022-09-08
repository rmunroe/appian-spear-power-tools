<p align="center">
  <img src="https://raw.githubusercontent.com/rmunroe/appian-spear-power-tools/main/resources/img/spear_logo.png">
</p>


# SPEaR Power Tools

Brought to you by the Strategic Presales Execution and Readiness (SPEaR) Team in Appian Solutions Consulting


# Introduction

This plugin is the results of over a decade of experience working in the Appian Expression language. 
While the Expression language is extremely powerful and capable as-is, there are some edge cases that "power users" Appian Designers
may run into that require some sophisticated Expression rules to overcome. We have taken some of these sophisticated 
Expression rules and reimplemented them as a plugin for ease of use and speed, both in execution as well as 
time-to-market.


## Issues or Feedback

If you encounter any issues with the plugin, please post in the Appian Community's App Market page for this plugin. 
Additionally, please let us know if there is any additional functionality you would like to see in this plugin and
we will see about getting it added.

# Table of Contents
## Functions
### Date and Time Functions
* [SPT_DateTime_ToEpoch](#SPT_DateTime_ToEpoch)
* [SPT_DateTime_FromEpoch](#SPT_DateTime_FromEpoch)
* [SPT_DateTime_TimeAgo](#SPT_DateTime_TimeAgo)

### Document Functions
* [SPT_Docs_GetUuid](#SPT_Docs_GetUuid)
* [SPT_Docs_FromUuid](#SPT_Docs_FromUuid)

### List (Array) Functions
* [SPT_List_AppendAny](#SPT_List_AppendAny)
* [SPT_List_Count](#SPT_List_Count)
* [SPT_List_First](#SPT_List_First)
* [SPT_List_HasDuplicates](#SPT_List_HasDuplicates)
* [SPT_List_IsList](#SPT_List_IsList)
* [SPT_List_Last](#SPT_List_Last)
* [SPT_List_RandomElement](#SPT_List_RandomElement)
* [SPT_List_Randomize](#SPT_List_Randomize)
* [SPT_List_RemoveNulls](#SPT_List_RemoveNulls)
* [SPT_List_Slice](#SPT_List_Slice)
* [SPT_List_Unique](#SPT_List_Unique)

### Object Functions
* [SPT_Object_RemoveNullProperties](#SPT_Object_RemoveNullProperties)
* [SPT_Object_ToDictionary](#SPT_Object_ToDictionary)
* [SPT_Object_ToMap](#SPT_Object_ToMap)

### UUID Generation Functions
* [SPT_Uuid_Bulk](#SPT_Uuid_Bulk)
* [SPT_Uuid_FromString](#SPT_Uuid_FromString)
* [SPT_Uuid_FromStrings](#SPT_Uuid_FromStrings)


# Functions

This section discusses all Expression language functions included in the plugin.


## Date and Time Functions


### SPT_DateTime_ToEpoch
Returns the number of seconds since the standard base time known as "the epoch", namely January 1, 1970, 00:00:00 GMT.

| Parameter | Description |
| ----------- | ----------- |
| dateTime | The Date and Time to get the epoch time for |

#### Example
```
a!localVariables(
  local!time: datetime(1976, 8, 11, 8, 30, 0, 10),
  fn!spt_datetime_toepoch(local!time) == 208600200
)
```
Returns `true`


### SPT_DateTime_FromEpoch
Converts the epoch value (the number of seconds since January 1, 1970, 00:00:00 GMT) to a Date and Time.

| Parameter | Description |
| ----------- | ----------- |
| epoch | The epoch time in seconds to get a Date and Time for |

#### Example
```
fn!spt_datetime_fromepoch(208600200) = datetime(1976, 8, 11, 8, 30)
```
Returns `true`


### SPT_DateTime_TimeAgo
Returns a text description of the relative duration a given Date and Time was or is from Now.

| Parameter | Description                                                          |
| ----------- |----------------------------------------------------------------------|
| dateTime | The Date and Time to describe                                        |
| locale | Optional locale abbreviation supported by [the PrettyTime library](https://github.com/ocpsoft/prettytime/tree/master/core/src/main/java/org/ocpsoft/prettytime/i18n) |

#### Examples
```
a!localVariables(
  local!time: now() - 100,
  fn!spt_datetime_timeago(local!time)
)
```
Returns `"3 months ago"`
```
a!localVariables(
  local!time: now() - 100,
  fn!spt_datetime_timeago(local!time, "de")
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
the database instead of it's integer Document ID. Then use that UUID to fetch a Document instance and use it as you 
would when storing an integer Document ID.


### SPT_Docs_GetUuid
Returns the UUID for the given Appian Document

| Parameter | Description |
| ----------- | ----------- |
| document | The Appian Document |

#### Example
In this example, we are populating a CDT used to store doc info in a database table with some metadata for quick access, 
and the provided Document's UUID. This CDT would then be saved to the database for later use.
```
a!localVariables(
  local!myCdt: 'type!{urn:com:appian:types:ABC}ABC_Document'(
    docName: document(ri!docToSave, "name"),
    docExt: document(ri!docToSave, "extension"),
    docSize: document(ri!docToSave, "size"),
    docUuid: fn!spt_docs_getuuid(ri!docToSave)
  ),
  
...
```


### SPT_Docs_FromUuid
Returns the Appian Document that has the given UUID. Returns null if no Document is found for the given UUID.

| Parameter | Description |
| ----------- | ----------- |
| uuid | The Appian Document's UUID |

#### Example
In this example, we are resolving a Document from its UUID as stored in a database and retrieved as a CDT. For further
illustration we are building a display name from the actual, resolved Document.
```
a!localVariables(
  local!myCdt: rule!ABC_getDocumentCdtById(id: ri!docCdtId),
  local!document: fn!spt_docs_fromuuid(local!myCdt.docUuid),
  local!docDisplay: document(local!document, "name") & "." & document(local!document, "extension"),

...
```


## List (Array) Functions

These functions are for working with Lists in Appian, aka arrays. Several of them will have similar names to existing 
List functions, but will have specific caveats that help make Lists easier.


### SPT_List_AppendAny
Appends any value to a List without changing the value's type. If the type of the list and the value being appended do not match, the List is cast to a List of Variant.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to append to |
| value | The value to append |


#### NOTE: Nulls and Empty Strings

Appian converts the `null` value to an empty string `""` when providing the value to a function plugin. It is therefore
impossible to tell which value (`null` vs `""`) the user actually meant.

_**When an `Any Type` value is provided, this plugin will default to both `null` and `""` being intended as `null`.**_

For instance if you use [SPT_List_AppendAny](#SPT_List_AppendAny) to append `""` onto a List, instead of an empty string `""`
being appended, the value `null` is used. See the example given below.

#### Examples
```
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  
  fn!spt_list_appendany(local!listOfTextString, 4)
)
```
Returns (List of Variant) `{"one", "two", "three", 4}`
```
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  
  fn!spt_list_appendany(local!listOfTextString, "")
)
```
Returns (List of Variant) `{"one", "two", "three", null}`


### SPT_List_Count
Returns the element count (including null elements) in a list. If the passed in value is not a list, or the list is null or empty, returns 0.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to count |

#### Examples
```
a!localVariables(
  local!nullValue: null,
  fn!spt_list_count(local!nullValue)
)
```
Returns `0`
```
a!localVariables(
  local!stringValue: "stringValue",
  fn!spt_list_count(local!stringValue)
)
```
Returns `0`
```
a!localVariables(
  local!hundredElementArray: enumerate(100),
  fn!spt_list_count(local!hundredElementArray)
)
```
Returns `100`


### SPT_List_First
Returns the first element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

#### Examples
```
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  fn!spt_list_first(local!listOfTextString)
)
```
Returns `"one"`
```
a!localVariables(
  local!notAnArray: "notAnArray",
  fn!spt_list_first(local!notAnArray)
)
```
Returns `"notAnArray"`


### SPT_List_HasDuplicates
Returns true if all items in the List are unique. If not a list or the list is null or empty, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to check |

#### Examples
```
a!localVariables(
  local!listOfPrimitive: { 1, 2, 3, 5, 3, 3, 4, 5, 5, 5 },
  fn!spt_list_hasduplicates(local!listOfPrimitive)
)
```
Returns `true`
```
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },
  fn!spt_list_hasduplicates(local!listOfCdt)
)
```
Returns `true`
```
fn!spt_list_hasduplicates(enumerate(10))
```
Returns `false`
```
fn!spt_list_hasduplicates(123)
```
Returns `false`
```
fn!spt_list_hasduplicates(null)
```
Returns `false`


### SPT_List_IsList
Returns true if the value passed in is a List type. If the passed in value is null, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The value to check |

#### Examples
```
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },
  fn!spt_list_islist(local!listOfCdt)
)
```
Returns `true`
```
a!localVariables(
  local!hundredElementList: enumerate(100),
  fn!spt_list_islist(local!hundredElementList)
)
```
Returns `true`
```
a!localVariables(
  local!emptyList: {},
  fn!spt_list_islist(local!emptyList)
)
```
Returns `true`
```
a!localVariables(
  local!stringValue: "stringValue",
  fn!spt_list_islist(local!stringValue)
)
```
Returns `false`
```
a!localVariables(
  local!nullValue: null,
  fn!spt_list_islist(local!nullValue)
)
```
Returns `false`


### SPT_List_Last
Returns the last element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

#### Examples
```
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  fn!spt_list_last(local!listOfTextString)
)
```
Returns `"three"`
```
a!localVariables(
  local!notAnArray: "notAnArray",
  fn!spt_list_last(local!notAnArray)
)
```
Returns `"notAnArray"`


### SPT_List_RandomElement
Returns a random element in the provided list. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to select from |
| count | The number of elements to include (optional; default is 1) |
| unique | If selecting multiple, ensure that the elements are unique. Will throw an error if count is greater than the number of elements in the array. |

#### Example
```
a!localVariables(
  local!listOfMap: {
    a!map(id: 1, value: a!map(subValue: "first map")),
    a!map(id: 2, value: "second map"),
    a!map(id: 3, value: "third map"),
    a!map(id: 4, value: a!map(subValue: "fourth map")),
    a!map(id: 5, value: "fifth map"),
    a!map(id: 6, value: "sixth map"),
  },
  
  a!map(
    one: fn!spt_list_randomelement(local!listOfMap),
    unique3: fn!spt_list_randomelement(local!listOfMap, 3, true),
    ten: fn!spt_list_randomelement(local!listOfMap, 10)
  )
)
```
Returns a Map with:
* a single random element
* 3 unique elements
* a List of 10 random elements


### SPT_List_Randomize
Returns the provided list in a randomized order (shuffled). If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to randomize |

#### Example
```
a!localVariables(
  local!listOfMap: {
    a!map(id: 1, value: a!map(subValue: "first map")),
    a!map(id: 2, value: "second map"),
    a!map(id: 3, value: "third map"),
    a!map(id: 4, value: a!map(subValue: "fourth map")),
    a!map(id: 5, value: "fifth map"),
    a!map(id: 6, value: "sixth map"),
  },
  
  fn!spt_list_randomize(local!listOfMap).id
)
```
Returns the list of `id` properties, in random order. E.g. `{2, 3, 6, 4, 1, 5}`

### SPT_List_RemoveNulls
Removes all null elements from the given list. If a List of Text (string) is passed it, removes empty strings ("") as well. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to remove nulls from |

#### Examples
```
a!localVariables(
  local!listOfPrimitive: { 1, null, 2, 3, null, 4, null, 5, null },
  fn!spt_list_removenulls(local!listOfPrimitive)
)
```
Returns `{1, 2, 3, 4, 5}`
```
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },
  fn!spt_list_removenulls(local!listOfCdt).id
)
```
Returns the list of `id` properties from non-null elements. E.g. `{1, 2, 3}`
```
a!localVariables(
  local!justNull: {null},
  fn!spt_list_removenulls(local!justNull)
)
```
Returns an empty List of Text String (due to how Appian treats `null` internally)
```
a!localVariables(
  local!notAList: "one",
  fn!spt_list_removenulls(local!notAList)
)
```
Returns `"one"`


### SPT_List_Slice
Returns a subset of the provided list, starting with and including startIndex and ending with and including endIndex. Returns null if list is not a List type.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to slice |
| startIndex | The first index to include in the slice |
| endIndex | The last index to include in the slice. If omitted, the rest of the list is included. |

#### Examples
```
a!localVariables(
  local!hundredElementArray: enumerate(100) + 1,
  fn!spt_list_slice(local!hundredElementArray, 10, 15)
)
```
Returns `{10, 11, 12, 13, 14, 15}`
```
a!localVariables(
  local!remaining: enumerate(10) + 1,
  fn!spt_list_slice(local!remaining, 8)
)
```
Returns `{8, 9, 10}`
```
a!localVariables(
  local!stringValue: "stringValue",
  fn!spt_list_slice(local!stringValue, 1, 5)
)
```
Returns `null`
```
a!localVariables(
  local!nullValue: null,
  fn!spt_list_slice(local!nullValue, 1, 2)
)
```
Returns `null`


### SPT_List_Unique
Returns the unique elements found in the provided list. If the list is null or empty, returns null. If not a List, returns what was passed in. By default, null elements are removed but can be kept by setting keepNulls to true.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to unique |
| keepNulls | If true, keeps null values (uniqued, so 1 at most) |

#### Example
```
a!localVariables(
  local!listOfPrimitive: { 1, 2, 3, 5, 3, 3, 4, 5, 5, 5 },
  fn!spt_list_unique(local!listOfPrimitive)
)
```
Returns `{1, 2, 3, 5, 4}`


## Object Functions

These functions are for working with data structures in Appian, namely Dictionaries, Maps, and Custom Data Types (CDTs).


### SPT_Object_RemoveNullProperties
Removes properties from a Map or Dictionary where the value is null. If the passed in value is not a Map or Dictionary an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to remove nulls from |
| recursive | If true (default), will recurse into nested objects and remove nulls from them as well |

#### Example
```
a!localVariables(
  local!dictionary: {
    id: 1,
    value: "foo",
    unused: null,
    nested: {
      id: 2,
      value: "bar",
      unused: null
    }
  },
  
  fn!spt_object_removenullproperties(local!dictionary)
)
```
Returns:
```
{
  id: 1,
  value: "foo",
  nested: {
    id: 2,
    value: "bar"
  }
}
```


### SPT_Object_ToDictionary
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Dictionary, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Dictionary |

#### Example
```
a!localVariables(
  local!map: a!map(
    id: 1,
    value: "This was a Map",
    nestedMap: a!map(id: 2, value: "This was a nested Map"),
    nestedMapArray: {
      a!map(
        id: 3,
        value: "This was a Map in an array 1"
      ),
      a!map(
        id: 4,
        value: "This was a Map in an array 2"
      )
    }
  ),
  fn!spt_object_todictionary(local!map)
)
```
Returns (Dictionary):
```
{
  id: 1,
  value: "This was a Map",
  nestedMap: { id: 2, value: "This was a nested Map" },
  nestedMapArray: {
    {
      id: 3,
      value: "This was a Map in an array 1"
    },
    {
      id: 4,
      value: "This was a Map in an array 2"
    }
  }
}
```


### SPT_Object_ToMap
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Map, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Map |

This function can be used to store any dynamic data structure into a Process Variable as a Map. When used in conjunction 
with `a!fromJson()` it can store the result of a REST service call as a PV without any additional data massaging (see example 2).

#### Examples
```
a!localVariables(
  local!dict: {
    id: 1,
    value: "This was a Dictionary",
    nested: {
      id: 2,
      value: "This was a nested Dictionary"
    },
    nestedArray: {
      {
        id: 3,
        value: "This was a Dictionary in an array 1"
      },
      {
        id: 4,
        value: "This was a Dictionary in an array 2"
      }
    }
  },
  fn!spt_object_tomap(local!dict)
)
```
Returns:
```
a!map(
  id: 1,
  value: "This was a Dictionary",
  nested: a!map(
    id: 2,
    value: "This was a nested Dictionary"
  ),
  nestedArray: {
    a!map(
      id: 3,
      value: "This was a Dictionary in an array 1"
    ),
    a!map(
      id: 4,
      value: "This was a Dictionary in an array 2"
    )
  }
)
```
Deserializing JSON to a Map:
```
a!localVariables(
  local!json: "{""id"":123,""value"":""This was JSON, now it's a Map"",""nestedObject"":{""message"":""A nested object, also now a Map""}}",
  fn!spt_object_tomap(a!fromJson(local!json))
)
```
Returns:
```
a!map(
  id: 123,
  value: "This was JSON, now it's a Map",
  nestedObject: a!map(
    message: "A nested object, also now a Map"
  )
)
```


## UUID Generation Functions

These functions are for generating UUIDs in Appian.


### SPT_Uuid_Bulk
Creates a list of UUIDs in bulk. Best practice is to know the number of UUIDs to be generated and call this method once in any given Expression evaluation, in order to avoid Appian's caching. (I.e. do not call in a forEach using count of 1 or you will get the same UUID for each call.)

| Parameter | Description |
| ----------- | ----------- |
| count | The number of UUIDs to generate |

#### Examples
```
fn!spt_uuid_bulk(3)
```
Returns:
```
{
  "36cda050-9514-4c8e-a2e5-4bc6ce475d9d",
  "161e14e7-993c-40be-a986-30c1c287d274",
  "aea18a72-f196-440c-8089-32ff525f4e97"
}
```
(Note: your UUIDs will be unique.)

If you need to loop over many objects and add/update UUIDs:
```
a!localVariables(
  local!list: {
    {id: 1, name: "One"},
    {id: 2, name: "Two"},
    {id: 3, name: "Three"},
    {id: 4, name: "Four"},
    {id: 5, name: "Five"},
  },
  
  local!uuids: fn!spt_uuid_bulk(count(local!list)),
  
  local!updated: a!forEach(
    items: local!list,
    expression: a!update(
      fv!item,
      "uuid",
      local!uuids[fv!index]
    )
  ),
  
  local!updated
)
```
Returns:
```
{
  {
    id: 1,
    name: "One",
    uuid: "54b643e5-c4ff-42f2-8155-946174b845a5"
  },
  {
    id: 2,
    name: "Two",
    uuid: "86efc76e-6ec4-4478-a30c-69b9b40f3134"
  },
  {
    id: 3,
    name: "Three",
    uuid: "43426143-812f-4ac7-a55a-998c5124d96f"
  },
  {
    id: 4,
    name: "Four",
    uuid: "6f678028-49f3-49ce-84dc-2cc336638c08"
  },
  {
    id: 5,
    name: "Five",
    uuid: "e3323b18-2370-4b99-966f-d9928665c002"
  }
}
```
(Note: your UUIDs will be unique.)


### SPT_Uuid_FromString
Creates a UUID using the given string as a seed. The UUID will always be the same for any given string input value.

| Parameter | Description |
| ----------- | ----------- |
| string | The string value to create the UUID from |

#### Examples
```
fn!spt_uuid_fromstring(
  "This will always produce the same UUID unless this text is changed"
)
```
Returns: `"af587b80-7ce1-3f19-ba1c-08c8ae551bd0"`

Using existing UUIDs to generate new UUID based on some additional text
```
fn!spt_uuid_fromstring(
  concat(
    "af587b80-7ce1-3f19-ba1c-08c8ae551bd0",
    "|",
    "Rob Munroe"
  )
)
```
Returns: `"d2a16e0d-594c-3d14-8b81-f39b68219a89"`


### SPT_Uuid_FromStrings
Creates a list of UUIDs using the given strings as a seed. The UUIDs will always be the same for any given string input value.

| Parameter | Description |
| ----------- | ----------- |
| strings | The list of string values to create the UUIDs from |

#### Examples
```
fn!spt_uuid_fromstrings({ "One", "Two", "Three" })
```
Returns
```
{
  "6855d98a-8356-3d8f-a0dd-231f6eca10ce",
  "b7b76aa4-fa73-31c9-8d2b-9fa56117bc5d",
  "fb5b4b07-2102-3d0f-9dbf-579140656173"
}
```


