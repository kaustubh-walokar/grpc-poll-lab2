grpc Poll  server
==============================================
SRC at : https://github.com/kaustubh-walokar/grpc-poll-lab2/tree/master/examples/src/main/java/io/grpc/examples/helloworld

In order to run the poll server simply execute one of the gradle tasks `pollServer`, or `pollClient`.


Assuming you are in the grpc-java root folder you would first start the route guide server
by running

```
$ ./gradlew :grpc-examples:pollServer
```

and in a different terminal window then run the client by typing

```
$ ./gradlew :grpc-examples:pollClient
```

That's it!
