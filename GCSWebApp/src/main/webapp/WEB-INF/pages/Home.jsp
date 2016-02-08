<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@page import="java.security.Principal"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Google Cloud Storage App</title>
<!-- Latest compiled and minified CSS -->
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>

<div>
	<button id="viewBucket">View Google Cloud Bucket</button>
	<button id="uploadDoc">Upload New Document</button>
</div>

<div id="bucketItems">
					<c:choose>
						<c:when test="${!empty objectsInBucket}">
							<div class="search-result">
								<div class="result-header">
									<h3 id="resultCount">Objects in Google Cloud Storage</h3>
								</div>
								<div class="table-responsive">
									<table class="table table-bordered table-advance" id="tblBucketContents">
										<thead>
											<tr>
												<th>File Name</th>	
												<th>Download </th>
												<th>Delete</th>							
											</tr>
										</thead>
										<tbody>
											
											<c:forEach items="${objectsInBucket}" var="object" varStatus="counter">
												<tr>
													<td><c:out value="${object}" /></td>
													<td><a id="downloadLink" href="downloadFile?name=<c:out value="${object}"/>"><i class="fa fa-download">Download</i></a></td>
													<td>
														<button id="deleteObject" name="delete">Delete</button>
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div>
								<c:out value="${emptyBucket}" />
							</div>
						</c:otherwise>
					</c:choose>
	
</div>
<br>
<br>
<span id="errMsg" class="alert-danger"></span>
<div id="UploadedItem">
		<div class="search-result">
			<div class="result-header">
				<h4>Object added in Google Cloud Storage Successfully!!!</h4>
			</div>
			<div class="table-responsive">
				<table class="table table-bordered table-advance" id="tblUploadedContent">
					<thead>
						<tr>
							<th>File Name</th>	
							<th>File size</th>
							<th>Total Time taken to upload</th>						
						</tr>
					</thead>
					<tbody>
							<tr>
								<td><c:out value="${uploadedFileName}" /></td>
								<td><c:out value="${uploadedFileSize}" /></td>
								<td><c:out value="${uploadTime}" /></td>
							</tr>											
					</tbody>
				</table>
			</div>
		</div>
</div>
<script type="text/javascript">
$("#errMsg").text("");
$("#errMsg").hide();

$("#UploadedItem").hide();
var uploadedFileName ='${uploadedFileName}';
if(uploadedFileName.length>0){
	$("#UploadedItem").show();
	alert("file uploaded successfully!!");
}
$("#uploadDoc").click(function() {
	window.location.href = "uploadDoc";
});

function validateEmail(emailCheck) {  
    var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$/;  
   if (filter.test(emailCheck)) {  
        return true;  
    }  
    else {  
        return false;  
   }  
} 

$("button[name='delete']").click(function(event) {  //on click 
	if (confirm("Are you sure you want to delete this file from Google Cloud Storage! ")) {
	var trow = $(this).parent('td').parent('tr');
	rowIndex = trow.index();
	var fileName= $('#tblBucketContents tbody tr:eq('+rowIndex+') td:eq(1)').text();
	
 	$.ajax({
		type : "POST",
		url : "deleteFile",
		data : {
			fileName : fileName								 						 
		}
	})
	.done(function(msg) {
		trow.remove();
	}); 
	}
});

 </script>
</body>
</html>