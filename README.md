# chatapp
A simple chat app for Group Chat, using jQuery Ajax GET request and custom server in Java.
The application allows the chat in one single group. As it is based on AJAX, the server is called asynchronusly for below two purpose-
1. Request new message (1 request/sec)
2. Check user status (Online/Offline) (1 request/4 sec)

This two processes run seperately from two different WebWorkers.
The application is runnable particualarly in Chrome. (Not supported in IE, Not tested in Firefox and Safari)

Branch Purpose:
Integration of Tomcat Server and replace the current html/logic with JSP & Servlet.

