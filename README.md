# Technical-Exercise

## Solution explained:

### Since there is no physical limitations of RAM ==> fastest way would be O(1). 
- We can accomplish that by preprocessing the list of names.
- Create a mapping between every possible prefix and the list of names it is a prefix of.
- Once the server is up, all is needed to return the list from the mapping.

### Since there is no physical limitation for CPU ==> leverage multiple cores to speed-up the preprocessing. 
- We can accomplish that by creating a thread for each name in the list.
- Each thread would fill the map from before (make sure it is thread-safe).
  - Check if there is only first name or both first and last.
  - If only first, start with index 0 to name.length() - 1.
  - Else:
    - Start from 0 to fullName.length() - 1.
    - Loop over only the last name to enable autocomplete if only last name was typed.


#### The UI has autocomplete dropdown list that technically does the process by itself but I implemented it in a way that with each keystroke change it calls the @RestController.
#### Added another option to press the button or "Enter" to illustrate the process implemented in a different way using @Controller and some Exceptions.
