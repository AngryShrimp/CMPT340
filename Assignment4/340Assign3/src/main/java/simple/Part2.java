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
        int proposalStep = 0;
        int[] ranks;
        boolean married = false;

        public void onReceive(Object message) throws Throwable {
            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            //Trigger our propose, redirect the proposal to the appropriate female
            else if (message instanceof malePropose) {
                if (!married) {
                    Timeout t = new Timeout(Duration.create(20, TimeUnit.SECONDS));

                    Future<Object> future = Patterns.ask(femaleActorList.get(ranks[proposalStep]), new femalePropose(number), t);

                    try {
                        married = (Boolean) Await.result(future, t.duration());
                    } catch (Exception e) {
                        System.out.println("Got a timeout after waiting 20s in Male" + number);
                        System.exit(1);
                    }
                    if(!married)
                        proposalStep++;
                    //if we arent married restart
                    if(proposalStep >= femaleActorList.toArray().length)
                        proposalStep = 0;
                    getSender().tell(married, getSelf());
                }
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
        int currentMale = -1;
        int[] ranks;
        boolean married = false;

        public void onReceive(Object message) throws Throwable {

            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            if (message instanceof femalePropose) {
                if (!married){
                    married = true;
                    currentMale = ((femalePropose) message).number;
                    getSender().tell(new ProposalReply(true), getSelf());
                }
                else{
                    //if the new male is higher ranked (lower on the list) marry them and dump the old male
                    if(Arrays.asList(ranks).indexOf(((femalePropose) message).number) < Arrays.asList(ranks).indexOf(currentMale)){
                        //dump the old male
                        maleActorList.get(currentMale).tell(new Dumped(), getSelf());
                        //Marry new male
                        currentMale = ((femalePropose) message).number;
                        getSender().tell(new ProposalReply(true), getSelf());
                    }
                }
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

    private static class femalePropose implements Serializable {
        public int number;

        public femalePropose(int message) {
            this.number = message;
        }
    }

    private static class ProposalReply implements Serializable{
        boolean accepted;

        public ProposalReply(boolean b) {
            this.accepted = b;
        }
    }

    private static class malePropose implements  Serializable {

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

        for(int i = 0; i < num; i++) {
            shuffledFemaleList[i] = i;
            shuffledMaleList[i] = i;
        }

        for(int i = 0; i < num; i++){
            inbox.send(maleActorList.get(i), shuffledFemaleList);
            inbox.send(femaleActorList.get(i), shuffledMaleList);
        }

        inbox.send(maleActorList.get(0), new malePropose());

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
