Number.prototype.leftPad = function (n,str){
    return Array(n-String(this).length+1).join(str||'0')+this;
}

define(['doc', 'cast'], function($, $cast) {
    'use strict'

    var $document             = $(document),
        $player               = $('#video-player'),
        player                = $player.first(),
        $controls             = $('#video-controls'),
        controls              = $controls.first(),
        $startButton          = $('.start-video'),
        $playButton           = $controls.find('#video-play'),
        $fullscreen           = $controls.find('.fullscreen'),
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
        playProgressInterval  = 0,
        castOn                = false;

    var isMobile = function() {
      var check = false;
      (function(a){if(/(android|bb\d+|meego).+mobile|avantgo|bada\/|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od)|iris|kindle|lge |maemo|midp|mmp|mobile.+firefox|netfront|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|series(4|6)0|symbian|treo|up\.(browser|link)|vodafone|wap|windows ce|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))) check = true;})(navigator.userAgent||navigator.vendor||window.opera);
      return check;
    };

    if (isMobile()) {
        $startButton.removeClass('loading');

        $player.parent().addClass('show-cover');
    }

    var initCastOptions = function() {

        player.disableRemotePlayback = true;

        var videoSrc = $player.find('source').attr('src'),
            subtitleSrc = $player.find('#subtitle').attr('src');

        $cast.init(videoSrc, subtitleSrc);
    };

    var initControls = function() {
        $controls.addClass('show');
        $startButton.addClass('show');
        $player.removeAttr('controls');

        $time.text('00:00');
    }();

    var play = function(){
        player.play();
        $cast.play();
    };

    var pause = function(){
        player.pause();
        $cast.pause();
    };

    var playPause = function() {
        if (castOn) {
            $cast.playPause();
            return;
        }

        (player.paused || player.ended) ? play() : pause()
    };

    var isFullScreen = function() {
       return !!(document.fullScreen || document.webkitIsFullScreen || document.mozFullScreen || document.msFullscreenElement || document.fullscreenElement);
    }

    var isIOS = function() {
        return !!navigator.platform && /iPad|iPhone|iPod/.test(navigator.platform);
    }

    $player.on('loadeddata', function() {
        $startButton.removeClass('loading');
        $player.parent().removeClass('show-cover');

        $player.on('mouseover', function() {
            $controls.removeClass('hide');
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
            secs = parseInt(player.duration % 60, 10);

        if (mins > 60) {
            mins = mins % 60;
        }

        var finalLength = hours === 0 ? mins + ':' + secs : hours + ':' + mins + ':' + secs;

        $length.text(finalLength);

        var soundBox = $controls.find('#video-sound-box').first();

        soundProgress.style.left = soundBox.offsetWidth - soundProgressHalfSize + "px";

        initCastOptions();
    });

    $player.on('click', playPause);
    $playButton.on('click', playPause);
    $startButton.on('click', playPause);

    $sound.on('click', function() {
        player.muted = !player.muted;
        $sound.toggleClass('mute');
    });

    var setProgressBar = function(time) {
        var hours = parseInt(time / 60 / 60, 10),
            mins = parseInt(time / 60, 10),
            secs = parseInt(time % 60, 10),
            currentTime = hours === 0 ? 
                mins.leftPad(2) + ':' + secs.leftPad(2) : 
                hours.leftPad(2) + ':' + mins.leftPad(2) + ':' + secs.leftPad(2);

        $time.text(currentTime);

        playProgress.style.left = ( (time / player.duration) * (progressBox.offsetWidth) - playProgressHalfSize ) + "px";
    };

    var trackPlayProgress = function() {
        (function progressTrack() {
            setProgressBar(player.currentTime);
            playProgressInterval = setTimeout(progressTrack, 50); 
         })(); 
    };

    var stopTrackPlayProgress = function() {
        clearTimeout(playProgressInterval);
    };

    $player.on('play', function() {
        if (player.readyState >= 1) {
            $startButton.removeClass('show');
            $startButton.removeClass('downloading');
        }
        $playButton.first().title = 'Pause';
        $playButton.addClass('paused');
        trackPlayProgress();
    }); 

    $player.on('pause', function() {
        $playButton.removeClass('paused');
        $playButton.first().title = 'Play';

        stopTrackPlayProgress();
    });

    $fullScreenButton.on('click', function() {
        if (isIOS()) {
            player.removeAttribute('playsinline');
            player.removeAttribute('webkit-playsinline');
            play();
        }

        if (player.requestFullscreen) player.requestFullscreen();
        else if (player.mozRequestFullScreen) player.mozRequestFullScreen();
        else if (player.webkitRequestFullScreen) player.webkitRequestFullScreen();
        else if (player.msRequestFullscreen) player.msRequestFullscreen();
    });

    var fullscreenOff = function() {
        if (!isFullScreen()) {
            player.setAttribute('playsinline', '');
            player.setAttribute('webkit-playsinline', '');
        }
    };

    $document.on('fullscreenchange', fullscreenOff);
    $document.on('mozfullscreenchange', fullscreenOff);
    $document.on('webkitfullscreenchange', fullscreenOff);
    $document.on('msfullscreenchange', fullscreenOff);

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
        $cast.seekTo(player.currentTime);
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

    $player.on('waiting', function () {
        if (player.readyState >= 1) {
            $startButton.addClass('downloading');
            $startButton.addClass('show');
        } else {
            $startButton.addClass('loading');
            $startButton.addClass('show');
        }
    });

    $player.on('playing', function () {
        $startButton.removeClass('show');
        $startButton.removeClass('downloading');
    });

    $player.on('cast-connected', function() {
        castOn = true;
        $startButton.removeClass('show');
        $playButton.addClass('paused');
        $fullscreen.addClass('hide');

        player.muted = true;
        pause();
    });

    $player.on('cast-disconnected', function() {
        castOn = false;
        $fullscreen.removeClass('hide');

        player.muted = false;
    });

    $player.on('cast-played', function() {
        $startButton.removeClass('show');
        $playButton.addClass('paused');
    });

    $player.on('cast-paused', function() {
        $startButton.addClass('show');
        $playButton.removeClass('paused');
    });

    $player.on('cast-time-changed', function(event) {
        player.currentTime = event.detail.time;
        setProgressBar(event.detail.time);
    });

});
