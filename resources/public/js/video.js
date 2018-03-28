define(['doc'], function($) {   
    'use strict'

    var $videoPlayer          = $('#video-player'),
        videoPlayer           = $videoPlayer.first(),
        $videoControls        = $('#video-controls'),
        videoControls         = $videoControls.first(),
        $playButton           = $videoControls.find('#video-play'),
        $fullScreenButton     = $videoControls.find('#video-fullscreen'),
        $videoProgress        = $videoControls.find('#video-progress'),
        $videoPlayProgress    = $videoControls.find('#video-play-progress'),
        $videoProgressBox = $videoControls.find('#video-progress-box'),
        isFullscreen          = false,
        playProgressInterval  = 0;

    $videoPlayer.removeAttr('controls');

    $videoPlayer.on('loadeddata', function() {
        $videoPlayer.on('mouseover', function() {
            $videoControls.removeClass('hide');

            if (isFullscreen) $videoControls.addClass('hide');
        });

        $videoControls.on('mouseover', function() {
            $videoControls.removeClass('hide');            
        });

        $videoPlayer.on('mouseout', function() {
             $videoControls.addClass('hide');
        });

        $videoControls.on('mouseout', function() {
             $videoControls.addClass('hide');
        });
    });

    var playPause = function() {
        if (videoPlayer.paused || videoPlayer.ended) {
            videoPlayer.play();
            $playButton.addClass('paused');
        } else {
            videoPlayer.pause();
            $playButton.removeClass('paused');
        }
    };

    $videoPlayer.on('click', playPause);
    $playButton.on('click', playPause);

    var trackPlayProgress = function() {
        (function progressTrack() { 
             $videoPlayProgress.first().style.left = ( (videoPlayer.currentTime / videoPlayer.duration) * ($videoProgressBox.first().offsetWidth) ) + "px";
             playProgressInterval = setTimeout(progressTrack, 50); 
         })(); 
    };

    var stopTrackPlayProgress = function() {
        clearTimeout(playProgressInterval);
    };

    $videoPlayer.on('play', function() { 
        $playButton.first().title = 'Pause';

        trackPlayProgress();
    }); 

    $videoPlayer.on('pause', function() {
        $playButton.first().title = 'Play';

        stopTrackPlayProgress();
    });

    var fullscreenOff = function() {
        isFullscreen = false;

        $videoPlayer.removeClass('fullscreen');
        $videoControls.removeClass('fullscreen');
        $fullScreenButton.removeClass('fullscreen-off');
    };

    var fullscreenOn = function() {
        isFullscreen = true;

        $videoPlayer.addClass('fullscreen');
        $videoControls.addClass('fullscreen');
        $fullScreenButton.addClass('fullscreen-off');

        $(document).on('keydown' , function(e) {
            e = e || window.event; 
            if ( (e.keyCode || e.which) === 27 ) fullscreenOff();
        });
    };

    $fullScreenButton.on('click', function() {
        isFullscreen ? fullscreenOff() : fullscreenOn();
    });
});

