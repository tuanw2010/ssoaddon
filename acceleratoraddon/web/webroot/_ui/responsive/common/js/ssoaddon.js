$('#facebook-login').click(function(event) {
    event.preventDefault();
    var facebookForm = $('<form>', {
        'action': $(this).data('url'),
        'method': 'post'
    }).append(jQuery('<input>', {
        'name': 'scope',
        'value': 'email,publish_stream,offline_access',
        'type': 'hidden'
    }));
    $(document.body).append(facebookForm);
    facebookForm.submit();
});

$('#twitter-login').click(function(event) {
    event.preventDefault();
    var twitterForm = $('<form>', {
        'action': $(this).data('url'),
        'method': 'post'
    });
    $(document.body).append(twitterForm);
    twitterForm.submit();
});

$('#wechat-login').click(function(event) {
    event.preventDefault();
    var wechatForm = $('<form>', {
        'action': $(this).data('url'),
        'method': 'post'
    }).append(jQuery('<input>', {
        'name': 'scope',
        'value': 'snsapi_login',
        'type': 'hidden'
    }));
    $(document.body).append(wechatForm);
    wechatForm.submit();
});
