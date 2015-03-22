package io.grpc.examples.helloworld;

import io.grpc.ChannelImpl;
import io.grpc.examples.routeguide.PollRequest;
import io.grpc.examples.routeguide.PollResponse;
import io.grpc.examples.routeguide.PollServiceGrpc;
import io.grpc.transport.netty.NegotiationType;
import io.grpc.transport.netty.NettyChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link PollServer}.
 */
public class PollClient {
    private static final Logger logger = Logger.getLogger(PollClient.class.getName());

    private final ChannelImpl channel;
    private final PollServiceGrpc.PollServiceBlockingStub bs;

    public PollClient(String host, int port) {
        channel =
                NettyChannelBuilder.forAddress(host, port).negotiationType(NegotiationType.PLAINTEXT)
                        .build();

        bs = PollServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTerminated(5, TimeUnit.SECONDS);
    }

    public void createPoll(String moderatorId, String question, String startedAt, String expiredAt, String[] choice) {
        try {
            logger.info("sending message");


            PollRequest a = PollRequest.newBuilder()
                    .setExpiredAt(expiredAt)
                    .setQuestion(question)
                    .setStartedAt(startedAt)
                    .addChoice(choice[0])
                    .addChoice(choice[1])
                    .setModeratorId(moderatorId)
                    .build();

            PollResponse poll_response = bs.createPoll(a);
            logger.info("Created a new poll with id = " + poll_response.getId());
            logger.info("sent");
        } catch (RuntimeException e) {
            logger.log(Level.WARNING, "RPC failed", e);
            return;
        }


    }

    public static void main(String[] args) throws Exception {
        PollClient client = new PollClient("localhost", 8980);
        try {
      /* Access a service running on the local machine on port 50051 */
            String moderatorId = "12";
            String question = "What type of smartphone do you have?";
            String startedAt = "2015-03-18T13:00:00.000Z";
            String expiredAt = "2015-03-19T13:00:00.000Z";
            String[] choice = new String[]{"Android", "iPhone"};

            client.createPoll(moderatorId, question, startedAt, expiredAt, choice);

        } finally {
            client.shutdown();
        }
    }
}
