package components;

public class PowerSource extends Component {
    public PowerSource(String name) {
        super(name, null);
    }

    /**
     * Engage the power source, change the state of engage to true, engage loads if it has any.
     */
    @Override
    public void engage(){
        Reporter.report(this,Reporter.Msg.ENGAGING);
        this.changeSwitch(true);
        if(this.getLoads().size()>0){
            this.engageLoads();
        }
    }

    /**
     * Disengage the power source, disengage its loads if it has any.
     */
    @Override
    public void disengage(){
        Reporter.report(this,Reporter.Msg.DISENGAGING);
        if(this.getLoads().size()>0){
            this.disengageLoads();
        }

    }

    /**
     * It is not a switchable item, so this will always return false.
     */
    @Override
    public boolean isSwitchOn() {
        return false;
    }

    @Override
    public String toString(){

        return "PowerSource" + this.getName() + "(draw " + this.getDraw() + ")";
    }
}
