define('login-remember', [], function() {
    'use strict'

    var STORAGE_KEY = "login";

    var login = function() {
        var login = localStorage.getItem(STORAGE_KEY);
        if (login != null) {
            return JSON.parse(login);
        }
        return null;
    }();

    if (login != null) {
        login.validated = true;

        localStorage.setItem(STORAGE_KEY, JSON.stringify(login));
    }

    return {
        'preLogin': function(username, password) {
            var login = {
                "username": username,
                "password" : password,
                "validated": false,
            };

            localStorage.setItem(STORAGE_KEY, JSON.stringify(login));
        },
        'getRemembered': function() {
            return login;
        },
        'clear': function() {
            localStorage.removeItem(STORAGE_KEY);
        }
    }
});