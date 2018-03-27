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
             videoControls.style.opacity = 1; //TODO css
        });

        $videoControls.on('mouseover', function() {
             videoControls.style.opacity = 1;
        });

        $videoPlayer.on('mouseout', function() {
             videoControls.style.opacity = 0;
        });

        $videoControls.on('mouseout', function() {
             videoControls.style.opacity = 0;
        });
    });

    var playPause = function() {
        if (videoPlayer.paused || videoPlayer.ended) {
            videoPlayer.play();
        } else {
            videoPlayer.pause();
        }
    };

    $videoPlayer.on('click', playPause);
    $playButton.on('click', playPause);

    var trackPlayProgress = function() {
        (function progressTrack() { 
             $videoPlayProgress.first().style.width = ( (videoPlayer.currentTime / videoPlayer.duration) * ($videoProgressBox.first().offsetWidth) ) + "px";
             playProgressInterval = setTimeout(progressTrack, 50); 
         })(); 
    };

    var stopTrackPlayProgress = function() {
        clearTimeout(playProgressInterval);
    };

    $videoPlayer.on('play', function() { 
        this.title = 'Pause';
        this.innerHTML = '<span id="pauseButton">&#x2590;&#x2590;</span>';

        trackPlayProgress();
    }); 

    $videoPlayer.on('pause', function() {
        this.title = 'Play';
        this.innerHTML = '&#x25BA;';

        stopTrackPlayProgress();
    });

    var fullscreenOff = function() {
        isFullscreen = false;

        $videoPlayer.removeClass('fullscreen');
        $videoControls.removeClass('controls-fullscreen');
        $fullScreenButton.removeClass('fullscreen-off');
    };

    var fullscreenOn = function() {
        isFullscreen = true;
        //TODO css

        $videoPlayer.addClass('fullscreen');
        $videoControls.addClass('controls-fullscreen');
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

