Number.prototype.leftPad = function (n,str){
    return Array(n-String(this).length+1).join(str||'0')+this;
}

define(['doc'], function($) {   
    'use strict'

    var $document             = $(document),
        $videoPlayer          = $('#video-player'),
        videoPlayer           = $videoPlayer.first(),
        $videoControls        = $('#video-controls'),
        videoControls         = $videoControls.first(),
        $playButton           = $videoControls.find('#video-play'),
        $fullScreenButton     = $videoControls.find('#video-fullscreen'),
        $videoProgress        = $videoControls.find('#video-progress'),
        $videoPlayProgress    = $videoControls.find('#video-play-progress'),
        videoPlayProgress     = $videoPlayProgress.first(),
        $videoProgressBox     = $videoControls.find('#video-progress-box'),
        videoProgressBox      = $videoProgressBox.first(),
        $videoLength          = $videoControls.find('#video-length'),
        $videoTime            = $videoControls.find('#video-time'),
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
             if (!videoPlayer.paused) $videoControls.addClass('hide');
        });

        $videoControls.on('mouseout', function() {
             if (!videoPlayer.paused) $videoControls.addClass('hide');
        });

        var hours = parseInt(videoPlayer.duration / 60 / 60, 10),
            mins = parseInt(videoPlayer.duration / 60, 10),
            secs = parseInt(videoPlayer.duration % 60, 10),
            finalLength = hours === 0 ? mins + ':' + secs : hours + ':' + mins + ':' + secs;

        $videoLength.text(finalLength);

        $videoTime.text('00:00');
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
            var hours = parseInt(videoPlayer.currentTime / 60 / 60, 10),
                mins = parseInt(videoPlayer.currentTime / 60, 10),
                secs = parseInt(videoPlayer.currentTime % 60, 10),
                currentTime = hours === 0 ? 
                    mins.leftPad(2) + ':' + secs.leftPad(2) : 
                    hours.leftPad(2) + ':' + mins.leftPad(2) + ':' + secs.leftPad(2);

            $videoTime.text(currentTime);

            videoPlayProgress.style.left = ( (videoPlayer.currentTime / videoPlayer.duration) * (videoProgressBox.offsetWidth) ) + "px";
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

        $document.on('keydown' , function(e) {
            e = e || window.event; 
            if ( (e.keyCode || e.which) === 27 ) fullscreenOff();
        });
    };

    $fullScreenButton.on('click', function() {
        isFullscreen ? fullscreenOff() : fullscreenOn();
    });

    var findPosX = function(progressBox) { 
        var curleft = progressBox.offsetLeft;
        while( progressBox = progressBox.offsetParent ) {
            curleft += progressBox.offsetLeft;
        } 
        return curleft; 
    }

    var setPlayProgress = function(clickX) {
        var progressBox = $videoControls.find('#video-progress-box').first();

        var newPercent = Math.max( 0, Math.min(1, (clickX - findPosX(progressBox)) / progressBox.offsetWidth) ); 
        videoPlayer.currentTime = newPercent * videoPlayer.duration; 
        videoPlayProgress.style.left = newPercent * (progressBox.offsetWidth)  + "px";
    }

    $videoPlayProgress.on('mousedown', function() {
        stopTrackPlayProgress();

        playPause();

        $document.on('mousemove', function(e) {
            setPlayProgress(e.pageX);
        });

        $videoProgressBox.on('mouseup', function(e) {
            $document.off('mousedown');
            $document.off('mousemove');

            videoPlayer.play();
            setPlayProgress(e.pageX);
            trackPlayProgress();
        });
    });

    $document.on('keydown' , function(e) {
        e = e || window.event; 
        if ( (e.keyCode || e.which) === 32 ) playPause();
    });
});

