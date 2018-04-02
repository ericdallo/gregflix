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
        $startButton          = $('.start-video'),
        $playButton           = $videoControls.find('#video-play'),
        $fullScreenButton     = $videoControls.find('#video-fullscreen'),
        $videoPlayProgress    = $videoControls.find('#video-play-progress'),
        videoPlayProgress     = $videoPlayProgress.first(),
        playProgressHalfSize  = (videoPlayProgress.offsetWidth / 2),
        $videoProgressBox     = $videoControls.find('#video-progress-box'),
        videoProgressBox      = $videoProgressBox.first(),
        $videoLength          = $videoControls.find('#video-length'),
        $videoTime            = $videoControls.find('#video-time'),
        $videoSound           = $videoControls.find('#video-sound'),
        $videoSoundBox        = $videoControls.find('#video-sound-box'),
        videoSoundBox         = $videoSoundBox.first(),
        $videoSoundProgress   = $videoControls.find('#video-sound-progress'),
        videoSoundProgress    = $videoSoundProgress.first(),
        soundProgressHalfSize = (videoSoundProgress.offsetWidth / 2),
        playProgressInterval  = 0;

    var isFullscreen = function() {
        return document.fullscreen || document.mozFullScreen || document.webkitIsFullScreen;
    }

    $videoControls.addClass('show');
    $startButton.addClass('show');

    $videoPlayer.removeAttr('controls');

    $videoPlayer.on('loadeddata', function() {
        $startButton.removeClass('loading');

        $videoPlayer.on('mouseover', function() {
            $videoControls.removeClass('hide');

            if (isFullscreen()) $videoControls.addClass('hide');
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

        var soundBox = $videoControls.find('#video-sound-box').first();

        videoSoundProgress.style.left = soundBox.offsetWidth - soundProgressHalfSize + "px";
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
    $startButton.on('click', playPause);

    $videoSound.on('click', function() {
        videoPlayer.muted = !videoPlayer.muted;
        $videoSound.toggleClass('mute');
    });

    var trackPlayProgress = function() {
        (function progressTrack() {
            var hours = parseInt(videoPlayer.currentTime / 60 / 60, 10),
                mins = parseInt(videoPlayer.currentTime / 60, 10),
                secs = parseInt(videoPlayer.currentTime % 60, 10),
                currentTime = hours === 0 ? 
                    mins.leftPad(2) + ':' + secs.leftPad(2) : 
                    hours.leftPad(2) + ':' + mins.leftPad(2) + ':' + secs.leftPad(2);

            $videoTime.text(currentTime);

            videoPlayProgress.style.left = ( (videoPlayer.currentTime / videoPlayer.duration) * (videoProgressBox.offsetWidth) - playProgressHalfSize ) + "px";
            playProgressInterval = setTimeout(progressTrack, 50); 
         })(); 
    };

    var stopTrackPlayProgress = function() {
        clearTimeout(playProgressInterval);
    };

    $videoPlayer.on('play', function() {
        $playButton.first().title = 'Pause';
        $startButton.removeClass('show');
        $playButton.addClass('paused');
        trackPlayProgress();
    }); 

    $videoPlayer.on('pause', function() {
        $playButton.removeClass('paused');
        $playButton.first().title = 'Play';

        stopTrackPlayProgress();
    });

    var fullscreenOff = function() {
        document.exitFullscreen || document.msExitFullscreen || document.mozCancelFullScreen || document.webkitExitFullscreen;
    };

    var fullscreenOn = function() {
        var requestFullscreen = videoPlayer.requestFullscreen || videoPlayer.msRequestFullscreen || videoPlayer.mozRequestFullScreen || videoPlayer.webkitRequestFullscreen;
        requestFullscreen.call(videoPlayer); 
    };

    $fullScreenButton.on('click', function() {
        isFullscreen() ? fullscreenOff() : fullscreenOn();
    });

    var findPosX = function(box) { 
        var curleft = box.offsetLeft;
        while( box = box.offsetParent ) {
            curleft += box.offsetLeft;
        } 
        return curleft; 
    }

    var setPlayProgress = function(clickX) {
        var progressBox = $videoControls.find('#video-progress-box').first();

        var newPercent = Math.max( 0, Math.min(1, (clickX - findPosX(progressBox)) / progressBox.offsetWidth) ); 
        videoPlayer.currentTime = newPercent * videoPlayer.duration; 
        videoPlayProgress.style.left = newPercent * (progressBox.offsetWidth) - playProgressHalfSize + "px";
    }

    var setSoundProgress = function(clickX) {
        var soundBox = $videoControls.find('#video-sound-box').first();

        var newPercent = Math.max( 0, Math.min(1, (clickX - findPosX(soundBox)) / soundBox.offsetWidth) );
        videoPlayer.volume = newPercent;
        videoSoundProgress.style.left = newPercent * (soundBox.offsetWidth) - soundProgressHalfSize + "px";
    }

    $videoPlayProgress.on('mousedown', function() {
        stopTrackPlayProgress();

        playPause();

        $document.on('mousemove', function(e) {
            setPlayProgress(e.pageX);
        });

        $document.on('mouseup', function(e) {
            $document.off('mousemove');
            $document.off('mouseup');

            videoPlayer.play();
            setPlayProgress(e.pageX);
            trackPlayProgress();
        });
    });

    $videoSoundProgress.on('mousedown', function() {
        $document.on('mousemove', function(e) {
            setSoundProgress(e.pageX);
        });
    });

    $document.on('mouseup', function(e) {
        $document.off('mousemove');
    });

    $document.on('keydown' , function(e) {
        e = e || window.event; 
        if ( (e.keyCode || e.which) === 32 ) playPause();
    });
});

