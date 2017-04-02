package simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/*
 *	Keenan Johnstone
 *	11119412
 *	kbj182
 *	cmpt340 Assignment 4
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

        /*
        MALE ACTOR
         */
        public void onReceive(Object message) throws Throwable {
            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            //Trigger our propose, redirect the proposal to the appropriate female
            else if (message instanceof malePropose) {
                if (!married) {
                    System.out.println("M" + number + ": Proposing to W" + ranks[proposalStep]);
                    femaleActorList.get(ranks[proposalStep]).tell(new femalePropose(number), getSelf());
                }
            }
            //Proposal reply from Female
            else if (message instanceof ProposalReply) {
                if(((ProposalReply) message).accepted)
                    System.out.println("M" + number + ": married to W" + ((ProposalReply) message).number);
                else
                    System.out.println("M" + number + ": proposal was rejected by W" + ((ProposalReply) message).number);
                married = ((ProposalReply) message).accepted;

                //If the man isnt married, try the next woman and keep cycling until married
                if(!married){
                    proposalStep++;
                    //start from the from of the list again
                    if(proposalStep >= ranks.length)
                        proposalStep = 0;
                    System.out.println("M" + number + ": There's plenty of fish in the sea, time to try again!");
                    getSelf().tell(new malePropose(), getSelf());
                }

            }
            //If we get dumped
            else if (message instanceof Dumped) {
                married = false;
                System.out.println("M" + number + ": DUMPED by W" + ((Dumped) message).number + ", time to get back in the dating scene!");
                getSelf().tell(new malePropose(), getSelf());
            }
            //If it's just an list, this is where we get the list of the opposite gender
            else if (message instanceof int[]) {
                ranks = (int[]) message;
                shuffleArray(ranks);
                System.out.println("M" + number + ": Rank array = " + Arrays.toString(ranks));
            }
            //Else unhandled
            else {
                unhandled(message);
            }
        }
    }

    /*
    FEMALE ACTOR
     */
    private static class ActorFemale extends UntypedActor {
        private int number;
        int currentMale;
        int[] ranks;
        boolean married = false;

        public void onReceive(Object message) throws Throwable {

            //Set our number from 1 to n
            if (message instanceof SetSelf){
                this.number = ((SetSelf) message).num;
            }
            if (message instanceof femalePropose) {
                int newMale = ((femalePropose) message).number;
                System.out.println("W" + number + ": being proposed to by M" + newMale + "(#" + arraysIndexOf(ranks, newMale) + ")");
                if (!married){
                    married = true;
                    currentMale = newMale;
                    System.out.println("W" + number + ": accepting proposal from M" + currentMale);
                    getSender().tell(new ProposalReply(true, number), getSelf());
                }
                else{

                    //if the new male is higher ranked (lower on the list) marry them and dump the old male
                    //System.out.println("W" + number + ": COMPARING MEN, CURRENT: M" + currentMale + "(#" + arraysIndexOf(ranks, currentMale) + ") VS M" + ((femalePropose) message).number + "(#" + arraysIndexOf(ranks, ((femalePropose) message).number) + ")");
                    if(arraysIndexOf(ranks, currentMale) > arraysIndexOf(ranks, newMale)){
                        System.out.println("W" + number + ": dumping M" + currentMale + " for M" + newMale);
                        //dump the old male
                        maleActorList.get(currentMale).tell(new Dumped(number), getSelf());
                        //Marry new male
                        currentMale = newMale;
                        getSender().tell(new ProposalReply(true, number), getSelf());
                    }
                    else {
                        System.out.println("W" + number + ": rejecting proposal by M" + newMale);
                        getSender().tell(new ProposalReply(false, number), getSelf());
                    }
                }
            }
            //If it's just an list, this is where we get the list of the opposite gender
            else if (message instanceof int[]) {
                ranks = (int[]) message;
                shuffleArray(ranks);
                System.out.println("W" + number + ": Rank array = " + Arrays.toString(ranks));
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
        int number;

        public ProposalReply(boolean b, int i) {
            this.accepted = b;
            this.number = i;
        }
    }

    private static class malePropose implements  Serializable {

    }

    private static class Dumped implements  Serializable {
        public int number;

        public Dumped(int message) {
            this.number = message;
        }
    }

    public static void main(String[] args) throws InterruptedException {
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

        //Wait for the actors to finish creating

        TimeUnit.SECONDS.sleep(1);
        System.out.println("================");
        for(int i = 0; i < maleActorList.size(); i++)
        {
            inbox.send(maleActorList.get(i), new malePropose());
            TimeUnit.MICROSECONDS.sleep(100);
        }


        TimeUnit.SECONDS.sleep(2*num);

        // Shut down the system gracefully if we are taking too long (we can get stuck in a loop
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

    public static int arraysIndexOf(int[] array, int x){
        for(int i = 0; i < array.length; i++)
        {
            if(array[i] == x)
                return i;
        }
        return -1;
    }
}
