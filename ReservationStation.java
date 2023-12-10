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
    int duration;
    int executeEndCycle;
    Instruction instruction;

    public ReservationStation(String tagCharacter, int duration) {
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
        this.duration = duration;
        this.executeEndCycle=-1;
        
    }
    public void print()
    {
        System.out.print("Tag: " + tag +
                         "\tStart Cycle: " + startCycle +
                         "\t Execute End Cycle: " + executeEndCycle +
                         "\tBusy: " + busy +
                         "\tInstruction Type: " + (instructionType != null ? instructionType.toString() : "None") +
                         "\tVj: " + vj +
                         "\tVk: " + vk +
                         "\tQj: " + qj +
                         "\tQk: " + qk +
                         "\tA: " + A +
                         "\tDuration left: " + duration +
                         "\tReady To Write: " + readyToWrite);
    
    }

}
