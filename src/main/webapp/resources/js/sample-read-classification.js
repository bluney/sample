/**
 * 
 */
;function ApplyTable(table){
	table.dataTable( {
		"aaSorting": [[ 0, "asc" ]],
		"sDom": "<'row view-filter'<'col-sm-12'<'pull-left'i><'pull-right'f><'clearfix'>>>rt<'row view-pager'<'col-sm-12'<'text-left'p>>>",
		"sPaginationType": "bootstrap",
		"iDisplayLength": 100,
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
		"TableTools": {
			"sSwfPath": "resources/plugins/datatables/copy_csv_xls_pdf.swf",
			"aButtons": [
				"copy",
				"print",
				{
					"sExtends":    "collection",
					"sButtonText": 'Save <span class="caret" />',
					"aButtons":    [ "csv", "xls", "pdf" ]
				}
			]
		}
	});
}

// Run Datables plugin and create 3 variants of settings
function AllTables(){
	ApplyTable($('#readClassification'));
	LoadSelect2Script(MakeSelect2);
}

function MakeSelect2(){
	$('select').select2();
	$('.dataTables_filter').each(function(){
		$(this).find('label input[type=text]').attr('placeholder', 'Search');
	});
}

function onClickGoToMainPage(){
	var url = "/service/market/market.html";
	LoadAjaxContent(url);	
}


$(document).ready(function() {
	// Load Datatables and run plugin on tables 
	LoadDataTablesScripts(AllTables);
	
});



