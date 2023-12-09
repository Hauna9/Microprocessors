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



    
 
 */
//queue instructions; //FIXME make it queue?

import java.util.*;
import java.io.*;


public class Microprocessor {

    static Queue<Instruction> instructions;
    static Queue<Instruction> instructionsToWrite; //got issued
    static int clockCycle = 0;
    static ReservationStation[] Adder;
    static ReservationStation[] Multiplier;
    static Buffer[] Load;
    static Buffer[] Store;
    static RegisterFile[] RegisterFile; // reg number is the position;//FIXME make it even? and value , default is 0
    static CommonDataBus CDB; // one value has tag, the other has the actual value
    static float[] Memory;
    static boolean stall=false;

   public Microprocessor(int adderSize, int multiplierSize, int loadSize, int storeSize, int registerSize, int memorySize)
   {
    instructions= new LinkedList<>();
    Adder = new ReservationStation[adderSize];
    populateReservationStation(Adder, "A");
    Multiplier = new ReservationStation[multiplierSize];
    populateReservationStation(Multiplier, "M");
    Load = new Buffer[loadSize];
    populateBuffer(Load, "L");
    Store = new Buffer[storeSize];
    populateBuffer(Store, "S");
    RegisterFile = new RegisterFile[registerSize];
    CDB = new CommonDataBus(); //FIXME do the constructor
    Memory = new float[memorySize];
    

   }

    public static void populateReservationStation(ReservationStation[] reservationStation, String tagCharacter)
    {
         for(int i=0; i<reservationStation.length; i++)
         {
            // makes every position have the correct tag; m1 m2 m3 etc
              reservationStation[i] = new ReservationStation(tagCharacter+ (i+1));
         }
    }

    public static void populateBuffer(Buffer[] buffer, String tagCharacter)
    {
         for(int i=0; i<buffer.length; i++)
         {
             // makes every position have the correct tag; L1 L2 L3 etc
              buffer[i] = new Buffer(tagCharacter + (i+1));
         }
    }
    
    public static void checkCleanup(){
    //go thro the instructions getting executed rn and update stuff according yto cycke
    //if it is done, we remove it from the reservation station call method emptyBuffer, and remove from the instruction queue


   }

   public static void issue(Instruction instruction){
    if (instruction.issued==false)
  {
    //Available reservation station
     if(reservationStation(instruction) != -1)
     {
       stall=false;
        issueInstruction(instruction);     
           
     } 
     else{
            stall=true;
        
     }
   }
}

   public static void execute(Instruction instruction){

    if(!instruction.executed) //check instruction has not been executed
    {
        if(instruction.executeStartCycle == -1) //execution has not started
       { if(canExecute(instruction))
        {
            //start execution
            instruction.executeStartCycle = clockCycle;
            startExecute(instruction);
        }
         }
    else{
            instruction.duration--;
        if(instruction.duration == 0)
        {
            instruction.executed = true;
            instruction.executeEndCycle = clockCycle;
            executeInstruction(instruction);
            if(instruction.instructionType != InstructionType.SD)
                {
                    //instructionsToWrite.add(instruction);
                    emptyBuffer(Store, instruction.position);
                    instruction.written = true;
                }

        }
        }
    
        
    }   

    }

    public static void startExecute(Instruction instruction)
    {
        switch(instruction.instructionType)
        {
            //FIXME bnez??
            case ADD: case SUB: case ADDI: case SUBI: case BNEZ:  //FIXME bnez here?
                Adder[instruction.position].startCycle = clockCycle;
                break;
            case MUL: case DIV: 
                Multiplier[instruction.position].startCycle = clockCycle;
                break;
            case LD:
                Load[instruction.position].startCycle = clockCycle;
                break;
            case SD:
                Store[instruction.position].startCycle = clockCycle;
                break;
            default: 
                break;
        }
    }

   public static boolean canExecute(Instruction instruction)
   {
         switch(instruction.instructionType)
         {
              case ADD: case SUB:   //FIXME bnez here?
                return Adder[instruction.position].qj=="" && Adder[instruction.position].qk=="";
              case ADDI: case SUBI: 
                return Adder[instruction.position].qj==""; //immediate is always available
              case MUL: case DIV: 
                return Multiplier[instruction.position].qj=="" && Multiplier[instruction.position].qk=="";
              case LD:
                return true;
              case SD:
                return RegisterFile[instruction.destinationRegister].busy==0; //FIXME check this
              case BNEZ: //FIXME
               // return canExecuteBNEZ(instruction);
              default: return false;
                    
                }
                
         }
   
