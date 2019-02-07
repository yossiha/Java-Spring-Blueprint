$(document).ready(
		function() {
			$('form.cropit').submit(
					function(event) {
						// Move cropped image data to hidden input
						var imageData = $('.image-editor').cropit('export', {
							type : 'image/jpeg',
							quality : 1,
							originalSize : true
						});

						event.preventDefault(); // prevent default action
						var post_url = $(this).attr("action"); // get form
																// action url
						var data = new FormData();

						var block = imageData.split(";");
						var contentType = block[0].split(":")[1];// In this
																	// case
																	// "image/gif,image/jpeg,image/png,
																	// etc"
						var realData = block[1].split(",")[1]; // get the real
																// base64
																// content of
																// the file

						var ext = "";
						if (contentType.indexOf("jpg") != -1
								|| contentType.indexOf("jpeg") != -1)
							ext = "jpg";
						else if (contentType.indexOf("gif") != -1)
							ext = "gif";
						else if (contentType.indexOf("png") != -1)
							ext = "png";
						else
							return false; // @TODO set proper error management

						var blob = b64toBlob(realData, contentType);
						data.append('image-data', blob, createRandomString(6)
								+ "." + ext);

						$.ajax({
							type : 'POST',
							url : $('form.cropit').attr('action'),
							enctype : 'multipart/form-data',
							processData : false, // make jQuery not to
													// process the data
							contentType : false, // make jQuery not to set
													// contentType
							cache : false,
							timeout : 1000000,
							data : data
						}).done(function() {
						}).fail(function() {
						});
					});
		});

/**
 * Convert a base64 string in a Blob according to the data and contentType.
 * 
 * @param b64Data
 *            {String} Pure base64 string without contentType
 * @param contentType
 *            {String} the content type of the file i.e (image/jpeg - image/png -
 *            text/plain)
 * @param sliceSize
 *            {Int} SliceSize to process the byteCharacters
 * @see http://stackoverflow.com/questions/16245767/creating-a-blob-from-a-base64-string-in-javascript
 * @return Blob
 */
function b64toBlob(b64Data, contentType, sliceSize) {
	contentType = contentType || '';
	sliceSize = sliceSize || 512;

	var byteCharacters = atob(b64Data);
	var byteArrays = [];

	for (var offset = 0; offset < byteCharacters.length; offset += sliceSize) {
		var slice = byteCharacters.slice(offset, offset + sliceSize);

		var byteNumbers = new Array(slice.length);
		for (var i = 0; i < slice.length; i++) {
			byteNumbers[i] = slice.charCodeAt(i);
		}

		var byteArray = new Uint8Array(byteNumbers);

		byteArrays.push(byteArray);
	}

	var blob = new Blob(byteArrays, {
		type : contentType
	});
	return blob;
}

function createRandomString(length) {
	var str = "";
	for (; str.length < length; str += Math.random().toString(36).substr(2))
		;
	return str.substr(0, length);
}
