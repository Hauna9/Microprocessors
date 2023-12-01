public class Instruction {
    
    InstructionType instructionType;
    String destination;
    String j; //mul add etc
    String k; // mull add etc
    int jump; //for load/store
    int regToJump; // for load/store
    int immediate; //for addi etc
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
        setAttributes(words);
        this.issued = false;
        this.issueCycle = 0;
        this.executed = false;
        this.duration = setDuration(words); //based on instriuctip type
        this.position = -1;
        this.destination= setDestination(words);
    }

    //TODO switch cases on these to set the values
    public String setDestination(String [] words) {
        //FIXME branch instruction not using tag right?
            switch(words[0])
            {
                // case "LD": case:"SD":
                default:
                    return words[1].substring(1); // extract the register number
            }
    }

    public void setAttributes(String [] words)
    {
            switch(words[0])
            {
                case "LD": case "SD":
                
                    //this.j=words[1].substring(1); // extract the register number
                     break;
                case "ADD": case "SUB": case "MUL": case "DIV":
                    this.j=words[2].substring(1); // extract the register number
                    this.k=words[3].substring(1); // extract the register number
                     break;
                case "ADDI": case "SUBI": case "MULI": case "DIVI":
                    this.j=words[2].substring(1); // extract the register number
                    this.k=words[3].substring(1); // extract the register number
                    break;
                case "BNEZ":
                    // this.j=words[1].substring(1); // extract the register number
                    // this.jump=Integer.parseInt(words[2]);
                    // this.regToJump=Integer.parseInt(words[3].substring(1));
                    // break;
            }
    }

    public int setDuration(String [] words)
    {
        return -1;
    }


}
