<%@ page import="com.google.appengine.api.blobstore.BlobstoreService"%>
<%@ page
	import="com.google.appengine.api.blobstore.BlobstoreServiceFactory"%>
<%
	//BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	//String uploadUrl = blobstoreService.createUploadUrl("/image-upload");
	String uploadUrl = "";
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Image Upload</title>
</head>
<body>
	<h1>Image Upload</h1>

	<form method="POST" enctype="multipart/form-data"
		action="<%=uploadUrl%>">
		<p>Upload Image(jpg/png):</p>
		<input type="file" name="image" accept="image/*">
		<br />
		<br />
		<button>Upload</button>
	</form>
	
	<h2>Documentation:</h2>
	<p>
		before create project, we read the exercise document from this link:<a href="http://borg.csueastbay.edu/~grewe/CS651/Exercises/ExerciseSaaS.html">exercise page.</a>
		<br/>and found some links to which api we should use.  
		You can see a screenshot below of what these hint links are:
	</p>

	<p>This first link of Google Cloud Vision API tell use how to use the client library.</p>
	<p>
		the code shows generally- grabs an image from the request, and get data from it.
		then we can send it to the Google Cloud Vision API. we can get the image as an array of bytes 
		and  rebuilds the image from these bytes and then the image annotator runs with the image 
		and feature set in mind and <i>rates</i> the correlation of the image to each feature. 
		And it prints these features. In the servlet code for this particular project
		you'll find that much of the code screened here gets used.
	</p>
	<img src="https://s1.ax1x.com/2020/04/15/J9l3F0.jpg">
	<img src="https://s1.ax1x.com/2020/04/15/J9lloq.jpg">

	<p>
		In the other hint link (<a
			href="http://www.studywithdemo.com/2015/03/upload-image-file-using-jspservlet.html#more">corresponding
			web tutorial</a>) we find the following:
	</p>
	<img src="https://s1.ax1x.com/2020/04/15/J9lMes.jpg">
	
	<h3>The HTML Form</h3>
	<p>
		we come to the question- how wen can upload the image
		the user gives and send it to the Cloud Vision API? 
		wen can use an actual <i>Internet</i> location. Googling around directs
		one to many different possible solutions for this file uploading
		issue, but finally, we usd Blobstore. This is a solutions from Google in their Google Cloud Platform, 
		but they're more of a hassle than Blobstore is.
	</p>
	<img src="https://s1.ax1x.com/2020/04/15/J9lQwn.jpg">

	<p>We can see at the beggining of our index.jsp page that
		we import Blobstore and get a URL to upload the image to. 
		We've got a form that on submission posts the image uploaded to the uploadURL (defined as
		the servlet). And below that we've got the documentation that you are
		currently reading. Blobstore usefully acts as our intermediary host,
		getting the image from this HTML form to the servlet.</p>

	<h3>The Servlet</h3>
	<p>How about the servlet? bellow is the code,
	we can see that, we just get data from the request by the key which 
	we defined in the index.jsp. and use the function we defined how to get 
	the Annotation from the image. 
	 </p>
	<img src="https://s1.ax1x.com/2020/04/15/J9luLj.jpg">

</body>
</html>
