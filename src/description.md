## SPT_Docs_FromUuid
Returns the Appian Document that has the given UUID. Returns null if no Document is found for the given UUID.

| Parameter | Description |
| ----------- | ----------- |
| uuid | The Appian Document's UUID |

## SPT_Docs_GetUuid
Returns the UUID for the given Appian Document

| Parameter | Description |
| ----------- | ----------- |
| document | The Appian Document |

## SPT_List_Count
Returns the element count (including null elements) in a list. If the passed in value is not a list, or the list is null or empty, returns 0.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to count |

## SPT_List_First
Returns the first element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

## SPT_List_HasDuplicates
Returns true if all items in the List are unique. If not a list or the list is null or empty, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to check |

## SPT_List_IsList
Returns true if the value passed in is a List type. If the passed in value is null, returns false.

| Parameter | Description |
| ----------- | ----------- |
| list | The value to check |

## SPT_List_Last
Returns the last element of the list. Returns null if list is null or empty. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to choose from |

## SPT_List_RandomElement
Returns a random element in the provided list. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to select from |
| count | The number of elements to include (optional; default is 1) |
| unique | If selecting multiple, ensure that the elements are unique. Will throw an error if count is greater than the number of elements in the array. |

## SPT_List_Randomize
Returns the provided list in a randomized order (shuffled). If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to randomize |

## SPT_List_RemoveNulls
Removes all null elements from the given list. If a List of Text (string) is passed it, removes empty strings ("") as well. If not a List, returns what was passed in.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to remove nulls from |

## SPT_List_Slice
Returns a subset of the provided list, starting with and including startIndex and ending with and including endIndex. Returns null if list is not a List type.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to slice |
| startIndex | The first index to include in the slice |
| endIndex | The last index to include in the slice. If omitted, the rest of the list is included. |

## SPT_List_Unique
Returns the unique elements found in the provided list. If the list is null or empty, returns null. If not a List, returns what was passed in. By default, null elements are removed but can be kept by setting keepNulls to true.

| Parameter | Description |
| ----------- | ----------- |
| list | The list to unique |
| keepNulls | The list to unique |

## SPT_Object_RemoveNullProperties
Removes properties from a Map or Dictionary where the value is null. If the passed in value is not a Map or Dictionary an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to remove nulls from |
| recursive | If true (default), will recurse into nested objects and remove nulls from them as well |

## SPT_Object_ToDictionary
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Dictionary, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Dictionary |

## SPT_Object_ToMap
Converts the given object (Map(s), Dictionary(s) or CDT(s)) to a Map, including nested objects (unlike the cast() function). If the passed in value is not a Map, Dictionary, or CDT (or a List of them) an error is thrown.

| Parameter | Description |
| ----------- | ----------- |
| object | The object to convert to a Map |

## SPT_Uuid_Bulk
Creates a list of UUIDs in bulk. Best practice is to know the number of UUIDs to be generated and call this method once in any given Expression evaluation, in order to avoid Appian's caching. (I.e. do not call in a forEach using count of 1 or you will get the same UUID for each call.)

| Parameter | Description |
| ----------- | ----------- |
| count | The number of UUIDs to generate  |

## SPT_Uuid_FromString
Creates a UUID using the given string as a seed. The UUID will always be the same for any given string value.

| Parameter | Description |
| ----------- | ----------- |
| string | The string value to create the UUID from  |

## SPT_Uuid_FromStrings
Creates a list of UUIDs using the given strings as a seed. The UUIDs will always be the same for any given string value.

| Parameter | Description |
| ----------- | ----------- |
| strings | The list of string values to create the UUIDs from  |

