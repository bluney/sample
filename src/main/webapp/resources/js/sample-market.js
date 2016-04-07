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

function ApplyTable(table){
	table.dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		"sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'p<'clearfix'>>",
		//"sDom": "<'box-content'<'col-sm-6'f><'col-sm-6 text-right'l><'clearfix'>>rt<'box-content'<'col-sm-6'i><'col-sm-6 text-right'p><'clearfix'>>",
		//"sDom": '<"top"i>rt<"bottom"p><"clear">',
		"sPaginationType": "bootstrap",
		"oLanguage": {
			"sSearch": "",
			"sLengthMenu": '_MENU_'
		}
	});
}
// Run Datables plugin and create 3 variants of settings
function AllTables(){
	ApplyTable($('table.display'))
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
});