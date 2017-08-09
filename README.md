# chatapp
A simple chat app for Group Chat, using jQuery Ajax GET request and custom server in Java.
The application allows the chat in one single group. As it is based on AJAX, the server is called asynchronusly for below two purpose-
1. Request new message (1 request/sec)
2. Check user status (Online/Offline) (1 request/4 sec)

This two processes run seperately from two different WebWorkers.
The application is runnable particualarly in Chrome. (Not supported in IE, Not tested in Firefox and Safari)

TODO:
1. Handling of Message Sending- Due to use of editable **&lt;div&gt;**, spaces and tabs are requires special handling. Also, when in middle of a sentence and press _ENTER_ to send the message, it is appending an undesirable _newline_ character.
2. Implement Server Push to avoid continuous Ajax calls.
3. Remove custom server and bind it with Tomcat 8.
4. Database support for user and chat data management.
5. Overall UI improvement

