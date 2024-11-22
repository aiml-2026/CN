import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class SimpleHttpServer {
    private static final int PORT = 6666;  // Change this if needed
    private static final String UPLOAD_DIR = "uploads/";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started at http://localhost:" + PORT);

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        while (true) {
            Socket clientSocket = serverSocket.accept();
            handleClient(clientSocket);
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String line = in.readLine();
            if (line == null) return;

            String[] request = line.split(" ");
            String method = request[0];
            String path = request[1];

            if (method.equals("GET")) {
                handleGetRequest(out, path);
            } else if (method.equals("POST")) {
                handlePostRequest(in, out, path, clientSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleGetRequest(OutputStream out, String path) throws IOException {
        if (path.equals("/")) {
            sendResponse(out, "200 OK", "text/html", getHtmlForm());
        } else if (path.startsWith("/uploads/")) {
            File file = new File(UPLOAD_DIR + path.substring(9));
            if (file.exists()) {
                sendFileResponse(out, file);
            } else {
                sendResponse(out, "404 Not Found", "text/html", "<h1>File not found</h1>");
            }
        } else {
            sendResponse(out, "404 Not Found", "text/html", "<h1>Page not found</h1>");
        }
    }

    private static void handlePostRequest(BufferedReader in, OutputStream out, String path, Socket clientSocket) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while (!(line = in.readLine()).isEmpty()) {
            String[] header = line.split(": ");
            if (header.length == 2) {
                headers.put(header[0], header[1]);
            }
        }

        if (!headers.containsKey("Content-Type") || !headers.get("Content-Type").startsWith("multipart/form-data")) {
            sendResponse(out, "400 Bad Request", "text/html", "<h1>Invalid request</h1>");
            return;
        }

        String boundary = "--" + headers.get("Content-Type").split("boundary=")[1];
        DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());

        skipHeaders(dataIn, boundary);

        // Read the file content
        String fileName = "uploaded_image.jpg";  // You can modify this to generate a unique file name
        FileOutputStream fileOut = new FileOutputStream(UPLOAD_DIR + fileName);

        byte[] buffer = new byte[1024];
        int bytesRead;
        boolean fileUploaded = false;

        while ((bytesRead = dataIn.read(buffer)) != -1) {
            // Check if we have reached the boundary (end of the file data)
            String data = new String(buffer, 0, bytesRead);
            if (data.contains(boundary)) {
                fileUploaded = true; // Mark as successfully uploaded
                break; // Stop reading file content
            }

            // Write the file content to the output file
            fileOut.write(buffer, 0, bytesRead);
        }
        fileOut.close();

        // Send success or failure response based on whether the file was uploaded
        if (fileUploaded) {
            sendResponse(out, "200 OK", "text/html", "<h1>File uploaded successfully</h1><a href=\"/uploads/" + fileName + "\">Download Image</a>");
        } else {
            sendResponse(out, "400 Bad Request", "text/html", "<h1>File upload failed</h1>");
        }
    }

    private static void skipHeaders(DataInputStream in, String boundary) throws IOException {
        String line;
        // Skip until we find the first boundary line
        while (!(line = in.readLine()).contains(boundary)) {
            // Skipping headers
        }

        // Skip additional headers until we get to the actual file content
        while (!(line = in.readLine()).isEmpty()) {
            // Skipping additional part headers
        }
    }

    private static void sendResponse(OutputStream out, String status, String contentType, String content) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write("HTTP/1.1 " + status + "\r\n");
        writer.write("Content-Type: " + contentType + "\r\n");
        writer.write("Content-Length: " + content.length() + "\r\n");
        writer.write("\r\n");
        writer.write(content);
        writer.flush();
    }

    private static void sendFileResponse(OutputStream out, File file) throws IOException {
        byte[] fileData = Files.readAllBytes(file.toPath());

        OutputStreamWriter writer = new OutputStreamWriter(out);
        writer.write("HTTP/1.1 200 OK\r\n");
        writer.write("Content-Type: image/jpeg\r\n");
        writer.write("Content-Length: " + fileData.length + "\r\n");
        writer.write("Content-Disposition: attachment; filename=\"" + file.getName() + "\"\r\n");
        writer.write("\r\n");
        writer.flush();

        out.write(fileData);
        out.flush();
    }

    // HTML form for image upload
    private static String getHtmlForm() {
        return """
<html>
<head>
    <title>Image Upload</title>
    <style>
        .container {
            width: 50%;
            margin: 0 auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 10px;
            box-shadow: 0px 0px 10px rgba(0, 0, 0, 0.1);
            background-color: #f9f9f9;
        }
        h1 {
            color: #333;
        }
        input[type="file"] {
            margin-bottom: 20px;
        }
        input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
    </style>
</head>
<body>
    <div class="container">
        <center>
            <h1>Upload Image</h1>
            <form method="POST" enctype="multipart/form-data" action="/">
                <input type="file" name="file" accept="image/*"><br><br>
                <input type="submit" value="Upload Image">
            </form>
        </center>
    </div>
</body>
</html>
               """;
    }
}
