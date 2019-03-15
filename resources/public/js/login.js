define(['doc', 'form', 'login-remember'], function($, form, $loginRemember) {
    'use strict'

    var $loginForm      = $('.login'),
        $username       = $loginForm.find('input[name="username"]'),
        $password       = $loginForm.find('input[name="password"]'),
        $rememberMeCheck = $loginForm.find('input[name="remember-me"]');

    $('input').on('focus', function() {
        $(this).parent().removeClass('invalid');
    });

    var login = $loginRemember.getRemembered();

    if (login != null) {
        $username.val(login.username);
        $password.val(login.password);
        $rememberMeCheck.first().checked = true;
    }

    form.validate($loginForm, {
        messages: {},
        success: function() {
            var isRememberMeChecked = $rememberMeCheck.first().checked;

            if (isRememberMeChecked) {
                $loginRemember.preLogin($username.val(), $password.val());
            } else {
                $loginRemember.clear();
            }

            this.submit();
        },
        error: function() {
            $('.invalid-flag').addClass('invalid');
        }
    });
});