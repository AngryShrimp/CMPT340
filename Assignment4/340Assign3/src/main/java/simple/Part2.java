package simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import akka.util.Timeout;
import javafx.concurrent.Worker;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.Collections.shuffle;

/**
 * Created by Keenan on 2017-03-22.
 */
public class Part2 {

    //create our list of male and female actors
    static List<ActorRef> maleActorList = new ArrayList<ActorRef>();
    static List<ActorRef> femaleActorList = new ArrayList<ActorRef>();

    //Define our Actors
    private static class ActorMale extends UntypedActor {
        int number;
        int[] ranks;
        boolean married = false;

        public void onReceive(Object message) throws Throwable {
            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            //Proposal reply from Female
            else if (message instanceof ProposalReply) {
                married = ((ProposalReply) message).accepted;
            }
            //If we get dumped
            else if (message instanceof Dumped) {
                married = false;
            }
            //If it's just an list, this is where we get the list of the opposite gender
            else if (message instanceof int[]) {
                ranks = (int[]) message;
                shuffleArray(ranks);
                System.out.println("M" + number + Arrays.toString(ranks));
            }
            //Else unhandled
            else {
                unhandled(message);
            }
        }
    }

    private static class ActorFemale extends UntypedActor {
        private int number;
        int[] ranks;
        boolean married = false;

        public void onReceive(Object message) throws Throwable {

            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            //If it's just an list, this is where we get the list of the opposite gender
            else if (message instanceof int[]) {
                ranks = (int[]) message;
                shuffleArray(ranks);
                System.out.println("W" + number + Arrays.toString(ranks));
            }
        }
    }

    //Define our messages that we can send
    private static class SetSelf implements Serializable {
        public int num;

        public SetSelf(int n){
            this.num = n;
        }
    }

    private static class Propose implements Serializable {
        public String message;

        public Propose(String message) {
            this.message = message;
        }
    }

    private static class ProposalReply implements Serializable{
        boolean accepted;

        public ProposalReply(boolean b) {
            this.accepted = b;
        }
    }

    private static class Dumped implements  Serializable {

    }

    public static void main(String[] args) {
        //Get input
        System.out.print("Please enter the number of Men and Women that the program will run for: ");
        Scanner input = new Scanner(System.in);
        Long num = new Long(input.nextInt());
        input.close();


        // Create an actor system
        final ActorSystem actorSystem = ActorSystem.create("actor-system");

        // Create an inbox
        final Inbox inbox = Inbox.create(actorSystem);

        // Create our list of female and male actors
        for(int i = 0; i < num; i++)
        {
            ActorRef workerMale = actorSystem.actorOf(Props.create(ActorMale.class), "male" + i);
            workerMale.tell(new SetSelf(i), ActorRef.noSender());
            maleActorList.add(workerMale);

            ActorRef workerFemale = actorSystem.actorOf(Props.create(ActorFemale.class), "female" + i);
            workerFemale.tell(new SetSelf(i), ActorRef.noSender());
            femaleActorList.add(workerFemale);
        }

        //Send the actors shuffled lists of the actors of the opposite genders
        //Temp lists are needed for easy shuffling
        int[] shuffledFemaleList = new int[num.intValue()];
        int[] shuffledMaleList =  new int[num.intValue()];

        for(int i = 0; i < num; i++)
        {
            shuffledFemaleList[i] = i;
            shuffledMaleList[i] = i;
        }

        for(int i = 0; i < num; i++){
            inbox.send(maleActorList.get(i), shuffledFemaleList);
            inbox.send(femaleActorList.get(i), shuffledMaleList);
        }

//        System.out.print(maleActorList);
//        System.out.print(femaleActorList);

        // Shut down the system gracefully
        actorSystem.terminate();
    }

    public static void shuffleArray(int[] a) {
        int n = a.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(a, i, change);
        }
    }

    private static void swap(int[] a, int i, int change) {
        int helper = a[i];
        a[i] = a[change];
        a[change] = helper;
    }
}
