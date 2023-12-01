public class Instruction {
    
    InstructionType instructionType;
    String j;
    String k;
    boolean issued;
    int issueCycle;
    //TODO initialise the cycle length depending on the instruction type
    boolean executed;
    int duration;
    int position; //the position in the buffer
    

//     insucion class --> instcution type , and has its own counter according to the stages of the intrsuctin type
// taken to load (starts decrementing once issued and exec started) (boolean exec staryted)

    public Instruction(String [] words) {
        this.instructionType = InstructionType.valueOf(words[0].toUpperCase());
        this.j = setJ(words); //based on instruction type
        this.k = setK(words); //based on instruction type
        this.issued = false;
        this.issueCycle = 0;
        this.executed = false;
        this.duration = setDuration(words); //based on instriuctip type
        this.position = -1;
    }

    //TODO switch cases on these to set the values
    public String setJ(String [] words) {
            return "";

    }

    public String setK(String [] words)
    {
            return "";
    }

    public int setDuration(String [] words)
    {
        return -1;
    }


}
