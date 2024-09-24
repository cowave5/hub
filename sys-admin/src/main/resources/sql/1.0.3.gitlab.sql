update oauth_config
set app_id       = '155cbf79f0e8dc8c6b912b2ac033e741e9efb9ecffe661571693d392c279bc5d',
    app_secret   = '5eb6f1172bb175145f29d772f8ebacde5e37be4743cebbf917da2e8c68d999ec',
    redirect_url = 'http://10.64.4.74:81/oauth/gitlab'
where app_type = 'gitlab';
