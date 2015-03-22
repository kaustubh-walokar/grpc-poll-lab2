package edu.sjsu.cmpe273.lab2;

import io.grpc.ServerImpl;
import edu.sjsu.cmpe273.lab2.PollRequest;
import edu.sjsu.cmpe273.lab2.PollResponse;
import edu.sjsu.cmpe273.lab2.PollServiceGrpc;
import io.grpc.stub.StreamObserver;
import io.grpc.transport.netty.NettyServerBuilder;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * Server that manages startup/shutdown of a {@code Greeter} server.
 */
public class PollServer {
    private static final Logger logger = Logger.getLogger(PollServer.class.getName());

    /* The port on which the server should run */
    private int port = 8980;
    private ServerImpl server;

    private void start() throws Exception {

        server = NettyServerBuilder.forPort(port)
                .addService(PollServiceGrpc.bindService(new myPollService())).build().start();

        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                PollServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws Exception {
        final PollServer server = new PollServer();
        server.start();
    }

    private static class myPollService implements PollServiceGrpc.PollService {
        final AtomicInteger counter = new AtomicInteger(111111);
        @Override
        public void createPoll(PollRequest request, StreamObserver<PollResponse> responseObserver) {

            PollResponse temp = PollResponse.newBuilder()
                    .setId(request.getModeratorId()+"XYZ")
                    .build();

            responseObserver.onValue(temp);
            logger.info("-------------------------------------------------------");
            logger.info("-----------Server Here---------------------------------");
            logger.info("-------------------------------------------------------");
            logger.info("The moderator id recieved = "+request.getModeratorId().toString());
            logger.info("-------------------------------------------------------");
            responseObserver.onCompleted();

        }
    }
}
