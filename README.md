SocketTester
============

My brother's been having some issues at work with his app &amp; SQL server, this should help test if things are working right.

How to use
============

Take the JAR in the current directory of this repository and save it to a client machine and a server machine.

Then, for the client, run SocketTester with these arguments:

``java -jar SocketTester.jar client IP_ADDRESS_OF_SERVER PORT SECRET_STRING``

Example: ``java -jar SocketTester.jar client 127.0.0.1 9001 secretmessage``

After, for the server, run SocketTester with these arguments:

``java -jar SocketTester.jar server BIND_IP BIND_PORT SECRET_STRING``

Example: ``java -jar SocketTester.jar server 127.0.0.1 9001 secretmessage``

To add profiling, or determine delays between packets: add ``debug`` to the program arguments.

Specifications
==============

This is **TCP only** as UDP wouldn't be used normally.  The packets are sent only once every second, and this delay cannot be changed with program arguments and requires recompiling the source.  The good news is it only utilizes the Java SE APIs so this shouldn't be a problem.

How to determine if the SocketTester caught a problem
=====================================================

You will likely receive this in the console:
```java
java.net.SocketException: Connection reset
	at java.net.SocketInputStream.read(Unknown Source)
	at java.net.SocketInputStream.read(Unknown Source)
	at java.net.SocketInputStream.read(Unknown Source)
	at java.io.DataInputStream.readByte(Unknown Source)
	at se.jkrau.sockettester.packets.Packets.readStream(Packets.java:20)
	at se.jkrau.sockettester.comm.Client.run(Client.java:50)
[09/07/2013 02:18:12 PM CDT] Something went wrong with the server.
```

This was done by me stopping the server, however you might receive messages like ``Connection timed out`` or something similar of sorts if something really went wrong.

What's the point of this?
==========================

Scroll to the top.
