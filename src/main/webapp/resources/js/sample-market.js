/**
 * 
 */

function importExcel(){
	var specs = "left=10,top=10,width=900,height=600";
	specs += ",toolbar=no,menubar=no,status=no,scrollbars=no,resizable=no";
	window.open("/service/market/requestExcel", "시계열자료 가져오기", specs);	
}

function analyzeMarketTimeSeries(){
	$.ajax({
		type : "GET",
		url : "/service/market/analyzeMarketTimeSeries",
		//data : {"code" : updateLevel},
		success : function (response) {
			//$("#level" + updateLevel + "Selector").html(response);
		}
	});		
}

function calcurateRateOfEarning(){
	var hasParam = false;
	var rate = null;
	var selling = null;
	var lease = null;

	if($('#chk-rate').is(":checked")) {
		rate = $('#input-spinner-rate').pcntspinner("value");
	}
	if($('#chk-selling').is(":checked")) {
		selling = $('#input-spinner-selling').pcntspinner("value");
	}
	if($('#chk-lease').is(":checked")) {
		lease = $('#input-spinner-lease').pcntspinner("value");
	}
	
	if(rate || selling || lease) {
		hasParam = true;
	}
	
	if(!hasParam) {
		alert("WARNING : 최소 하나 이상의 옵션을 선택해야 합니다.");
		return;
	}
	
	$.ajax({
		type : "GET",
		url : "/service/market/calcurateRateOfEarning",//+param,
		data : {"rate" : rate,
				"selling" : selling,
				"lease" : lease,
				"timing" : $('#input-spinner-timing').weekspinner("value")},
		success : function (response) {
			$("#divEarningRate").html(response);
		}
	});		
}

function processBestCase() {
	$.ajax({
		type : "GET",
		url : "/service/market/processBestCase",
		success : function (response) {
			//$("#level" + updateLevel + "Selector").html(response);
		}
	});	
}

function ApplyTable(table){
	table.dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		//"sDom": "<'row view-filter'<'col-sm-12'<'pull-left'l><'pull-right'f><'clearfix'>>>t<'row view-pager'<'col-sm-12'<'text-center'ip>>>",
		//"sDom": "<'col-sm-8 text-right'i<'clearfix'>><'col-sm-4 text-left'f<'clearfix'>>rt<'col-sm-12 text-left'p><'clearfix'>",
		"sDom": "<'row view-filter'<'col-sm-12'<'pull-left'i><'pull-right'f><'clearfix'>>>rt<'row view-pager'<'col-sm-12'<'text-left'p>>>",
		//"sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
		//"sDom": '<"top"i>rt<"bottom"p><"clear">',
		"sPaginationType": "bootstrap",
		"oLanguage": {
			"sSearch": "",
			"sLengthMenu": '_MENU_',
			"oPaginate": {
				"sNext": "",
				"sPrevious": ""
			},
			"sInfo": " _START_~_END_ of _TOTAL_ entries",
			"sInfoEmpty": "0 entries",
			"sInfoFiltered": "(_MAX_ total entries)",
		},
		"bRetrieve" : true,
		"bDestroy" : true
	});
}

// Run Datables plugin and create 3 variants of settings
function AllTables(){
	ApplyTable($('#tableSelling'));
	ApplyTable($('#tableLease'));
	ApplyTable($('#tableRate'));
	ApplyTable($('#tableGinSelling'));
	ApplyTable($('#tableGinLease'));
	ApplyTable($('#tableEarningRate'));
	LoadSelect2Script(MakeSelect2);
}
function MakeSelect2(){
	$('select').select2();
	$('.dataTables_filter').each(function(){
		$(this).find('label input[type=text]').attr('placeholder', 'Search');
	});
}

$.widget( "ui.pcntspinner", $.ui.spinner, {
    _format: function(value) { return value + ' %'; },
    _parse: function(value) { return parseFloat(value); }
});

$.widget( "ui.weekspinner", $.ui.spinner, {
    _format: function(value) { return value + ' 주'; },
    _parse: function(value) { return parseInt(value); }
});


function ondblclickForClassfication(classification) {
	var url = "/service/market/readClassification.html?classification=" + classification;
	LoadAjaxContent(url);	
}

$(document).ready(function() {
	// Load Datatables and run plugin on tables 
	LoadDataTablesScripts(AllTables);
	
	// Add Drag-n-Drop feature
	WinMove();
	
	$('.calcurate-rate-of-earning').change(function() {
		var selector;
		var id = $(this).attr("id");
		if(id == "chk-rate") {
			selector = $('#input-spinner-rate');
		} else if(id == "chk-selling") {
			selector = $('#input-spinner-selling');
		} else if(id == "chk-lease") {
			selector = $('#input-spinner-lease');
		}
	
		if($(this).is(":checked")) {
			selector.spinner("option", "disabled", false);
		} else {
			selector.spinner("option", "disabled", true);
		}
	});
	
	$("#input-spinner-rate").pcntspinner({
		min:50,
		max:100,
		step:1
	}).width(60);

	$("#input-spinner-selling").pcntspinner({
		min:0.0,
		max:10.0,
		step:0.1
//		disabled:true
	}).width(60);
	
	$("#input-spinner-lease").pcntspinner({
		min:0.0,
		max:10.0,
		step:0.1
	}).width(60);
	
	$("#input-spinner-rate").pcntspinner("value", 75);
	$("#input-spinner-selling").pcntspinner("value", 5.0);
	$("#input-spinner-lease").pcntspinner("value", 5.0);
	
	$("#input-spinner-timing").weekspinner({
		min:4*6,
		max:4*36,
		step:4
	}).width(60);
	$("#input-spinner-timing").weekspinner("value", 104);

});
