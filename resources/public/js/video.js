Number.prototype.leftPad = function (n,str){
    return Array(n-String(this).length+1).join(str||'0')+this;
}

define(['doc'], function($) {
    'use strict'

    var $document             = $(document),
        $player               = $('#video-player'),
        player                = $player.first(),
        $controls             = $('#video-controls'),
        controls              = $controls.first(),
        $startButton          = $('.start-video'),
        $playButton           = $controls.find('#video-play'),
        $fullScreenButton     = $controls.find('#video-fullscreen'),
        $playProgress         = $controls.find('#video-play-progress'),
        $selectableProgress   = $controls.find('#video-progress-selectable'),
        playProgress          = $playProgress.first(),
        playProgressHalfSize  = (playProgress.offsetWidth / 2),
        $progressBox          = $controls.find('#video-progress-box'),
        progressBox           = $progressBox.first(),
        $length               = $controls.find('#video-length'),
        $time                 = $controls.find('#video-time'),
        $sound                = $controls.find('#video-sound'),
        $soundBox             = $controls.find('#video-sound-box'),
        soundBox              = $soundBox.first(),
        $soundProgress        = $controls.find('#video-sound-progress'),
        $selectableSound      = $controls.find('#video-volume-selectable'),
        soundProgress         = $soundProgress.first(),
        soundProgressHalfSize = (soundProgress.offsetWidth / 2),
        playProgressInterval  = 0;

    var isFullscreen = function() {
        return document.fullscreen || document.mozFullScreen || document.webkitIsFullScreen;
    }

    $controls.addClass('show');
    $startButton.addClass('show');
    $player.addClass('custom-controls');

    $player.removeAttr('controls');

    $player.on('loadeddata', function() {
        $startButton.removeClass('loading');

        $player.on('mouseover', function() {
            $controls.removeClass('hide');

            if (isFullscreen()) $controls.addClass('hide');
        });

        $controls.on('mouseover', function() {
            $controls.removeClass('hide');            
        });

        $player.on('mouseout', function() {
             if (!player.paused) $controls.addClass('hide');
        });

        $controls.on('mouseout', function() {
             if (!player.paused) $controls.addClass('hide');
        });

        var hours = parseInt(player.duration / 60 / 60, 10),
            mins = parseInt(player.duration / 60, 10),
            secs = parseInt(player.duration % 60, 10),
            finalLength = hours === 0 ? mins + ':' + secs : hours + ':' + mins + ':' + secs;

        $length.text(finalLength);

        $time.text('00:00');

        var soundBox = $controls.find('#video-sound-box').first();

        soundProgress.style.left = soundBox.offsetWidth - soundProgressHalfSize + "px";
    });

    var playPause = function() {
        if (player.paused || player.ended) {
            player.play();
        } else {
            player.pause();
        }
    };

    $player.on('click', playPause);
    $playButton.on('click', playPause);
    $startButton.on('click', playPause);

    $sound.on('click', function() {
        player.muted = !player.muted;
        $sound.toggleClass('mute');
    });

    var trackPlayProgress = function() {
        (function progressTrack() {
            var hours = parseInt(player.currentTime / 60 / 60, 10),
                mins = parseInt(player.currentTime / 60, 10),
                secs = parseInt(player.currentTime % 60, 10),
                currentTime = hours === 0 ? 
                    mins.leftPad(2) + ':' + secs.leftPad(2) : 
                    hours.leftPad(2) + ':' + mins.leftPad(2) + ':' + secs.leftPad(2);

            $time.text(currentTime);

            playProgress.style.left = ( (player.currentTime / player.duration) * (progressBox.offsetWidth) - playProgressHalfSize ) + "px";
            playProgressInterval = setTimeout(progressTrack, 50); 
         })(); 
    };

    var stopTrackPlayProgress = function() {
        clearTimeout(playProgressInterval);
    };

    $player.on('play', function() {
        $playButton.first().title = 'Pause';
        $startButton.removeClass('show');
        $playButton.addClass('paused');
        trackPlayProgress();
    }); 

    $player.on('pause', function() {
        $playButton.removeClass('paused');
        $playButton.first().title = 'Play';

        stopTrackPlayProgress();
    });

    var fullscreenOff = function() {
        document.exitFullscreen || document.msExitFullscreen || document.mozCancelFullScreen || document.webkitExitFullscreen;
    };

    var fullscreenOn = function() {
        var requestFullscreen = player.requestFullscreen || player.msRequestFullscreen || player.mozRequestFullScreen || player.webkitRequestFullscreen;
        requestFullscreen.call(player); 
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
        var progressBox = $controls.find('#video-progress-box').first();

        var newPercent = Math.max( 0, Math.min(1, (clickX - findPosX(progressBox)) / progressBox.offsetWidth) ); 
        player.currentTime = newPercent * player.duration; 
        playProgress.style.left = newPercent * (progressBox.offsetWidth) - playProgressHalfSize + "px";
    }

    var setSoundProgress = function(clickX) {
        var soundBox = $controls.find('#video-sound-box').first();

        var newPercent = Math.max( 0, Math.min(1, (clickX - findPosX(soundBox)) / soundBox.offsetWidth) );
        player.volume = newPercent;
        soundProgress.style.left = newPercent * (soundBox.offsetWidth) - soundProgressHalfSize + "px";
    }

    $playProgress.on('mousedown', function() {
        stopTrackPlayProgress();

        playPause();

        $document.on('mousemove', function(e) {
            setPlayProgress(e.pageX);
        });

        $document.on('mouseup', function(e) {
            $document.off('mousemove');
            $document.off('mouseup');

            player.play();
            setPlayProgress(e.pageX);
            trackPlayProgress();
        });
    });

    $selectableProgress.on('click', function(e) {
        setPlayProgress(e.pageX);
    });

    $soundProgress.on('mousedown', function() {
        $document.on('mousemove', function(e) {
            setSoundProgress(e.pageX);
        });
    });

    $selectableSound.on('click', function(e) {
        setSoundProgress(e.pageX);
    });

    $document.on('mouseup', function(e) {
        $document.off('mousemove');
    });

    $document.on('keydown' , function(e) {
        e = e || window.event;

        var pressedKey = (e.keyCode || e.which);
        if (pressedKey === 32) playPause();

        if (pressedKey === 37) {
            player.currentTime -= 10;
            $controls.toggleClass('hide');
        }
        if (pressedKey === 39) {
            player.currentTime += 10;
            $controls.toggleClass('hide');
        }
    });
});

