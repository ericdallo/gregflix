# Form-amd

_Form-amd library_

form.js is a small library to help form manipulations and validation. This library uses [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) structure.

[![Build Status](https://travis-ci.org/elo7/form-amd.svg?branch=master)](https://travis-ci.org/elo7/form-amd)

It uses html5 form attributes validate specification and works on browsers that does not support html5 validation.
Then, we built it from scratch.

## Installation

Install with [Bower](http://bower.io): `bower install form-amd`

## Dependencies

Form-amd depends on an [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) implementation. We suggest [async-define](https://gist.github.com/sergiolopes/5778124) implementation for dependency lookup.
Form-amd also depends on [doc-amd](https://github.com/elo7/doc-amd).

## Methods

#### submitOnChange
`.submitOnChange(selectorOrDocElement[, callback])`

###### Description:
Submit the parent form when event **change** is triggered.

###### Parameters:
> selectorOrDocElement: doc-amd object or String //A CSS selector. Note that, if it is a class name with dots, the dots must be escaped. E.g.: doc(".my\\\\.class")

> callback: Function() //A function to call before the event is triggered

###### Sample:
``` js
define(['form'], function(form) {
  form.submitOnChange($('#country')); //Submit the parent form when the country is selected
  form.submitOnChange('#country', function(){...}); //Run the callback function and then submit the parent form when the country is selected
});
```

#### submitOnBlur
`.submitOnBlur(selector)`

###### Description:
Submit the parent form when event **blur** is triggered.

###### Parameters:
> selector: String

###### Sample:
``` js
define(['form'], function(form) {
  form.submitOnBlur('#name'); //Submit the parent form when the form element loses focus
});
```

#### focus
`.focus(selector)`

###### Description:
Focus on selected element. If the device is mobile, it calls **scrollIntoView** function.

###### Parameters:
> selector: String 

###### Sample:
``` js
define(['form'], function(form) {
  form.focus('#input'); //Focus on the element #input
});
```

#### validate
`.validate(selectorOrDocElement[, object])`

###### Description:
Validate the form using almost all the html5 attributes validate spec.

###### Parameters:
> selectorOrDocElement: doc-amd object or String //A CSS selector. Note that, if it is a class name with dots, the dots must be escaped. E.g.: doc(".my\\\\.class")

> object: Object //An object with the properties _messages_ ("required", "min", "maxlength", "pattern" or "email"), _success_ (function callback) or _error_ (function callback)

###### Sample:
``` js
define(['form'], function(form) {
  form.validate($('#form')); //Validate the form with default messages
  form.validate('#form', {
    messages: {
      'required': 'Field required.',
      'min': 'Enter a value greater than or equal to {0}.',
      'maxlength': 'Enter a value with max length less than or equal to {0}.',
      'pattern': 'Enter a valid value.',
      'email': 'Enter a valid email address.'
    }, //Validate the form with this messages
    success: function(){
      // success callback
    },
    error: function(){
      // error callback
    }
  });
});
```

###### Default messages:
``` txt
 required: This field is required
 min: Please enter a value greater than or equal to {0}
 maxlength: Please enter a value with max length less than or equal to {0}
 pattern: Please enter a valid value
 email: Please enter a valid email address
```

#### appendMessage
`.append(selector, text)`

###### Description:
Append validation messages

###### Parameters:
> selector: String 

> text: String

###### Sample:
``` js
define(['form'], function(form) {
  form.append('label[for="date"]', 'dd/mm/yyyy'); //This will append <span class="message">dd/mm/yyyy</span>. Note that this element will be removed when the user starts to type another value.
});
```

#### removeValidationErrors
`.removeValidationErrors(selector)`

###### Description:
Removes all validation messages from selected form

###### Parameters:
> selector: String 

###### Sample:
``` js
define(['form'], function(form) {
  form.removeValidationErrors('#form'); //This will remove all validation messages appended
});
```

## License

Form-amd is released under the [BSD](https://github.com/elo7/form-amd/blob/master/LICENSE). Have at it.

* * *

Copyright :copyright: 2016 Elo7# form-amd
