public class Buffer {
    String tag;
    int busy;
    int effectiveAddress;
    int startCycle;
    //FIXME add address?
    boolean readyToWrite;

    public Buffer(String tagCharacter) {
        this.busy = 0;
        this.tag = tagCharacter ;
        this.effectiveAddress = -1;
        this.startCycle = -1;
    }
    
    public void print()
    {
        System.out.print("Tag: " + tag +
                         "\tBusy: " + busy +
                         "\tEffective Address: " + effectiveAddress +
                         "\tStart Cycle: " + startCycle +
                         "\tReady To Write: " + readyToWrite);
    
    }
    
}

