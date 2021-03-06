package com.oopstudio.media.rtsp;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oopstudio.utils.Log;

public class RtspRequest {
	// Parse method & uri
	public static final Pattern regexMethod = Pattern.compile("(\\w+) (\\S+) RTSP", Pattern.CASE_INSENSITIVE);
	// Parse a request header
	public static final Pattern rexegHeader = Pattern.compile("(\\S+):(.+)",Pattern.CASE_INSENSITIVE);

	public String method;
	public String uri;
	public HashMap<String, String> headers = new HashMap<String, String>();

	/** Parse the method, uri & headers of a RTSP request */
	public static RtspRequest parseRequest(BufferedReader input)
			throws IOException, IllegalStateException, SocketException {
		RtspRequest request = new RtspRequest();
		String line;
		Matcher matcher;

		// Parsing request method & uri
		if ((line = input.readLine()) == null)
			throw new SocketException("Client disconnected");

		matcher = regexMethod.matcher(line);
		matcher.find();
		request.method = matcher.group(1);
		request.uri = matcher.group(2);

		// Parsing headers of the request
		while ((line = input.readLine()) != null && line.length() > 3) {
			
			matcher = rexegHeader.matcher(line);
			matcher.find();
			request.headers.put(matcher.group(1).toLowerCase(),
					matcher.group(2));
			
		}
		if (line == null)
			throw new SocketException("Client disconnected");

		// It's not an error, it's just easier to follow what's happening in
		// logcat with the request in red
		return request;
	}
}
