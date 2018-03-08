# Event-amd

_Event-amd add/remove event library_

Event.js is a tiny library to add and remove events in any browser. This library uses [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) structure.

## Install

Install : `npm install elo7-events-amd`

## Dependency

Event-amd depends on an [amd](http://en.wikipedia.org/wiki/Asynchronous_module_definition) implementation. We suggest [define-async](https://www.npmjs.com/package/define-async) implementation for dependency lookup.

## Parameters

#### element: DocumentElement
Ex.: document.querySelector('#link')


#### event: String
Event that will be added or removed from the _element_

Ex.: 'click'


#### callback: Function
Function that will be called when the _event_ is triggered, only for _addEvent_

Ex.: function(){ ... }


#### eventCategory: String (optional)
You can add multiple '_events_' of the same type (e.g: _click_) and use the _eventCategory_ parameter to remove certain events when needed.

Ex.:

	event.addEvent(element, 'click', callback, 'tracking');

	event.addEvent(element, 'click', callback, 'action');

	event.removeEvent(element, 'click', 'action');

## Example

``` js
define(['event'], function(event) {
	event.addEvent(element, event, callback, eventCategory);
	event.removeEvent(element, event, eventCategory);
});
```

## License

Event-amd is released under the [BSD](https://github.com/elo7/event-amd/blob/master/LICENSE). Have at it.

* * *

Copyright :copyright: 2015 Elo7# event-amd
