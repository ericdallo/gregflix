define('cast', ['doc'], function($) {
    'use strict'

    var remotePlayer,
        remotePlayerController,
        mediaInfo,
        currentMedia,
        videoCallback = {};

    var onConnected = function() {

        if (remotePlayer.isConnected) {
            setupRemotePlayer();
            videoCallback.connected();
        }
    };

    function onMediaDiscovered(how, media) {
       currentMedia = media;
    }

    var setupRemotePlayer = function() {
        var castSession = cast.framework.CastContext.getInstance().getCurrentSession();

        if (castSession != null && currentMedia == null) {
            var request = new chrome.cast.media.LoadRequest(mediaInfo);
            request.activeTrackIds = [1];

            castSession.loadMedia(request).then(onMediaDiscovered.bind(this, 'loadMedia'),function(errorCode) { console.log('Cast error code: ' + errorCode); });
        }

        remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.IS_PAUSED_CHANGED, function() {
            if (remotePlayer.isPaused) {
                videoCallback.paused();
            } else {
                videoCallback.played();
            }
        });

        remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.VOLUME_LEVEL_CHANGED, function() {
            videoCallback.volumeChanged(remotePlayer.volumeLevel);
        });
        
    };

    return {
        'init': function(videoSrc, subtitleSrc, callback) {
            videoCallback = callback;

            cast.framework.CastContext.getInstance().setOptions({
              receiverApplicationId:
                chrome.cast.media.DEFAULT_MEDIA_RECEIVER_APP_ID,
                autoJoinPolicy: chrome.cast.AutoJoinPolicy.ORIGIN_SCOPED
            });

            var subtitle = new chrome.cast.media.Track(1, chrome.cast.media.TrackType.TEXT);
            subtitle.trackContentId = subtitleSrc;
            subtitle.trackContentType = 'text/vtt';
            subtitle.subtype = chrome.cast.media.TextTrackType.SUBTITLES;
            subtitle.name = 'Subtitle';
            subtitle.language = 'pt-BR';
            subtitle.customData = null;

            mediaInfo = new chrome.cast.media.MediaInfo(videoSrc);
            mediaInfo.contentType = 'video/mp4';
            mediaInfo.metadata = new chrome.cast.media.GenericMediaMetadata();
            mediaInfo.customData = null;
            mediaInfo.streamType = chrome.cast.media.StreamType.BUFFERED;
            mediaInfo.textTrackStyle = new chrome.cast.media.TextTrackStyle();
            mediaInfo.duration = null;
            mediaInfo.tracks = [subtitle];

            remotePlayer = new cast.framework.RemotePlayer();
            remotePlayerController = new cast.framework.RemotePlayerController(remotePlayer);
            remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.IS_CONNECTED_CHANGED, onConnected.bind(this));
        },

        'pause': function() {
            var session = cast.framework.CastContext.getInstance().getCurrentSession();

            if (session != null && currentMedia != null) {
                currentMedia.pause(null, null, null);
            }
        }
    }
});