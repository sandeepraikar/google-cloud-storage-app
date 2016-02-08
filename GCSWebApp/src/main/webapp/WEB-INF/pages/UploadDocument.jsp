<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Upload Documents</title>
<!-- Latest compiled and minified CSS -->

	<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">

</head>
<body>

<h4>Welcome to Google Cloud Storage!</h4>
<div>

<fieldset>
<span id="successMsg" class="alert-success"></span>
<span id="errMsg" class="alert-danger"></span>
	
	<form id="idUploadDocument" enctype="multipart/form-data" action="uploadDocumentImpl" method="post">
		<div class="form-group row">
			<label class="col-md-2" for="uploadDoc">Upload Document<em
				style="color: red;">*</em></label>
			<div class="col-md-6">
				<input id="fileUpload" type="file" name="uploadDocument"
					class="form-control" />
			</div>

			<div class="col-md-2">
				<input type="submit" id="btnUploadDocument" value="Upload"
					class="form-control" />
			</div>
		</div>
	</form>
	<input id="txtHiddenUploadedFileName" type="hidden" name="fileName" type="text" />
			
	
	
	</fieldset>
</div>

<script type="text/javascript">
	$("#errMsg").text(""); 		
	$("#errMsg").hide();  
	
	$("#btnUploadDocument").click(function() {  
		uploadedFileName = $("#fileUpload").val().replace(/C:\\fakepath\\/i,'');
		alert(uploadedFileName.length);
		if(uploadedFileName.length>0){
				$("#btnUploadDocument").submit();
		}
	});
</script>
</body>
</html>