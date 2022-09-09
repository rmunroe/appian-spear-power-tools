<p align="center">
  <img src="https://raw.githubusercontent.com/rmunroe/appian-spear-power-tools/main/resources/img/spear_logo.png">
</p>

# SPEaR Power Tools

Brought to you by the Strategic Presales Execution and Readiness (SPEaR) Team in Appian Solutions Consulting


# Introduction

This plugin is the results of over a decade of experience working in the Appian Expression language.  While the Expression language is extremely powerful and capable as-is, there are some edge cases that "power user" Appian Designers may run into that require some sophisticated Expression rules to overcome. We have taken some of these sophisticated Expression rules and reimplemented them as a plugin for ease of use and speed, both in execution and time-to-market.


## Issues or Feedback

If you encounter any issues with the plugin, please post in the Appian Community's App Market page for this plugin.  Additionally, please let us know if there is any additional functionality you would like to see in this plugin and we will see about getting it added.


------------------------------------------------------------------------------


# Table of Contents

## Date and Time Functions
* [SPT_DateTime_ToEpoch](#SPT_DateTime_ToEpoch)
* [SPT_DateTime_FromEpoch](#SPT_DateTime_FromEpoch)

## Document Functions
* [SPT_Docs_GetUuid](#SPT_Docs_GetUuid)
* [SPT_Docs_FromUuid](#SPT_Docs_FromUuid)

## Formatting Functions
* [SPT_Fmt_AsWords](#SPT_Fmt_AsWords)
* [SPT_Fmt_BytesDisplaySize](#SPT_Fmt_BytesDisplaySize)
* [SPT_Fmt_TimeAgo](#SPT_Fmt_TimeAgo)

## List (Array) Functions
* [SPT_List_AppendAny](#SPT_List_AppendAny)
* [SPT_List_Count](#SPT_List_Count)
* [SPT_List_First](#SPT_List_First)
* [SPT_List_HasDuplicates](#SPT_List_HasDuplicates)
* [SPT_List_Last](#SPT_List_Last)
* [SPT_List_RandomElement](#SPT_List_RandomElement)
* [SPT_List_Randomize](#SPT_List_Randomize)
* [SPT_List_RemoveNulls](#SPT_List_RemoveNulls)
* [SPT_List_Slice](#SPT_List_Slice)
* [SPT_List_Unique](#SPT_List_Unique)

## Number Functions
* [SPT_Num_RandInRange](#SPT_Num_RandInRange)

## Object Functions
* [SPT_Object_RemoveNullProperties](#SPT_Object_RemoveNullProperties)
* [SPT_Object_ToDictionary](#SPT_Object_ToDictionary)
* [SPT_Object_ToMap](#SPT_Object_ToMap)

## Text Functions
* [SPT_Text_GetMetaphone](#SPT_Text_GetMetaphone)
* [SPT_Text_ToNumber](#SPT_Text_ToNumber)

## Type Functions
* [SPT_Type_IsDecimal](#SPT_Type_IsDecimal)
* [SPT_Type_IsInteger](#SPT_Type_IsInteger)
* [SPT_Type_IsList](#SPT_Type_IsList)
* [SPT_Type_IsListOfObjects](#SPT_Type_IsListOfObjects)
* [SPT_Type_IsNumeric](#SPT_Type_IsNumeric)
* [SPT_Type_IsObject](#SPT_Type_IsObject)

## UUID Generation Functions
* [SPT_Uuid_Bulk](#SPT_Uuid_Bulk)
* [SPT_Uuid_FromText](#SPT_Uuid_FromText)



------------------------------------------------------------------------------



# Date and Time Functions



## SPT_DateTime_ToEpoch

Returns the number of seconds since the standard base time known as [the epoch](https://en.wikipedia.org/wiki/Unix_time), namely January 1, 1970, 00:00:00 GMT.

| Parameter | Description                                 |
| --------- | ------------------------------------------- |
| dateTime  | The Date and Time to get the epoch time for |

#### Example
```REXX
a!localVariables(
  local!time: datetime(2017, 7, 5, 8, 30),

  spt_datetime_toepoch(local!time)
)
```
Returns `1499243400`



## SPT_DateTime_FromEpoch

Converts [the epoch](https://en.wikipedia.org/wiki/Unix_time) value (the number of seconds since January 1, 1970, 00:00:00 GMT) to a Date and Time.

| Parameter | Description                                          |
| --------- | ---------------------------------------------------- |
| epoch     | The epoch time in seconds to get a Date and Time for |

#### Example
```REXX
spt_datetime_fromepoch(1499243400) = datetime(2017, 7, 5, 8, 30)
```
Returns `true`



------------------------------------------------------------------------------



# Document Functions

These functions are related to working with Documents in Appian's internal content management system.

Both [SPT_Docs_GetUuid](#SPT_Docs_GetUuid) and [SPT_Docs_FromUuid](#SPT_Docs_FromUuid) come from the need for creating demonstration Applications that need to be ported to many different Appian instances - something that is rather rare in typical Appian production environments, but may be valuable in your use cases as well.

When moving Documents from one Appian instance to another (via Export or Compare & Deploy), the integer Document ID is *not* consistent. Therefore, saving the integer ID to a database, then porting that data and the Documents to another instance and using the stored ID to retrieve the Document will likely not produce the correct Document, as new IDs are assigned at import time. To avoid this, the Document's UUID can be used as a unique identifier (as is the intention of a UUID) to quickly retrieve the Document. UUIDs of Documents are assigned at the time of upload and do not ever change, regardless of how many different Appian instances they are deployed to (assuming you are not re-uploading the raw document file).

To store Document references in databases and keep these links across Appian instances, consider retrieving a Document's UUID and storing that in a `VARCHAR(50)` field in the database instead of its integer Document ID. Then use that UUID to fetch a Document instance and use it as you would when storing an integer Document ID. This gives the consistency of a Constant when using a database.

As a performance note, [SPT_Docs_GetUuid](#SPT_Docs_GetUuid) takes around 4 ms to execute, while [SPT_Docs_FromUuid](#SPT_Docs_FromUuid) takes around 3 ms.



## SPT_Docs_GetUuid

Returns the UUID for the given Appian Document

| Parameter | Description         |
| --------- | ------------------- |
| document  | The Appian Document |

#### Example
In this example, we are populating a CDT used to store doc info in a database table with some metadata for quick access, and the provided Document's UUID. This CDT would then be saved to the database for later use.
```REXX
a!localVariables(
  local!myCdt: 'type!{urn:com:appian:types:ABC}ABC_Document'(
    docName: document(ri!docToSave, "name"),
    docExt: document(ri!docToSave, "extension"),
    docSize: document(ri!docToSave, "size"),

    docUuid: spt_docs_getuuid(ri!docToSave)
  ),
  
...
```



## SPT_Docs_FromUuid

Returns the Appian Document that has the given UUID. Returns `null` if no Document is found for the given UUID.

| Parameter | Description                |
| --------- | -------------------------- |
| uuid      | The Appian Document's UUID |

#### Example
In this example, we are resolving a Document from its UUID as stored in a database and retrieved as a CDT. For further illustration we are building a display name from the actual, resolved Document.
```REXX
a!localVariables(
  local!myCdt: rule!ABC_getDocumentCdtById(id: ri!docCdtId),

  local!document: spt_docs_fromuuid(local!myCdt.docUuid),

  local!docDisplay: document(local!document, "name") & "." & document(local!document, "extension"),

...
```



------------------------------------------------------------------------------



# Formatting Functions

These functions are for formatting different input values to Text values suitable for displaying.



## SPT_Fmt_AsWords

Returns a words-based description of the provided numeric value, e.g. for when writing checks.

| Parameter     | Description                                         |
| ------------- | --------------------------------------------------- |
| number        | The Integer or Decimal number to convert into words |
| converterName | The name of the converter to use. See below.        |

This function requires you specify which converter to use. Typically, for Integer input values, an `*_INTEGER` converter is used, and for Decimal values a `*_BANKING_MONEY_VALUE` converter is used, however any combination can be used.

### Integer Converter Names
* `BRAZILIAN_PORTUGUESE_INTEGER`
* `BULGARIAN_INTEGER`
* `CZECH_INTEGER`
* `ENGLISH_INTEGER`
* `FRENCH_INTEGER`
* `GERMAN_INTEGER`
* `ITALIAN_INTEGER`
* `KAZAKH_INTEGER`
* `LATVIAN_INTEGER`
* `POLISH_INTEGER`
* `RUSSIAN_INTEGER`
* `SERBIAN_CYRILLIC_INTEGER`
* `SERBIAN_INTEGER`
* `SLOVAK_INTEGER`
* `TURKISH_INTEGER`
* `UKRAINIAN_INTEGER`


### Banking Money Value Converter Names
* `AMERICAN_ENGLISH_BANKING_MONEY_VALUE`
* `BRAZILIAN_PORTUGUESE_BANKING_MONEY_VALUE`
* `BULGARIAN_BANKING_MONEY_VALUE`
* `CZECH_BANKING_MONEY_VALUE`
* `ENGLISH_BANKING_MONEY_VALUE`
* `FRENCH_BANKING_MONEY_VALUE`
* `GERMAN_BANKING_MONEY_VALUE`
* `ITALIAN_BANKING_MONEY_VALUE`
* `KAZAKH_BANKING_MONEY_VALUE`
* `LATVIAN_BANKING_MONEY_VALUE`
* `POLISH_BANKING_MONEY_VALUE`
* `RUSSIAN_BANKING_MONEY_VALUE`
* `SERBIAN_BANKING_MONEY_VALUE`
* `SERBIAN_CYRILLIC_BANKING_MONEY_VALUE`
* `SLOVAK_BANKING_MONEY_VALUE`
* `TURKISH_BANKING_MONEY_VALUE`
* `UKRAINIAN_BANKING_MONEY_VALUE`

#### Example 1 - Integer to English words
```REXX
spt_fmt_aswords(123456789, "ENGLISH_INTEGER")
```
Returns: `"one hundred twenty-three million four hundred fifty-six thousand seven hundred eighty-nine"`

#### Example 2 - Decimal to US English money words (dollars)
```REXX
spt_fmt_aswords(123456.78, "AMERICAN_ENGLISH_BANKING_MONEY_VALUE")
```
Returns: `"one hundred twenty-three thousand four hundred fifty-six $ 78/100"`

#### Example 3 - Decimal to French money words (euros)
```REXX
spt_fmt_aswords(123456.78, "FRENCH_BANKING_MONEY_VALUE")
```
Returns: `"cent vingt-trois mille quatre cent cinquante-six € 78/100"`



## SPT_Fmt_BytesDisplaySize

Returns a friendly display size for the given number of bytes. Useful when used with `document(123, "size")`.

| Parameter | Description                                                                            |
| --------- | -------------------------------------------------------------------------------------- |
| bytes     | The size in bytes to get a display value for (e.g. `"123 KB"`)                         |
| binary    | If set to true, uses base `1024` instead of `1000` and returns values e.g. `"123 KiB"` |

#### Example 1 - Show the "standard" size for a byte value
```REXX
spt_fmt_bytesdisplaysize(203681)
```
Returns `"203.7 kB"`

#### Example 2 - Show the binary-based size for a byte value, retrieved from a Document
```REXX
a!localVariables(
  local!docSize: document(cons!SPTT_TEST_IMAGE_FILE, "size"), /* 203681 bytes */
  
  spt_fmt_bytesdisplaysize(local!docSize, true)
)
```
Returns `"198.9 KiB"`



## SPT_Fmt_TimeAgo

Returns a text description of the relative duration a given Date and Time was or is from `now()`.

| Parameter | Description                                                                                                                                                          |
| --------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| dateTime  | The Date and Time to describe                                                                                                                                        |
| locale    | Optional locale abbreviation supported by [the PrettyTime library](https://github.com/ocpsoft/prettytime/tree/master/core/src/main/java/org/ocpsoft/prettytime/i18n) |

#### Example 1 - 18 minutes, 30 seconds ago
```REXX
a!localVariables(
  local!time: now() - intervalds(0, 18, 30),

  spt_fmt_timeago(local!time)
)
```
Returns `"19 minutes ago"`

#### Example 2 - 100 days ago
```REXX
a!localVariables(
  local!time: now() - 100,

  spt_fmt_timeago(local!time)
)
```
Returns `"3 months ago"`

#### Example 3 - 100 days ago, but in German
```REXX
a!localVariables(
  local!time: now() - 100,

  spt_fmt_timeago(local!time, "de")
)
```
Returns `"vor 3 Monaten"`



------------------------------------------------------------------------------



# List (Array) Functions

These functions are for working with Lists in Appian, aka arrays. Several of them will have similar names to existing List functions, but will have specific caveats that help make Lists easier.



## SPT_List_AppendAny

Appends any value to a List without changing the value's type. If the type of the list and the value being appended do not match, the List is cast to a List of Variant.

| Parameter | Description           |
| --------- | --------------------- |
| list      | The list to append to |
| value     | The value to append   |


### NOTE: Nulls and Empty Strings

Appian converts the `null` value to an empty string `""` when providing the value to a function plugin. It is therefore impossible to tell which value (`null` vs `""`) the user actually meant.

_**When an `Any Type` value is provided, this plugin will default to both `null` and `""` being intended as `null`.**_

For instance if you use [SPT_List_AppendAny](#SPT_List_AppendAny) to append `""` onto a List, instead of an empty string `""` being appended, the value `null` is used. See the example given below.

#### Example 1 - Append an integer on a List of Text
If using the built-in `append()`, Appian would convert `4` to `"4"`.
```REXX
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  
  spt_list_appendany(local!listOfTextString, 4)
)
```
Returns (List of Variant) `{"one", "two", "three", 4}`

#### Example 2 - Empty string is converted to `null`
```REXX
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },
  
  spt_list_appendany(local!listOfTextString, "")
)
```
Returns (List of Variant) `{"one", "two", "three", null}`



## SPT_List_Count

Returns the element count (including `null` elements) in a list. If the passed in value is not a List, or the List is `null` or empty, returns `0`.

| Parameter | Description       |
| --------- | ----------------- |
| list      | The list to count |

#### Example 1 - Counting `null`
(If using the built-in `count()`, Appian would instead return `1`, which can be very confusing.)
```REXX
a!localVariables(
  local!nullValue: null,

  spt_list_count(local!nullValue)
)
```
Returns `0`

#### Example 2 - Counting a non-list value
(If using the built-in `count()`, Appian would instead return `1`, which can be very confusing.)
```REXX
a!localVariables(
  local!stringValue: "stringValue",

  spt_list_count(local!stringValue)
)
```
Returns `0`

#### Example 3 - Counting a List
```REXX
a!localVariables(
  local!hundredElementArray: enumerate(100),

  spt_list_count(local!hundredElementArray)
)
```
Returns `100`



## SPT_List_First

Returns the first element of the List. Returns `null` if List is `null` or empty. If not a List, returns what was passed in.

| Parameter | Description             |
| --------- | ----------------------- |
| list      | The list to choose from |

#### Example 1 - Getting the first element from a List
```REXX
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },

  spt_list_first(local!listOfTextString)
)
```
Returns `"one"`

#### Example 2 - Getting the first element from a non-List
```REXX
a!localVariables(
  local!notAnArray: "notAnArray",

  spt_list_first(local!notAnArray)
)
```
Returns `"notAnArray"`



## SPT_List_HasDuplicates

Returns `true` if there are duplicate items in the List. If all items are unique, or the value passed in is not a List or the List is `null` or empty, returns `false`.

| Parameter | Description       |
| --------- | ----------------- |
| list      | The list to check |

#### Example 1 - List of Integers with duplicate values
```REXX
a!localVariables(
  local!listOfPrimitive: { 1, 2, 3, 5, 3, 3, 4, 5, 5, 5 },

  spt_list_hasduplicates(local!listOfPrimitive)
)
```
Returns `true`

#### Example 2 - List of CDTs with duplicate values
```REXX
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },

  spt_list_hasduplicates(local!listOfCdt)
)
```
Returns `true`

#### Example 3 - List of Integers with no duplicates
```REXX
spt_list_hasduplicates(enumerate(10))
```
Returns `false`

#### Example 4 - Passing in a non-List
```REXX
spt_list_hasduplicates(123)
```
Returns `false`

##### Example 5 - Passing in `null`
```REXX
spt_list_hasduplicates(null)
```
Returns `false`



## SPT_List_Last

Returns the last element of the List. Returns `null` if List is `null` or empty. If not a List, returns what was passed in.

| Parameter | Description             |
| --------- | ----------------------- |
| list      | The list to choose from |

#### Example 1 - Grab the last element of a List of Text
```REXX
a!localVariables(
  local!listOfTextString: { "one", "two", "three" },

  spt_list_last(local!listOfTextString)
)
```
Returns `"three"`

#### Example 2 - Grab the last element of non-List
```REXX
a!localVariables(
  local!notAnArray: "notAnArray",

  spt_list_last(local!notAnArray)
)
```
Returns `"notAnArray"`



## SPT_List_RandomElement

Returns a random element in the provided List. If not a List, returns what was passed in.

| Parameter | Description                                                                                                                                   |
| --------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| list      | The list to select from                                                                                                                       |
| count     | The number of elements to include (optional; default is 1)                                                                                    |
| unique    | If selecting multiple, ensure that the elements are unique. Will throw an error if count is greater than the number of elements in the array. |

#### Example - Grab several random elements
```REXX
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
    one: spt_list_randomelement(local!listOfMap),
    unique3: spt_list_randomelement(local!listOfMap, 3, true),
    ten: spt_list_randomelement(local!listOfMap, 10)
  )
)
```
Returns a Map with:
* a single random element
* 3 unique, random elements
* a List of 10 random elements, some being duplicates



## SPT_List_Randomize

Returns the provided List in a randomized order (shuffled). If not a List, returns what was passed in.

| Parameter | Description           |
| --------- | --------------------- |
| list      | The list to randomize |

#### Example - Retrieve Map `id` properties in a randomized order 
```REXX
a!localVariables(
  local!listOfMap: {
    a!map(id: 1, value: a!map(subValue: "first map")),
    a!map(id: 2, value: "second map"),
    a!map(id: 3, value: "third map"),
    a!map(id: 4, value: a!map(subValue: "fourth map")),
    a!map(id: 5, value: "fifth map"),
    a!map(id: 6, value: "sixth map"),
  },
  
  spt_list_randomize(local!listOfMap).id
)
```
Returns the List of `id` properties, in random order. E.g. `{2, 3, 6, 4, 1, 5}`


## SPT_List_RemoveNulls

Removes all `null` elements from the given List. If a List of Text is passed in, removes empty strings (`""`) as well. If not a List, returns what was passed in.

| Parameter | Description                   |
| --------- | ----------------------------- |
| list      | The list to remove nulls from |

#### Example 1 - Remove nulls from a List of Integers
```REXX
a!localVariables(
  local!listOfPrimitive: { 1, null, 2, 3, null, 4, null, 5, null },

  spt_list_removenulls(local!listOfPrimitive)
)
```
Returns `{1, 2, 3, 4, 5}`

#### Example 2 - Remove nulls from a List of CDTs and retrieve the `id` property
```REXX
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    null,
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },

  spt_list_removenulls(local!listOfCdt).id
)
```
Returns `{1, 2, 3}`

#### Example 3 - Remove nulls from a List of only `null`
```REXX
a!localVariables(
  local!justNull: {null},

  spt_list_removenulls(local!justNull)
)
```
Returns an empty List of Text String (due to how Appian treats `null` internally)

#### Example 4 - Remove nulls from a non-List
```REXX
a!localVariables(
  local!notAList: "one",

  spt_list_removenulls(local!notAList)
)
```
Returns `"one"`



## SPT_List_Slice

Returns a subset of the provided List, starting with and including `startIndex` and ending with and including `endIndex`. Returns `null` if `list` is not a List type.

| Parameter  | Description                                                                           |
| ---------- | ------------------------------------------------------------------------------------- |
| list       | The list to slice                                                                     |
| startIndex | The first index to include in the slice                                               |
| endIndex   | The last index to include in the slice. If omitted, the rest of the list is included. |

#### Example 1 - Get elements 10-15 (inclusive)
```REXX
a!localVariables(
  local!hundredElementArray: enumerate(100) + 1,

  spt_list_slice(local!hundredElementArray, 10, 15)
)
```
Returns `{10, 11, 12, 13, 14, 15}`

#### Example 2 - Get element 8 and on 
```REXX
a!localVariables(
  local!remaining: enumerate(10) + 1,

  spt_list_slice(local!remaining, 8)
)
```
Returns `{8, 9, 10}`

#### Example 3 - Slice a non-List
```REXX
a!localVariables(
  local!stringValue: "stringValue",

  spt_list_slice(local!stringValue, 1, 5)
)
```
Returns `null`

#### Example 4 - Slice a `null` value
```REXX
a!localVariables(
  local!nullValue: null,

  spt_list_slice(local!nullValue, 1, 2)
)
```
Returns `null`



## SPT_List_Unique

Returns the unique elements found in the provided List. If `list` is null or empty, returns `null`. If not a List, returns what was passed in. By default, `null` elements are removed but can be kept by setting `keepNulls` to `true`.

| Parameter | Description                                        |
| --------- | -------------------------------------------------- |
| list      | The list to unique                                 |
| keepNulls | If true, keeps null values (uniqued, so 1 at most) |

#### Example - Get only unique values
```REXX
a!localVariables(
  local!listOfPrimitive: { 1, 2, 3, 5, 3, 3, 4, 5, 5, 5 },

  spt_list_unique(local!listOfPrimitive)
)
```
Returns `{1, 2, 3, 5, 4}`



------------------------------------------------------------------------------



# Number Functions

These functions are for working with Integers or Decimals.



## SPT_Num_RandInRange

Returns random value(s) in the given range. The value type will be either Integer or Decimal, determined by the types used for `min` and `max`.

| Parameter | Description                                                                           |
| --------- | ------------------------------------------------------------------------------------- |
| min       | The minimum number to be used in the range (Integer or Decimal)                       |
| max       | The maximum number to be used in the range (Integer or Decimal)                       |
| count     | The number of random values to produce (defaults to a single; if set, returns a List) |
| places    | If returning Decimals, optionally set the number of places to include                 |

#### Example 1 - Generate a single Integer
```REXX
spt_num_randinrange(10, 20)
```
Returns (e.g.) `14`

#### Example 2 - Generate 5 Decimals, with 2 decimal places
```REXX
spt_num_randinrange(10.0, 20.0, 5, 2)
```
Returns (e.g.) `{11.33, 12.22, 12.78, 16.53, 16.82}`



------------------------------------------------------------------------------



# Object Functions

These functions are for working with data structures in Appian, namely Dictionaries, Maps, and Custom Data Types (CDTs).



## SPT_Object_RemoveNullProperties

Removes properties from a Map or Dictionary where the value is `null`. If the passed in value is not a Map or Dictionary an error is thrown.

| Parameter | Description                                                                            |
| --------- | -------------------------------------------------------------------------------------- |
| object    | The object to remove nulls from                                                        |
| recursive | If true (default), will recurse into nested objects and remove nulls from them as well |

#### Example - Remove nulls from a Dictionary
```REXX
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
  
  spt_object_removenullproperties(local!dictionary)
)
```
Returns:
```REXX
{
  id: 1,
  value: "foo",
  nested: {
    id: 2,
    value: "bar"
  }
}
```



## SPT_Object_ToDictionary

Converts the given object (Map, Dictionary, or CDT) to a Dictionary, including nested objects (unlike the `cast()` function). Lists of objects are supported as well. If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description                           |
| --------- | ------------------------------------- |
| object    | The object to convert to a Dictionary |

#### Example - Convert a Map (with nested Maps) to a Dictionary
```REXX
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

  spt_object_todictionary(local!map)
)
```
Returns (Dictionary):
```REXX
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



## SPT_Object_ToMap

Converts the given object (Map, Dictionary, or CDT) to a Map, including nested objects (unlike the `cast()` function). Lists of objects are supported as well. If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description                    |
| --------- | ------------------------------ |
| object    | The object to convert to a Map |

This function can be used to store any dynamic data structure into a Process Variable as a Map. When used in conjunction with `a!fromJson()` it can store the result of a REST service call as a Process Variable without any additional data massaging (see [Example 2](#example-2---deserializing-json-to-a-map)).

#### Example 1 - Convert a Dictionary (with nested Dictionaries) to a Map
```REXX
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

  spt_object_tomap(local!dict)
)
```
Returns:
```REXX
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

#### Example 2 - Deserializing JSON to a Map
```REXX
a!localVariables(
  local!json: "{""id"":123,""value"":""This was JSON, now it's a Map"",""nestedObject"":{""message"":""A nested object, also now a Map""}}",

  spt_object_tomap(a!fromJson(local!json))
)
```
Returns:
```REXX
a!map(
  id: 123,
  value: "This was JSON, now it's a Map",
  nestedObject: a!map(
    message: "A nested object, also now a Map"
  )
)
```



------------------------------------------------------------------------------



# Text Functions

These functions are for working with Text input values.



## SPT_Text_GetMetaphone

Returns the Metaphone value for the given Text value. Alternatively uses Double Metaphone if doubleMetaphone is `true`.

| Parameter       | Description                                               |
| --------------- | --------------------------------------------------------- |
| text            | The text to generate a Metaphone value for                |
| doubleMetaphone | Use Double Metaphone instead (optional; default is false) |

Metaphone is a phonetic algorithm used for matching similar sounding English words and names. This can be used to help create a "fuzzy search" in Appian. 

Double Metaphone is an improvement upon Metaphone and supports more languages than only English.

See [the Wikipedia Metaphone page](https://en.wikipedia.org/wiki/Metaphone) for more info.

#### Example 1 - Get Metaphone for a last name
```REXX
spt_text_getmetaphone("Gomez-Hernandez")
```
Returns `"KMSH"`

#### Example 2 - Get Double Metaphone for a last name
```REXX
spt_text_getmetaphone("Gomez-Hernandez", true)
```
Returns `"KMSR"`



## SPT_Text_ToNumber

Parses the given Text value to an Integer or Decimal. If the value cannot be parsed, or it has additional non-numeric text, returns `null`.

| Parameter | Description                 |
| --------- | --------------------------- |
| text      | The text value to be parsed |

#### Example 1 - Text is non-numeric
```REXX
spt_text_tonumber("Bad text")
```
Returns `null`

#### Example 2 - Text is of a valid Integer
```REXX
spt_text_tonumber("123")
```
Returns `123`

#### Example 3 - Text is of a valid Decimal
```REXX
spt_text_tonumber("4.56")
```
Returns `4.56`



------------------------------------------------------------------------------



# Type Functions

These functions provide information about the (data) type of the values passed in.



## SPT_Type_IsDecimal

Returns `true` if the passed in value is Text that can be interpolated as a Decimal, or is a Decimal, or a List of Decimals.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - Text is non-numeric
```REXX
spt_type_isdecimal("Bad text")
```
Returns `false`

#### Example 2 - Text is of a valid Integer
```REXX
spt_type_isdecimal("123")
```
Returns `false`

#### Example 3 - Text is of a valid Decimal
```REXX
spt_type_isdecimal("4.56")
```
Returns `true`

#### Example 4 - Value is an Integer
```REXX
spt_type_isdecimal(123)
```
Returns `false`

#### Example 5 - Value is a Decimal
```REXX
spt_type_isdecimal(4.56)
```
Returns `true`

#### Example 6 - Value is a List of Integer
```REXX
spt_type_isdecimal({1, 2, 3})
```
Returns `false`

#### Example 7 - Value is a List of Decimal
```REXX
spt_type_isdecimal({1.1, 2.2, 3.3})
```
Returns `true`

#### Example 8 - Value is a List of Text
```REXX
spt_type_isdecimal({"one", "two", "three"})
```
Returns `false`



## SPT_Type_IsInteger

Returns `true` if the passed in value is Text that can be interpolated as an Integer, or is an Integer, or a List of Integers.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - Text is non-numeric
```REXX
spt_type_isinteger("Bad text")
```
Returns `false`

#### Example 2 - Text is of a valid Integer
```REXX
spt_type_isinteger("123")
```
Returns `true`

#### Example 3 - Text is of a valid Decimal
```REXX
spt_type_isinteger("4.56")
```
Returns `false`

#### Example 4 - Value is an Integer
```REXX
spt_type_isinteger(123)
```
Returns `true`

#### Example 5 - Value is a Decimal
```REXX
spt_type_isinteger(4.56)
```
Returns `false`

#### Example 6 - Value is a List of Integer
```REXX
spt_type_isinteger({1, 2, 3})
```
Returns `true`

#### Example 7 - Value is a List of Decimal
```REXX
spt_type_isinteger({1.1, 2.2, 3.3})
```
Returns `false`

#### Example 8 - Value is a List of Text
```REXX
spt_type_isinteger({"one", "two", "three"})
```
Returns `false`



## SPT_Type_IsList

Returns `true` if the value passed in is a List type. If the passed in value is `null`, returns `false`.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - Passing in a List of CDT
```REXX
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },

  spt_type_islist(local!listOfCdt)
)
```
Returns `true`

#### Example 2 - Passing in a List of Integers
```REXX
a!localVariables(
  local!hundredElementList: enumerate(100),

  spt_type_islist(local!hundredElementList)
)
```
Returns `true`

#### Example 3 - Passing in an empty List
```REXX
a!localVariables(
  local!emptyList: {},

  spt_type_islist(local!emptyList)
)
```
Returns `true`

#### Example 4 - Passing in a non-List
```REXX
a!localVariables(
  local!stringValue: "stringValue",

  spt_type_islist(local!stringValue)
)
```
Returns `false`

#### Example 5 - Passing in `null`
```REXX
a!localVariables(
  local!nullValue: null,

  spt_type_islist(local!nullValue)
)
```
Returns `false`



## SPT_Type_IsListOfObjects

Returns `true` if the value passed in is a List of Dictionaries, Maps, or CDTs. If the passed in value is `null`, returns `false`.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - Passing in a List of CDT
```REXX
a!localVariables(
  local!listOfCdt: {
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "first cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 2, value: "second cdt"),
    'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 3, value: "third cdt"),
  },

  spt_type_islistofobjects(local!listOfCdt)
)
```
Returns `true`

#### Example 2 - Passing in a List of Integers
```REXX
a!localVariables(
  local!hundredElementList: enumerate(100),

  spt_type_islistofobjects(local!hundredElementList)
)
```
Returns `false`

#### Example 3 - Passing in an empty List
```REXX
a!localVariables(
  local!emptyList: {},

  spt_type_islistofobjects(local!emptyList)
)
```
Returns `false`

#### Example 4 - Passing in a non-List
```REXX
a!localVariables(
  local!stringValue: "stringValue",

  spt_type_islistofobjects(local!stringValue)
)
```
Returns `false`

#### Example 5 - Passing in `null`
```REXX
a!localVariables(
  local!nullValue: null,

  spt_type_islistofobjects(local!nullValue)
)
```
Returns `false`



## SPT_Type_IsNumeric

Returns `true` if the passed in value is Text that can be interpolated as a number (Integer or Decimal), or is an Integer, a Decimal, a List of Integers, or a List of Decimals.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - Text is non-numeric
```REXX
spt_type_isnumeric("Bad text")
```
Returns `false`

#### Example 2 - Text is of a valid Integer
```REXX
spt_type_isnumeric("123")
```
Returns `true`

#### Example 3 - Text is of a valid Decimal
```REXX
spt_type_isnumeric("4.56")
```
Returns `true`

#### Example 4 - Value is an Integer
```REXX
spt_type_isnumeric(123)
```
Returns `true`

#### Example 5 - Value is a Decimal
```REXX
spt_type_isnumeric(4.56)
```
Returns `true`

#### Example 6 - Value is a List of Integer
```REXX
spt_type_isnumeric({1, 2, 3})
```
Returns `true`

#### Example 7 - Value is a List of Decimal
```REXX
spt_type_isnumeric({1.1, 2.2, 3.3})
```
Returns `true`

#### Example 8 - Value is a List of Text
```REXX
spt_type_isnumeric({"one", "two", "three"})
```
Returns `false`



## SPT_Type_IsObject


Returns `true` if the value passed in is a Dictionary, Map, or CDT.

| Parameter | Description        |
| --------- | ------------------ |
| value     | The value to check |

#### Example 1 - `null` is passed in
```REXX
spt_type_isobject(null)
```
Returns `false`

#### Example 2 - A Text value is passed in
```REXX
spt_type_isobject("ABC")
```
Returns `false`

#### Example 3 - Empty list is passed in
```REXX
spt_type_isobject({})
```
Returns `false`

#### Example 4 - A CDT is passed in
```REXX
a!localVariables(
  local!cdt: 'type!{urn:com:appian:types:ABC}ABC_TestCdt'(id: 1, value: "a cdt"),

  spt_type_isobject(local!cdt)
)
```
Returns `true`

#### Example 5 - A Dictionary is passed in
```REXX
a!localVariables(
  local!dict: { id: 1, value: { subValue: "a dict" } },

  spt_type_isobject(local!dict)
)
```
Returns `true`

#### Example 6 - A Map is passed in
```REXX
a!localVariables(
  local!map: a!map(id: 1, value: a!map(subValue: "a map")),
  
  spt_type_isobject(local!map)
)
```
Returns `true`



------------------------------------------------------------------------------



# UUID Generation Functions

These functions are for generating UUIDs in Appian.



## SPT_Uuid_Bulk

Creates a list of UUIDs in bulk. Best practice is to know the number of UUIDs to be generated and call this method once in any given Expression evaluation, in order to avoid Appian's caching. (I.e. do not call in a `a!forEach()` using count of `1` or you will get the same UUID for each call as the result is cached.)

| Parameter | Description                     |
| --------- | ------------------------------- |
| count     | The number of UUIDs to generate |

#### Example 1 - Generate 3 UUIDs at once
```REXX
spt_uuid_bulk(3)
```
Returns:
```REXX
{
  "36cda050-9514-4c8e-a2e5-4bc6ce475d9d",
  "161e14e7-993c-40be-a986-30c1c287d274",
  "aea18a72-f196-440c-8089-32ff525f4e97"
}
```
(**Note**: your UUIDs will be unique.)

#### Example 2 - Update a List of Dictionary with newly generated UUIDs
If you need to loop over many objects and add/update UUIDs:
```REXX
a!localVariables(
  local!list: {
    {id: 1, name: "One"},
    {id: 2, name: "Two"},
    {id: 3, name: "Three"},
    {id: 4, name: "Four"},
    {id: 5, name: "Five"},
  },
  
  local!uuids: spt_uuid_bulk(count(local!list)),
  
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
```REXX
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
(**Note**: your UUIDs will be unique.)



## SPT_Uuid_FromText

Creates a UUID using the given Text value as a seed. The UUID will always be the same for any given Text value.

| Parameter | Description                            |
| --------- | -------------------------------------- |
| text      | The Text value to create the UUID from |

#### Example 1 - Retrieve the unchanging UUID for a Text value
```REXX
spt_uuid_fromtext(
  "This will always produce the same UUID unless this text is changed"
)
```
Returns: `"af587b80-7ce1-3f19-ba1c-08c8ae551bd0"`

#### Example 2 - Use an existing UUID to generate new UUID based on some additional text
```REXX
spt_uuid_fromtext(
  concat(
    "af587b80-7ce1-3f19-ba1c-08c8ae551bd0",
    "|",
    "Rob Munroe"
  )
)
```
Returns: `"d2a16e0d-594c-3d14-8b81-f39b68219a89"`
