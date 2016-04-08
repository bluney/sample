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
	var param = "?";
	if($('#chk-rate').is(":checked")) {
		param += "rate=";
		param += $('#input-spinner-rate').spinner("value");
		hasParam = true;
	}
	if($('#chk-selling').is(":checked")) {
		if(hasParam) {
			param += "&";
		}
		param += "selling=";
		param += $('#input-spinner-selling').spinner("value");
		hasParam = true;
	}
	if($('#chk-lease').is(":checked")) {
		if(hasParam) {
			param += "&";
		}
		param += "lease=";
		param += $('#input-spinner-lease').spinner("value");
		hasParam = true;
	}
	
	if(!hasParam) {
		alert("WARNING : 최소 하나 이상의 옵션을 선택해야 합니다.");
		return;
	}
	
	$.ajax({
		type : "GET",
		url : "/service/market/calcurateRateOfEarning",//+param,
		data : {"rate" : $('#input-spinner-rate').spinner("value"),
				"selling" : $('#input-spinner-selling').spinner("value"),
				"lease" : $('#input-spinner-lease').spinner("value")},
		success : function (response) {
			$("#divEarningRate").html(response);
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
		}
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
			//alert("checked id=" + $(this).attr("id"));
			selector.spinner("option", "disabled", false);
			return;
		}
		//alert("unchecked id=" + $(this).attr("id"));
		selector.spinner("option", "disabled", true);
		// 'unchecked' event code
	});
	
	$("#input-spinner-rate").spinner({
		min:50,
		max:100,
		step:1
	}).val(75).width(50);

	$("#input-spinner-selling").spinner({
		min:0.0,
		max:10.0,
		step:0.1,
		disabled:true
	}).val(5.0).width(50);
	
	$("#input-spinner-lease").spinner({
		min:0.0,
		max:10.0,
		step:0.1,
		disabled:true
	}).val(5.0).width(50);
});
