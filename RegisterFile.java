public class RegisterFile {
 
    
    int busy=0;
    String tag;
    float value;

    public RegisterFile() {
        this.busy = 0;
        this.tag = ""; //if tag isnt empty then there is an instruction awaiin value
        this.value = -1;
    }
}
