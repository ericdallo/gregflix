# Ajax-amd

_Ajax-amd is an ajax library_

Ajax.js is a library that helps making ajax requests on modern (IE >= 10) browsers. This library uses [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) structure.

[![Build Status](https://travis-ci.org/elo7/ajax-amd.svg?branch=master)](https://travis-ci.org/elo7/ajax-amd)

## Install

Install with [NPM](http://npmjs.com): `npm install elo7-ajax-amd`

## Dependency

Elo7-ajax-amd depends on an [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) implementation. We suggest [async-define](https://github.com/elo7/async-define) implementation for dependency lookup.
You only need to install it with [NPM](http://npmjs.com) and load elo7-ajax-amd file on your page.

## Methods

#### get
`.get(url, data [,callbacks] [,config])`

###### Description:
Executes an ajax request using `get` http method.

###### Sample:
``` js
define(['ajax'], function(ajax) {
	ajax.get('http://domain.com/ajax', {
		'data': 'value'
	}, {
		'success': function(response [,xhr]) {
			// success callback
		},
		'error': function(response [,xhr]) {
			// error callback
		},
		'complete': function([xhr]) {
			// complete callback
		}
	}, {
		retries: 1,     // number of retries
		timeout: 1000,  // timeout in ms
		async: true    // asynchronous
	});
});
```

#### post
`.post(url, data [,callbacks] [,config])`

###### Description:
Executes an ajax request using `post` http method.

###### Sample:
``` js
define(['ajax'], function(ajax) {
	ajax.post('http://domain.com/ajax', {
		'data': 'value'
	}, {
		'success': function(response [,xhr]) {
			// success callback
		},
		'error': function(response [,xhr]) {
			// error callback
		},
		'complete': function([xhr]) {
			// complete callback
		}
	}, {
		retries: 1,     // number of retries
		timeout: 1000,  // timeout in ms
		async: true    // asynchronous
	});
});
```

#### put
`.put(url, data [,callbacks] [,config])`

###### Description:
Executes an ajax request using `put` http method.

#### delete
`.delete(url, data [,callbacks] [,config])`

###### Description:
Executes an ajax request using `delete` http method.

#### serializeObject
`.serializeObject(form)`

###### Description:
Serializes form fields to json. It can be used as data to the ajax function.

###### Sample:
``` js
define(['ajax'], function(ajax) {
	var serializedForm = ajax.serializeObject(document.querySelector('form'));
});
```


## License

Elo7-ajax-amd is released under the [BSD](https://github.com/elo7/ajax-amd/blob/master/LICENSE). Have at it.

* * *

Copyright :copyright: 2017 Elo7
