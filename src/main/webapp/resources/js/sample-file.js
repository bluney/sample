/**
 * 
 */
$(function() {
	$("#uploader").plupload({
		runtimes : 'html5,flash,silverlight,html4',
		url : "/service/file/plupload",
		// url : "pluploadservlet", //servlet版本
		// Maximum file size
		max_file_size : '1024mb',
		chunk_size : '10mb',
		// Resize images on clientside if we can
		resize : {
			width : 200,
			height : 200,
			quality : 90,
			crop : true
		},
		// Specify what files to browse for
//		filters : [ {
//			title : "Image files",
//			extensions : "jpg,gif,png"
//		}, {
//			title : "Zip files",
//			extensions : "zip,rar"
//		} ],
		// Rename files by clicking on their titles
		rename : true,
		// Sort files
		sortable : true,
		// Enable ability to drag'n'drop files onto the widget (currently only HTML5 supports that)
		dragdrop : true,
		// Views to activate
		views : {
			list : true,
			thumbs : true, // Show thumbs
			active : 'thumbs'
		},
		// Flash settings
		flash_swf_url : '/resources/plupload/js/Moxie.swf',
		// Silverlight settings
		silverlight_xap_url : '/resources/plupload/js/Moxie.xap'
	});
});