   public static void executeInstruction(Instruction instruction)
   {
        switch(instruction.instructionType)
        {
            case ADD:   //FIXME bnez here?
                instruction.result = Float.parseFloat(Adder[instruction.position].vj) + Float.parseFloat(Adder[instruction.position].vk);
                Adder[instruction.position].readyToWrite = true;
                break;
            case SUB: 
                instruction.result = Float.parseFloat(Adder[instruction.position].vj) - Float.parseFloat(Adder[instruction.position].vk);
                Adder[instruction.position].readyToWrite = true;
                break;
            case ADDI:
                instruction.result = Float.parseFloat(Adder[instruction.position].vj) + instruction.immediate;
                Adder[instruction.position].readyToWrite = true;
                break;
            case SUBI:
                instruction.result = Float.parseFloat(Adder[instruction.position].vj) - instruction.immediate;
                Adder[instruction.position].readyToWrite = true;
                break; 
            case BNEZ: //TODO bnez??
            break;
            case MUL:
                instruction.result = Float.parseFloat(Multiplier[instruction.position].vj) * Float.parseFloat(Multiplier[instruction.position].vk);
                Multiplier[instruction.position].readyToWrite = true;
                break;
            case DIV: 
                instruction.result = Float.parseFloat(Multiplier[instruction.position].vj) / Float.parseFloat(Multiplier[instruction.position].vk);
                Multiplier[instruction.position].readyToWrite = true;
                break;
            case LD: //FIXME this is in writeback
               // RegisterFile[instruction.destinationRegister].value = Memory[instruction.effectiveAddress];
                Load[instruction.position].readyToWrite = true;
            case SD://FIXME this is in writeback
                 Memory[instruction.effectiveAddress] = RegisterFile[instruction.destinationRegister].value;
                 
                break;
            default: 
                break;
        }
   }
   
   public static void write()
   {
    //FIXME do i need this? if(instruction.executed && !instruction.written)
    {

       if(instructionsToWrite.isEmpty())
       {
           return;
       }
      
       else
       {
            Instruction instructionToWrite = instructionsToWrite.poll();
            writeInstruction(instructionToWrite);
       }
       
       
      
    }


   }

   
   public static int findIndex(Instruction instruction)
   {
       int i=0;
         for(Instruction ins: instructions)
         {
              if(ins.equals(instruction))
              {
                return i;
              }
              i++;
         }
       return -1;
   }

   public static void emptyReservationStation(ReservationStation[] reservationStation, int position)
   {
       reservationStation[position].readyToWrite = false;
       reservationStation[position].busy = 0;
       reservationStation[position].instructionType = null;
       reservationStation[position].vj = "";
       reservationStation[position].vk = "";
       reservationStation[position].qj = "";
       reservationStation[position].qk = "";
       reservationStation[position].A = "";
       reservationStation[position].startCycle=-1;
   }

   public static void emptyBuffer(Buffer[] buffer, int position)
   {
       buffer[position].busy = 0;
       buffer[position].effectiveAddress = -1;
       buffer[position].startCycle = -1;
       //buffer[position].readyToWrite = false; //FIXME?
   }

