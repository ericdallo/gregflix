define('cast', ['doc'], function($) {
    'use strict'

    var remotePlayer,
        remotePlayerController,
        mediaInfo;

    var connectionChanged = function() {

        if (remotePlayer.isConnected) {
            setupRemotePlayer();
            $.broadcast('cast-connected');
        } else {
            $.broadcast('cast-disconnected');
        }
    };

    var setupRemotePlayer = function() {
        var session = cast.framework.CastContext.getInstance().getCurrentSession();

        if (session != null) {
            var request = new chrome.cast.media.LoadRequest(mediaInfo);
            request.activeTrackIds = [1];

            session.loadMedia(request);
        }

        remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.IS_PAUSED_CHANGED, function() {
            if (remotePlayer.isPaused) {
                $.broadcast('cast-paused');
            } else {
                $.broadcast('cast-played');
            }
        });

        remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.VOLUME_LEVEL_CHANGED, function() {
            $.broadcast('cast-volume-changed', {volumeLevel: remotePlayer.volumeLevel});
        });

        remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.CURRENT_TIME_CHANGED, function() {
            $.broadcast('cast-time-changed', {time: remotePlayer.currentTime});
        });
        
    };

    var playOrPause = function() {
        var session = cast.framework.CastContext.getInstance().getCurrentSession();

        if (session != null) {
            remotePlayerController.playOrPause();
        }
    };

    var seek = function(time) {
        remotePlayer.currentTime = time;
        remotePlayerController.seek();
    }

    return {
        'init': function(videoSrc, subtitleSrc) {

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
            remotePlayerController.addEventListener(cast.framework.RemotePlayerEventType.IS_CONNECTED_CHANGED, connectionChanged.bind(this));
        },

        'pause': function() {
            playOrPause();
        },

        'play': function() {
            playOrPause();
        },

        'playPause': function() {
            playOrPause();
        },

        'seekTo': function(time) {
            seek(time);
        }
    }
});