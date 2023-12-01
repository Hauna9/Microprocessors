/*

as about if cant issue, do we stall all? --> we do not issue it but we do not stall all
Skeleton:


checks for cleanup 



issue

    checks:  (instuction buffer) (after cleaning up buffers etc)
     if instruction is valid
        if buffer is full (is there strucutal hazard) 
            is there empty reservation stayion  (according to type)
            
            every instruction has boolean issued, if it is 1, we have boolean execstart, if it is false we set it to true, and set clock cycle
    attrbutes of exec beginning to the current clock cycle
    if issued and if exec staart, we decrement the remaining clocks for the duration
    if after decrementing it is 0, we remove it from thw station and sert the end exec cycle, and set exec done to true
    anything that has exec done to true we do the b: if it is 0, we check what else is 0 or add it ao anrray and handle which one to write back
       
        if instruction is ready to be issued  (remove from instruction queue ig)
       when issuing:
       initlaise issue number with thwe clock cycle
         set the reservation station to busy, update address //FIXME : check if we need to update address 
         register file initliase Q(i) to tag if the reservarion station
         
       TODO go through vod 12 and 13 for load and store buffers, and the loop examples

       wed: write it what i wrote here ^^
       2)main methods 
       3) prints
       4) write some test cases (basic)


    
 
 */
//queue instructions; //FIXME make it queue?

import java.util.*;
import java.io.*;


public class Microprocessor {

    static Queue<Instruction> instructions;
     static Queue<Instruction> currentInstructions; //got issued
    static int clockCycle = 0;
    static ReservationStation[] Adder;
    static ReservationStation[] Multiplier;
    static Buffer[] Load;
    static Buffer[] Store;
    static int[] RegisterFile; // reg number is the position;//FIXME make it even? and value , default is 0
    static CommonDataBus CDB; // one value has tag, the other has the actual value

   public Microprocessor(int adderSize, int multiplierSize, int loadSize, int storeSize, int registerSize)
   {
    instructions= new LinkedList<>();
    Adder = new ReservationStation[adderSize];
    Multiplier = new ReservationStation[multiplierSize];
    Load = new Buffer[loadSize];
    Store = new Buffer[storeSize];
    RegisterFile = new int[registerSize];
    CDB = new CommonDataBus(); //FIXME do the constructor

   }

   public static void checkCleanup(){
    //go thro the instructions getting executed rn and update stuff according yto cycke
    //if it is done, we remove it from the reservation station call method emptyBuffer, and remove from the instruction queue


   }

   public static void issue(Instruction instruction){
    //check if instruction is valid
    //check if buffer is full
    //check if there is empty reservation station
    //check if there is empty register file
    //check if there is empty load buffer
    //check if there is empty store buffer
    //check if there is empty CDB
    //check if there is empty instruction queue
    //check if there is empty current instruction queue
    //check if there is empty instruction queue
    //check if there is empty instruction queue
    //check if there is empty instruction

    //Available reservation station
     if(reservationStation(instruction) != -1)
     {
        issueInstruction(instruction);    
        
            
     } 
      //check if the reservation station for that intruction has empty space... if it does, we issue it

         // if(Adder[i].busy == false)
                    // {
                    //     Adder[i].busy = true;
                    //     Adder[i].instruction = instruction;
                    //     Adder[i].Qj = instruction.j;
                    //     Adder[i].Qk = instruction.k;
                    //     Adder[i].Vj = RegisterFile[instruction.j];
                    //     Adder[i].Vk = RegisterFile[instruction.k];
                    //     Adder[i].A = instruction.j + instruction.k;
                    //     Adder[i].instructionNumber = instruction.instructionNumber;
                    //     Adder[i].issueCycle = clockCycle;
                    //     instruction.issued = true;
                    //     instruction.issueCycle = clockCycle;
                    //     break;
                    // }
   }

   public static int availableReservationStation(ReservationStation[] reservationStation)
   {
       for(int i=0; i<reservationStation.length; i++)
       {
           if(reservationStation[i].busy == 0)
           {
               return i;
           }
       }
       return -1;
   } 

   public static int availableBuffer(Buffer [] buffer)
   {
       for(int i=0; i<buffer.length; i++)
       {
           if(buffer[i].busy == 0)
           {
               return i;
           }
       }
       return -1;
   }

   //FIXME floating point in same reg file right not separate?

   public static int reservationStation(Instruction instruction)
   {
        switch(instruction.instructionType)
        {
            case ADD: case SUB: case ADDI: case SUBI: case BNEZ:  //FIXME bnez here?
                return availableReservationStation(Adder);
            case MUL: case DIV: //FIXME case MULI: case DIVI:
                return availableReservationStation(Multiplier);
            case LD:
                return availableBuffer(Load);
            case SD:
                return availableBuffer(Store);
            default: return -1;
                   
                }
                
        }
   
    
        public static void issueInstruction(Instruction instruction)
        {
            int position = reservationStation(instruction);
            switch(instruction.instructionType)
            {
                case ADD: case SUB: case ADDI: case SUBI: case BNEZ:  //FIXME bnez here?
                    Adder[position].busy = 1;
                    Adder[position].instructionType = instruction.instructionType;
                    Adder[position].vj = instruction.j;
                    Adder[position].vk = instruction.k;
                    Adder[position].qj = instruction.j;
                    Adder[position].qk = instruction.k;
                    Adder[position].A = instruction.j + instruction.k;
                    //Adder[position].tag = instruction.instructionNumber;
                    break;
                case MUL: case DIV: //FIXME case MULI: case DIVI:
                    Multiplier[position].busy = 1;
                    Multiplier[position].instructionType = instruction.instructionType;
                    Multiplier[position].vj = instruction.j;
                    Multiplier[position].vk = instruction.k;
                    Multiplier[position].qj = instruction.j;
                    Multiplier[position].qk = instruction.k;
                    Multiplier[position].A = instruction.j + instruction.k;
                   // Multiplier[position].tag = instruction.instructionNumber;
                    break;
                case LD:
                    Load[position].busy = 1;
                   // Load[position].tag = instruction.instructionNumber;
                    break;
                case SD:
                    Store[position].busy = 1;
                  //  Store[position].tag = instruction.instructionNumber;
                    break;
                default: break; 
            }
        }

    public static void loadInstructions(){
         //read the file
        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              String[] words = data.split("\\s+");
              Instruction instruction = new Instruction(words);
              instructions.add(instruction);
            }
            myReader.close();
          } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
        /* Instruction instruction = new Instruction();
            instruction.initialise(); */
    }
    public static void main(String[] args) {
        //initialise the variables in this arch at the top
        //loop  --> check instructions loop
        //if there are previous instructions , we do the checks
        //check onstruction valid
        //see if issued do this etc
           // for(int i=0;)
        Microprocessor microprocessor=new Microprocessor(2,3,3,3,10);
        loadInstructions();
       
        for( Instruction instruction: instructions) {
            checkCleanup();
            
            // issue();
            // execute();
            // write();
            // print();
            clockCycle++;
            
            
          }
}

}