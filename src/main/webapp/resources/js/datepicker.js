    $(function(){
	$("#datepicker").datepicker({
		minDate: 0
	});
});

$.datepicker.regional['ru'] = {
	dateFormat: 'yy-mm-dd'
};
$.datepicker.setDefaults($.datepicker.regional['ru']);