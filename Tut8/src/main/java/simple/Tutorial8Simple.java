/* File Name: Tutorial8Solution.java
   Author: David Kreiser
   Adapted from basic Akka tutorial at http://akka.io/ 
   Class: CMPT 340 Tutorial 8 
   Contents: Basic file structure for Tutorial 8 example
*/

package simple;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;
import akka.actor.Inbox;

/* Imports go here */



/* End of imports  */


// One class contains entire program - sufficient for our purposes. Could be split into different files. 
public class Tutorial8Simple {

    // Define static classes for any actors that will be needed
    private static class Greeter extends UntypedActor{
        String greeting = "";

        @Override
        public void onReceive(Object message) throws Throwable {
            if(message instanceof SetGreeting){
                this.greeting = "Hello " + ((SetGreeting)message).who + "!";
            }
            else if(message instanceof Greet) {
                getSender().tell(new Greeting(this.greeting), getSelf());
            }
            else {
                unhandled(message);
            }

        }

    }

    // Define static classes for each type of message we are going to be passing
    private static class Greet implements Serializable {

    }

    private static class SetGreeting implements Serializable {
        public String who;

        public SetGreeting(String w){
            this.who = w;
        }
    }

    private static class Greeting implements Serializable {
        public String message;

        public Greeting(String m){
            this.message = m;
        }
    }

    public static void main(String[] args) {

        // Create an actor system
        final ActorSystem actorSystem = ActorSystem.create("actor-system");

        // Create a greeter actor
        final ActorRef greeter = actorSystem.actorOf(Props.create(Greeter.class), "gretter");

        // Create an inbox (actor in a box), allows our main function to send
        // and receive messages without being an actor itself
        final Inbox inbox = Inbox.create(actorSystem);

        // Set the actor's greeting message
        greeter.tell(new SetGreeting("Keenan"), ActorRef.noSender());

        // Ask the greeter for a greeting message
        inbox.send(greeter, new Greet());

        // Wait up to 5 seconds for a reply from the greeter
        Greeting reply = null;
        try {
            reply = (Greeting) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        }
        catch (TimeoutException e){
            System.out.println("Got a Timeout waiting for reply.");
        }

        // Print the reply received
        System.out.println("Greeting Recieved: " + reply.message);

        // Shut down the system
        actorSystem.terminate();

    }

}