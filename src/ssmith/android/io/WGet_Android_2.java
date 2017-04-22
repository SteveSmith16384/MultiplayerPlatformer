package ssmith.android.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.scs.worldcrafter.Statics;

public final class WGet_Android_2 { // todo - replace with one from SF

	private static final int MAX_TRIES = 6;
	private static final int TIMEOUT = 1000 * 60;
	
	private String text_response;
	private char[] data_response;
	private int response_code;
	private String redirect_to = "";
	private int tries_remaining = MAX_TRIES;
	private String post_data;


	public WGet_Android_2(String full_url, String _post_data) throws UnknownHostException, IOException {
		super();
		
		post_data = _post_data;

		while (tries_remaining > 0) { // In case of re-trying
			tries_remaining--;
			try {
				while (true) { // In case of redirects
					this.getOrPost(full_url);
					if (this.response_code == 302 && this.redirect_to.length() > 0) { 
						// Redirect ourselves
						full_url = redirect_to;
					} else if (response_code == 200 || response_code == -1 || response_code == 404 || response_code == 500 || response_code == 504) { // Android sometimes returns -1?
						tries_remaining = 0; // So we drop out
						break;
					} else {
						//throw new RuntimeException("Unwanted response code: " + this.response_code);
					}
				}
			} catch (IOException ex) { 
				// Loop round
			}
			// loop around again
		}
		if (this.response_code != 200 && this.response_code != -1) { // Sometimes android returns -1
			if (this.response_code > 0) {
				throw new IOException("Got response " + this.response_code + " from server.");
			} else { // No response
				throw new IOException("Could not connect to server.");
			}
		}
	}


	public int getResponseCode() {
		return this.response_code;
	}


	public String getResponse() {
		return text_response.toString();
	}


	private void getOrPost(String server) throws IOException {
		HttpPost http_get = new HttpPost(server.replaceAll("\\\\", "/"));

		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, TIMEOUT);
		http_get.setHeader("User-Agent", Statics.NAME + "_" + Statics.VERSION_NAME);
		HttpClient httpClient = new DefaultHttpClient(httpParameters);

		if (post_data != null) {
			if (post_data.length() > 0) {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("post", post_data));
				http_get.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}
		}

		HttpResponse httpResponse = httpClient.execute(http_get);
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

		response_code = httpResponse.getStatusLine().getStatusCode();

		// user reader to read & parse response 
		int content_length = -1;
		try {
			if (httpResponse.getFirstHeader("Content-Length") != null) {
				String s = httpResponse.getFirstHeader("Content-Length").getValue().toString();
				content_length = Integer.parseInt(s);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// Check if it's data rather than text
		String content_type = "";
		try {
			content_type = httpResponse.getFirstHeader("Content-Type").getValue();
		} catch (Exception ex) {
			content_type = "Unknown";
		}
		if (content_type.startsWith("text")) {
			String s = ""; 
			int bytes_remaining = content_length;
			StringBuffer str = new StringBuffer();
			while ((s = reader.readLine()) != null) { 
				str.append(s + "\n");
				bytes_remaining -= s.length();
				//displayProgress(bytes_remaining + " bytes remaining");  Always 0!
			}
			// remove last \n
			if (str.length() > 0) {
				str.delete(str.length()-1, str.length());
			}
			text_response = str.toString();
		} else {
			int bytes_remaining = content_length;
			int bytes_read = 0;
			data_response = new char[content_length]; // data_response.length
			while (bytes_remaining > 0) {
				try {
					int len = reader.read(data_response, bytes_read, bytes_remaining);
					if (len < 0) {
						break;
					}
					bytes_read += len;
					bytes_remaining -= len;
				} catch (java.lang.IndexOutOfBoundsException ex) {
					ex.printStackTrace();
				}
			}
		}

		reader.close();
	}

}