   public static void writeInstruction(Instruction instruction)
   {
         //FIXME write to CDB
         
         //FIXME update finished
         //FIXME update tags in both
         //FIXME empty reservation station (method to empty in buffr and reservation station)
         //check me updates reservations buffers with the new balues
         //FIXME check -1 etc or that handled above?
        
        instruction.written=true;
        instruction.finished=true; //FIXME redundant? also handle prints
        instructions.remove(findIndex(instruction));
          switch(instruction.instructionType)
        {
            case ADD: case SUB: case ADDI: case SUBI: case BNEZ:  //FIXME bnez here?
                if(Adder[instruction.position].readyToWrite)
                {
                    //FIXME write to CDB
                    CDB.tag = Adder[instruction.position].tag;
                    CDB.value = instruction.result + "";
                    emptyReservationStation(Adder, instruction.position);
                    instruction.written = true;
                }
                break;
            case MUL: case DIV: 
                if(Multiplier[instruction.position].readyToWrite)
                {
                    //FIXME write to CDB
                    CDB.tag = Multiplier[instruction.position].tag;
                    CDB.value = instruction.result +"";
                    emptyReservationStation(Multiplier, instruction.position);
                    instruction.written = true;
                }
                break;
            case LD:
                if(Load[instruction.position].readyToWrite)
                {
                    //FIXME write to CDB
                    CDB.tag = Load[instruction.position].tag;
                    CDB.value = Memory[instruction.effectiveAddress] + "";
                    emptyBuffer(Load, instruction.position);
                    instruction.written = true;
                }
                break;
            case SD:
                if(Store[instruction.position].readyToWrite)
                {
                    //FIXME write to CDB
                    //  CDB.tag = Store[instruction.position].tag;
                    // CDB.value = Memory[instruction.effectiveAddress] + "";
                    emptyBuffer(Store, instruction.position);
                    instruction.written = true;
                }
                break;
            default: 
                break;
        }
        updateStations(instruction);
   }

