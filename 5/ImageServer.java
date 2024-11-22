import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class ImageServer {
    private static final int PORT = 7777;
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
        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 " + status);
        writer.println("Content-Type: " + contentType);
        writer.println("Content-Length: " + content.length());
        writer.println();
        writer.println(content);
        writer.flush();
    }

    private static void sendFileResponse(OutputStream out, File file) throws IOException {
        byte[] fileData = Files.readAllBytes(file.toPath());

        PrintWriter writer = new PrintWriter(out);
        writer.println("HTTP/1.1 200 OK");
        writer.println("Content-Type: image/jpeg");
        writer.println("Content-Length: " + fileData.length);
        writer.println("Content-Disposition: attachment; filename=\"" + file.getName() + "\"");
        writer.println();
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
        /* General reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body and background styling */
        body {
            font-family: 'Montserrat', sans-serif;
            background: linear-gradient(135deg, #ff7e5f, #feb47b);
            height: 100vh;
            overflow: hidden;
            display: flex;
            justify-content: center;
            align-items: center;
            position: relative;
        }

        /* Bubble animation */
        .bubble {
            position: absolute;
            bottom: -100px;
            width: 40px;
            height: 40px;
            background-color: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            animation: rise 10s infinite ease-in;
        }

        @keyframes rise {
            from {
                transform: translateY(0) scale(1);
                opacity: 1;
            }
            to {
                transform: translateY(-1200px) scale(1.5);
                opacity: 0;
            }
        }

        /* Varying size and speed of bubbles */
        .bubble:nth-child(1) { width: 80px; height: 80px; left: 10%; animation-duration: 9s; animation-delay: 0s; }
        .bubble:nth-child(2) { width: 50px; height: 50px; left: 30%; animation-duration: 7s; animation-delay: 2s; }
        .bubble:nth-child(3) { width: 100px; height: 100px; left: 50%; animation-duration: 11s; animation-delay: 4s; }
        .bubble:nth-child(4) { width: 60px; height: 60px; left: 70%; animation-duration: 8s; animation-delay: 1s; }
        .bubble:nth-child(5) { width: 90px; height: 90px; left: 90%; animation-duration: 12s; animation-delay: 3s; }

        /* Container styling */
        .container {
            background: #fff;
            padding: 50px;
            border-radius: 20px;
            box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
            width: 40%;
            text-align: center;
            z-index: 1;
            animation: fadeIn 1s ease-in-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(50px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* Heading styling */
        h1 {
            color: #333;
            font-size: 2.5rem;
            margin-bottom: 20px;
            letter-spacing: 1px;
        }

        /* Custom file input */
        input[type="file"] {
            display: none;
        }

        /* Label for custom file input */
        label {
            background: linear-gradient(45deg, #43cea2, #185a9d);
            color: white;
            padding: 12px 40px;
            border-radius: 30px;
            cursor: pointer;
            font-size: 1.1rem;
            transition: background-color 0.3s ease;
        }

        label:hover {
            background-color: #148f77;
        }

        /* Custom file name */
        .file-name {
            margin-top: 15px;
            font-size: 1rem;
            color: #777;
        }

        /* Upload button */
        input[type="submit"] {
            background: linear-gradient(45deg, #ff7e5f, #feb47b);
            color: white;
            padding: 12px 40px;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            font-size: 1.1rem;
            margin-top: 25px;
            transition: background-color 0.3s ease;
        }

        input[type="submit"]:hover {
            background-color: #ff7043;
        }

        /* Subtle bounce effect on submit button hover */
        input[type="submit"]:hover {
            transform: translateY(-3px);
        }
    </style>
</head>
<body>
    <!-- Bubble Animations -->
    <div class="bubble"></div>
    <div class="bubble"></div>
    <div class="bubble"></div>
    <div class="bubble"></div>
    <div class="bubble"></div>

    <div class="container">
        <h1>Upload Image</h1>
        <form method="POST" enctype="multipart/form-data" action="/">
            <input type="file" id="fileInput" name="file" accept="image/*">
            <label for="fileInput">Choose Image</label><br>
            <span class="file-name" id="fileName">No file selected</span><br><br>
            <input type="submit" value="Upload Image">
        </form>
    </div>

    <script>
        // Display selected file name
        const fileInput = document.getElementById('fileInput');
        const fileName = document.getElementById('fileName');

        fileInput.addEventListener('change', function() {
            if (fileInput.files.length > 0) {
                fileName.textContent = fileInput.files[0].name;
            } else {
                fileName.textContent = 'No file selected';
            }
        });
    </script>
</body>
</html>


               """;
    }
}