public class ReservationStation {
    
    int startCycle;
    int busy;
    InstructionType instructionType; //FIXME how to limit only the correct or just keep it to the calling?
    String vj;
    String vk;
    String qj;
    String qk;
    String A;
    int tag; //FIXME make this tag a string?

    public ReservationStation() {
        this.startCycle = 0;
        this.busy = 0;
        this.instructionType = null;
        this.vj = "";
        this.vk = "";
        this.qj = "";
        this.qk = "";
        this.A = "";
        this.tag = 0;
    }


}
