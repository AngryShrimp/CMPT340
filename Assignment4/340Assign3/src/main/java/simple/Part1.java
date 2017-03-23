package simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Keenan on 2017-03-20.
 */
public class Part1 {
    //Define our new Akka Actors
    private static class Worker extends UntypedActor {
        public void onReceive(Object message) {
            if (message instanceof Long) {
                //Accept a long
                Long n = (Long) message;
                Long returnVal;

                if (n == 0) {
                    //return 0
                    returnVal = 0L;
                } else if (n == 1) {
                    //return 1
                    returnVal = 1L;
                } else {
                    //return fib(n-1) + fib(n-2)
                    //create TWO children actors
                    final ActorRef childWorker1 = getContext().actorOf(Props.create(Worker.class));
                    final ActorRef childWorker2 = getContext().actorOf(Props.create(Worker.class));

                    //Create a timeout
                    Timeout t = new Timeout(Duration.create(20, TimeUnit.SECONDS));

                    //Ask the children with the fib numbers
                    Future<Object> future1 = Patterns.ask(childWorker1, new Long(n - 1), t);
                    Future<Object> future2 = Patterns.ask(childWorker2, new Long(n - 2), t);

                    //Get results
                    Long r1 = null;
                    Long r2 = null;

                    try {
                        r1 = (Long) Await.result(future1, t.duration());
                    } catch (Exception e) {
                        System.out.println("Got a timeout after waiting 20 seconds for the value of " + (n - 1)
                                + "! from a child worker");
                        System.exit(1);
                    }
                    try {
                        r2 = (Long) Await.result(future2, t.duration());
                    } catch (Exception e) {
                        System.out.println("Got a timeout after waiting 20 seconds for the value of " + (n - 2)
                                + "! from a child worker");
                        System.exit(1);
                    }

                    //calculate the new return value
                    returnVal = new Long(r1 + r2);

                }

                //Send back the message
                getSender().tell(returnVal, getSelf());
            } else {
                unhandled(message);
            }
        }
    }

    public static void main(String[] args) {

        //Get input
        System.out.print("Please enter value of x for which to calculate x fibonacci values: ");
        Scanner input = new Scanner(System.in);
        Long num = new Long(input.nextInt());
        input.close();


        // Create an actor system
        final ActorSystem actorSystem = ActorSystem.create("actor-system");

        // Create a worker actor
        final ActorRef worker = actorSystem.actorOf(Props.create(Worker.class), "worker");

        // Create an inbox
        final Inbox inbox = Inbox.create(actorSystem);

        // Tell the worker to calculate the factorial of num
        inbox.send(worker, num);

        // Wait up to 20 seconds for a reply from the worker
        Long reply = null;
        try {
            reply = (Long) inbox.receive(Duration.create(20, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("Got a timeout after waiting 20 seconds for the fibonacci of " + num + ".");
            System.exit(1);
        }

        // Print the reply received
        System.out.println(num + "'s fibonacci is " + reply);

        // Shut down the system gracefully
        actorSystem.terminate();
    }
}
