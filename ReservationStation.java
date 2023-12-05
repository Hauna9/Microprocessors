public class ReservationStation {
    
    int startCycle;
    int busy;
    InstructionType instructionType; //FIXME how to limit only the correct or just keep it to the calling?
    String vj;
    String vk;
    String qj;
    String qk;
    String A;
    String tag; //FIXME make this tag a string?
    boolean readyToWrite;

    public ReservationStation(String tagCharacter) {
        this.startCycle = -1; //FIXME is this needed?
        this.busy = 0;
        this.instructionType = null;
        this.vj = "";
        this.vk = "";
        this.qj = "";
        this.qk = "";
        this.A = "";
        this.tag = tagCharacter;
        this.readyToWrite = false;
    }


}
