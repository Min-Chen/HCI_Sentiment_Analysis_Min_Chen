package app.http;

import app.concurrent.WorkQueue;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HTTPServer {
	protected int PORT;
	protected Map<String, Class> handlersHashMap;

	public HTTPServer(int port) {
		this.handlersHashMap = new HashMap<>();
		this.PORT = port;
	}

	public HTTPServer(int port, String configPath) {
		this.handlersHashMap = new HashMap<>();
		this.PORT = port;
	}

	public void setHandlersHashMap(Map handlersHashMap) {
		this.handlersHashMap = handlersHashMap;
	}

	public void start() {
		WorkQueue queue = new WorkQueue();

		try {
			ServerSocket server = new ServerSocket(PORT);
			System.out.println("Server starts at port: " + PORT);
			while (true) {
				Socket socket = server.accept();
//				System.out.println("accept socket");
				queue.execute(getTask(socket));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Return a runnable task - Use PrintWriter to write a String type of
	 * response back to client.
	 * 
	 * @param socket
	 * @return
	 */
	public Runnable getTask(Socket socket) {
		return new Runnable() {
			@Override
			public void run() {
				try (PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
					HTTPResponse response = assignRequest(socket);
					String responseString = response.getReponseHeader() + "\n\n" + response.getResponseBody();
					writer.write(responseString);
					writer.flush();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		};
	}

	/**
	 * Assign request to specific handler, and return a String type of response
	 * back to client.
	 * 
	 * @param socket
	 * @return
	 */
	public HTTPResponse assignRequest(Socket socket) {
		HTTPResponse response = new HTTPResponse();
		HTTPRequest request = new HTTPRequest(socket);
		if (request == null || !request.getMethod().equals("GET")) {
			response.setResponseHeader(ResponseHeaders.METHOD_NOT_ALLOWED_HEADER);
		} else {
			String potentialHandler = request.getHandler();
			if (this.handlersHashMap.containsKey(potentialHandler)) {
				try {
					Class requestHandlerClass = this.handlersHashMap.get(potentialHandler);
					Object requestHandlerInstance = requestHandlerClass.newInstance();
					response = ((RequestHandler) requestHandlerInstance).handleRequest(request);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				response.setResponseHeader(ResponseHeaders.NOT_FOUND_HEADER);
			}
		}
		return response;
	}
}