define(['doc', 'form'], function($, form) {
    'use strict'

    $('input').on('focus', function() {
        $(this).parent().removeClass('invalid');
    });

    form.validate('.login', {
        messages: {},
        success: function(event) {
            this.submit();
        },
        error: function(event) {
            $('.invalid-flag').addClass('invalid');
        }
    });
});