   public static void updateStations(Instruction instruction)
   {
       for(ReservationStation reservationStation: Adder)
       {
           if(reservationStation.qj.equals(instruction.tag))
           {
               reservationStation.qj = "";
               reservationStation.vj = CDB.value+ "";
           }
           if(reservationStation.qk.equals(instruction.tag))
           {
               reservationStation.qk = "";
               reservationStation.vk = CDB.value+ "";
           }
       }
       for(ReservationStation reservationStation: Multiplier)
       {
           if(reservationStation.qj.equals(instruction.tag))
           {
               reservationStation.qj = "";
               reservationStation.vj = CDB.value+ "";
           }
           if(reservationStation.qk.equals(instruction.tag))
           {
               reservationStation.qk = "";
               reservationStation.vk = CDB.value+ "";
           }
       }
    //    for(Buffer buffer: Store)
    //    {
    //        if(buffer.tag.equals(instruction.tag))
    //        {
    //            buffer.readyToWrite = false;
    //            buffer.busy = 0;
    //            buffer.effectiveAddress = -1;
    //            buffer.startCycle = -1;
    //        }
    //   }
    for(int i=0; i<RegisterFile.length; i++)
    {
        if(RegisterFile[i].tag.equals(instruction.tag))
        {
            RegisterFile[i].busy = 0;
            RegisterFile[i].tag = "";
            RegisterFile[i].value = Float.parseFloat(CDB.value);
        }
    }
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

   // return the position of the reservation station that is available , else -1 if no free slot
   public static int reservationStation(Instruction instruction)
   {
        switch(instruction.instructionType)
        {
            case ADD: case SUB: case ADDI: case SUBI: case BNEZ:  //FIXME bnez here?
                return availableReservationStation(Adder);
            case MUL: case DIV: 
                return availableReservationStation(Multiplier);
            case LD:
                return availableBuffer(Load);
            case SD:
                return availableBuffer(Store);
            default: return -1;
                   
                }
                
        }

   public static void checkAvailability(int j, int k, ReservationStation[] reservationStation, int position)
        {
            if(RegisterFile[j].busy == 0)
            {  //register value is available
                reservationStation[position].vj = Memory[j] + "";
                reservationStation[position].qj = "";
            }
            else
            { //register value is not available so we add the reg tag of the reservation station then
                //TODO use the tags in the reservation station to find the values from reg file
                reservationStation[position].qj = RegisterFile[j].tag;
                reservationStation[position].vj = "";
            }
            if(RegisterFile[k].busy == 0)
            {
                reservationStation[position].vk = Memory[j] + "";
                reservationStation[position].qk = "";
            }
            else
            {
                reservationStation[position].qk = RegisterFile[k].tag;
                reservationStation[position].vk = "";
            }
        }
   
public static void checkAvailabilityImmediate(int j, int immediate, ReservationStation[] reservationStation, int position)
 {
            if(RegisterFile[j].busy == 0)
            {  //register value is available
                reservationStation[position].vj =Memory[j] + "";
                reservationStation[position].qj = "";
            }
            else
            { //register value is not available so we add the reg tag of the reservation station then
                //TODO use the tags in the reservation station to find the values from reg file
                reservationStation[position].qj = RegisterFile[j].tag;
                reservationStation[position].vj = "";
            }
            
                reservationStation[position].vk = immediate + "";
                reservationStation[position].qk = "";
            
            
        }

    public static void issueInstruction(Instruction instruction)
        {
            int position = reservationStation(instruction);
            if(position ==-1)
            {
                //FIXME if -1 we stall only right?
                return; 
            }
            switch(instruction.instructionType)
            {
                case ADD: case SUB:   //FIXME bnez here?
                    Adder[position].busy = 1;
                    Adder[position].instructionType = instruction.instructionType;
                    checkAvailability(instruction.j, instruction.k, Adder, position);
                    //FIXME what if the reg file busy of that reg was 1?
                    RegisterFile[instruction.destinationRegister].busy = 1;
                    RegisterFile[instruction.destinationRegister].tag = Adder[position].tag;
                    instruction.tag = Adder[position].tag;
                    break;

                case MUL: case DIV: 
                   Multiplier[position].busy = 1;
                   Multiplier[position].instructionType = instruction.instructionType;
                   checkAvailability(instruction.j, instruction.k, Multiplier, position);
                    //FIXME what if the reg file busy of that reg was 1?
                    RegisterFile[instruction.destinationRegister].busy = 1;
                    RegisterFile[instruction.destinationRegister].tag = Multiplier[position].tag;
                    instruction.tag = Multiplier[position].tag;
                    break;

                case ADDI: case SUBI: 
                    Adder[position].busy = 1;
                    Adder[position].instructionType = instruction.instructionType;
                    checkAvailabilityImmediate(instruction.j, instruction.immediate, Adder, position);
                    RegisterFile[instruction.destinationRegister].busy = 1;
                    RegisterFile[instruction.destinationRegister].tag = Adder[position].tag;
                    instruction.tag = Adder[position].tag;
                   break;

                case LD:
                    Load[position].busy = 1;
                    Load[position].effectiveAddress= instruction.effectiveAddress;
                    RegisterFile[instruction.destinationRegister].busy = 1; 
                    RegisterFile[instruction.destinationRegister].tag = Load[position].tag;
                    instruction.tag = Load[position].tag;
                    break;
                case SD:
                    Store[position].busy = 1;
                    Store[position].effectiveAddress= instruction.effectiveAddress;
                    RegisterFile[instruction.destinationRegister].busy = 1; 
                    RegisterFile[instruction.destinationRegister].tag = Store[position].tag;
                    instruction.tag = Store[position].tag;
                    break;
                case BNEZ:

                break;
            }
            instruction.issued=true;
            instruction.issueCycle=clockCycle;
            instruction.position=position; //FIXME do we need this?
        }

    public static void loadInstructions(Microprocessor microprocessor, int[] latencies){
         //read the file
        try {
            File myObj = new File("input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              String data = myReader.nextLine();
              String[] words = data.split("[,\\s]+");
              Instruction instruction = new Instruction(words,microprocessor,latencies);
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
       
        //TODO gui for this , input and output
        int mulLatency=10;
        int loadLatency=3;
        int storeLatency=3;
        int divLatency=-3;
        int subLatency=2;
        int addLatency=2;
        int memorySize=100;
        int adderSize=3;
        int registerSize=30;
        int multiplierSize=2;
        int loadSize=3;
        int storeSize=3;
        int latencies[]={addLatency,subLatency,mulLatency,divLatency,loadLatency,storeLatency};

        
        Microprocessor microprocessor=new Microprocessor(adderSize,multiplierSize,loadSize,storeSize,registerSize,memorySize);
        loadInstructions(microprocessor,latencies);
       
        for( Instruction instruction: instructions) {
            checkCleanup();
            
            if(!stall)
             {issue(instruction);}
             execute(instruction);
             write();
            // print();
            //checkCleanup(); //should update buffers and stations and cdb ig and remove from arraylist instructions?
            clockCycle++;
            
            
          }
}

}