package saasex;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;

/**
 * after user submits the image,
 * Blobstore upload the file and then called this servlet.
 * and now we can request the Vision API.
 */
@WebServlet("/image-upload")
public class ImageUploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {

		PrintWriter writer = res.getWriter();
		res.setContentType("text/html");

		List<EntityAnnotation> labels = getImageLabelsByKey(req, "image");
		if (labels == null) {
			writer.println("!Error! Please Upload Image File!");
			return;
		}

		writer.println("</a>");
		writer.println("<p>Labels of the image from Google Cloud Vision:</p>");
		writer.println("<ul>");
		for (EntityAnnotation label : labels) {
			writer.println("<li>" + label.getDescription() + " " + label.getScore());
		}
		writer.println("</ul>");
	}

	private List<EntityAnnotation> getImageLabelsByKey(HttpServletRequest req, String key) throws IOException {
		BlobstoreService blobService = BlobstoreServiceFactory.getBlobstoreService();
		Map<String, List<BlobKey>> blobs = blobService.getUploads(req);
		List<BlobKey> blobKeys = blobs.get(key);

		if (blobKeys.size() <= 0) {
			return null;
		}

		// get the first key;
		BlobKey blobKey = blobKeys.get(0);

		// check file info
		BlobInfo blobInfo = new BlobInfoFactory().loadBlobInfo(blobKey);
		if (blobInfo.getSize() == 0) {
			blobService.delete(blobKey);
			return null;
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		int block = BlobstoreService.MAX_BLOB_FETCH_SIZE;
		long index = 0;
		while (true) {
			byte[] b = blobService.fetchData(blobKey, index, index + block - 1);
			baos.write(b);

			if (b.length < block) {
				break;
			}

			index += block;
		}

		byte imgBytes[] = baos.toByteArray();
		Image image = Image.parseFrom(imgBytes);

		Feature feature = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
		AnnotateImageRequest imgReq = AnnotateImageRequest.newBuilder().addFeatures(feature).setImage(image).build();
		List<AnnotateImageRequest> imgReqs = new ArrayList<>();
		imgReqs.add(imgReq);

		ImageAnnotatorClient client = ImageAnnotatorClient.create();
		BatchAnnotateImagesResponse resp = client.batchAnnotateImages(imgReqs);
		List<AnnotateImageResponse> imgResps = resp.getResponsesList();
		AnnotateImageResponse imgResp = imgResps.get(0);
		client.close();

		if (imgResp.hasError()) {
			System.err.println("!Error! annotate image label: " + imgResp.getError().getMessage());
			return null;
		}

		return imgResp.getLabelAnnotationsList();
	}

}