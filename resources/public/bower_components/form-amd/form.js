/**
 * Form.
 *
 * Lib to help form manipulations
 *
 */

define('form', ['doc'], function($) {
	'use strict';
	var isIE8 = (navigator.userAgent.toString().toLowerCase().indexOf('trident/4.0') != -1);
	var isMobile = (function(){
		var nav = navigator.userAgent||navigator.vendor||window.opera;
		if (/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(nav) || /1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw-(n|u)|c55\/|capi|ccwa|cdm-|cell|chtm|cldc|cmd-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc-s|devi|dica|dmob|do(c|p)o|ds(12|-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(-|_)|g1 u|g560|gene|gf-5|g-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd-(m|p|t)|hei-|hi(pt|ta)|hp( i|ip)|hs-c|ht(c(-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i-(20|go|ma)|i230|iac( |-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|-[a-w])|libw|lynx|m1-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|-([1-8]|c))|phil|pire|pl(ay|uc)|pn-2|po(ck|rt|se)|prox|psio|pt-g|qa-a|qc(07|12|21|32|60|-[2-7]|i-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h-|oo|p-)|sdk\/|se(c(-|0|1)|47|mc|nd|ri)|sgh-|shar|sie(-|m)|sk-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h-|v-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl-|tdg-|tel(i|m)|tim-|t-mo|to(pl|sh)|ts(70|m-|m3|m5)|tx-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas-|your|zeto|zte-/i.test(nav.substr(0,4))){
			return true;
		}
		return false;
	})();

	if (!String.prototype.trim) {
		String.prototype.trim = function () {
			return this.replace(/^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, '');
		};
	}

	var appendMessage = function(label, message) {
		if (label.find('.validation-message').isEmpty()) {
			label.addClass('validation').addClass('error');
			var messageTag = document.createElement('span');
			$(messageTag).addClass('validation-message');

			$(messageTag).text(message);
			label.first().appendChild(messageTag);
		}
	};

	var appendMessageWithArgs = function(label, formattedMessage) {
		for (var i = 2; i < arguments.length; i++) {
			var index = (i - 2);
			formattedMessage = formattedMessage.replace('{' + index + '}', arguments[i]);
		}

		appendMessage(label, formattedMessage);
	};

	var removeValidationErrors = function(form) {
		form.removeClass('has-errors');
		form.find('.validation-message').each(function(el) {
			$(el).parent().removeClass('error');
			$(el).removeItem();
		});
	};

	var validationMessages = {
		'required': 'This field is required',
		'min': 'Please enter a value greater than or equal to {0}',
		'maxlength': 'Please enter a value with max length less than or equal to {0}',
		'pattern': 'Please enter a valid value',
		'email': 'Please enter a valid email address',
		'url': 'Please enter a valid url'
	};

	var isEmpty = function(value) {
		if (value === null || value === undefined || value.trim() === '') {
			return true;
		}
		return false;
	};

	var isValid = function(form) {
		var valid = true;

		removeValidationErrors(form);

		form.find('[required]').each(function(element){
			if (isEmpty($(element).val())) {
				var parent = element.parentElement;
				appendMessage($(parent), validationMessages.required);
				valid = false;
			}
		});

		form.find('[min]').each(function(element){
			var length = parseInt(element.getAttribute('min')),
				value = $(element).val().trim();

			if (value && value.length < length) {
				var parent = element.parentElement;
				appendMessageWithArgs($(parent), validationMessages.min, length);
				valid = false;
			}
		});

		form.find('[maxlength]').each(function(element){
			var length = parseInt(element.getAttribute('maxlength'));

			if ($(element).val() && $(element).val().length > length) {
				var parent = element.parentElement;
				appendMessageWithArgs($(parent), validationMessages.maxlength, length);
				valid = false;
			}
		});

		form.find('[pattern]').each(function(element){
			var pattern = new RegExp('^' + element.getAttribute('pattern') + '$');

			if ($(element).val() && !pattern.test($(element).val())) {
				var parent = element.parentElement;
				appendMessage($(parent), validationMessages.pattern);
				valid = false;
			}
		});

		form.find('[type="email"]').each(function(element){
			var pattern = new RegExp(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);

			if (($(element).val()) && !pattern.test($(element).val())) {
				var parent = element.parentElement;
				appendMessage($(parent), validationMessages.email);
				valid = false;
			}
		});

		form.find('[type="url"]').each(function(element){
			if ($(element).val() && !element.validity.valid) {
				var parent = element.parentElement;
				appendMessage($(parent), validationMessages.url);
				valid = false;
			}
		});

		if (!valid){
			form.addClass('has-errors');

			var visibleErrorFields = form.find('.error input, .error textarea').filter(function(field) {
				return $(field).attr('type') !== 'hidden';
			});

			addEventToClearErrorMessages(visibleErrorFields);
			this.focus(visibleErrorFields.first());
		}

		return valid;
	};

	var addEventToClearErrorMessages = function(visibleErrorFields) {
		visibleErrorFields.each(function(element){
			$(element).on('input', function(event){
				var target = event.target ? $(event.target) : $(window.event.srcElement),
					parent = target.parent();
				target.off('input', 'validationOff');
				parent.removeClass('validation');
				parent.removeClass('error');
				parent.find('.validation-message').removeItem();
			}, 'validationOff');
		});
	};

	var toElements = function(selectorOrElements) {
		if (typeof selectorOrElements === 'string') {
			return $(selectorOrElements);
		}
		return selectorOrElements;
	};

	return {
		/**
		 * @param selectorOrElements CSS selector or doc object with selected elements
		 * @param function to execute before submitting form
		 *
		 * Usage example:
		 * 	form.submitOnChange(<FORM_ELEMENTS>, BEFORE_FUNCTION);
		 */
		'submitOnChange': function(selectorOrElements, beforeSubmit) {
			var elements = toElements(selectorOrElements);
			elements.on('change', function() {
				var form = $(this.form);
				if (beforeSubmit) {
					beforeSubmit();
					setTimeout(function() {
						form.trigger('submit'); // Precisa ser assincrono por causa do Safari
					}, 100);
				} else {
					form.trigger('submit');
				}
			});

			if (isIE8) {
				elements.on('click', function() {
					if ($(this).attr('type') == 'radio') {
						$(this).trigger('blur');
						$(this).trigger('focus');
					}
				});
			}
		},

		/**
		 * @param elements
		 *
		 * Usage example:
		 * 	form.submitOnBlur(<FORM_ELEMENTS>);
		 */
		'submitOnBlur': function(selector) {
			var els = $(selector);
			els.on('blur', function() {
				if (this.value.trim().length > 0) {
					$(this.form).trigger('submit');
				}
			});
		},

		/**
		 * @param element
		 *
		 * Usage example:
		 * 	form.focus(<FORM_ELEMENT>);
		 */
		'focus': function(selector) {
			$(selector).trigger('focus');
			if (isMobile && $(selector).first()) {
				$(selector).parent().scrollIntoView();
			}
		},

		/**
		 * @param form
		 * @param configs with custom messages, success callback and error callback
		 *
		 * Usage example:
		 * 	form.validate(<FORM_ELEMENT>, configs);
		 */
		'validate': function(form, configs) {
			if (configs && configs.messages) {
				validationMessages = configs.messages;
			}
			var $form = toElements(form);
			$form.attr('novalidate', true);
			$form.throttle('submit', function() {
				if (isValid.call(this, $form)) {
					configs && configs.success && configs.success.apply(this, arguments);
				} else {
					configs && configs.error && configs.error.apply(this, arguments);
				}
			});
		},

		/**
		 * @param label anchor
		 * @param interpolated message
		 *
		 * Usage example:
		 * 	form.appendMessage(<INPUT_LABEL_ANCHOR>, INTERPOLATED_MESSAGE);
		 */
		'appendMessage': function(label, message) {
			var $label = $(label);
			appendMessage($label, message);
			addEventToClearErrorMessages($label.find('input'));
		},

		/**
		 * @param form
		 *
		 * Usage example:
		 * 	form.removeValidationErrors(<FORM_ELEMENT>);
		 */
		'removeValidationErrors': function(form) {
			removeValidationErrors($(form));
		}

	};
});
