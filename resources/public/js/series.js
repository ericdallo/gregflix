define(['doc'], function($) {
	'use strict'

	if (window.location.hash) {
		$('#select-video-switch-' + window.location.hash.replace('#', '')).first().checked = true;
	}

	$('.select-video-switch').on('change', function() {
		if (this.checked) {
			window.location.hash = '#' + this.id.replace('select-video-switch-', '');
		}
	});

	$('.back').on('click', function() {
		$('.select-video-switch:checked').first().checked = false;
	});
});