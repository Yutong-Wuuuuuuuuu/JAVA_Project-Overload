package components;

import java.util.ArrayList;

public class Appliance extends Component{
    private int rate;
    private boolean turned_on;

    /**
     * Override the engage method. The purpose is the check whether the appliance's switch is on.
     * If the appliance is turned on, when it is engaged, it will also turn itself on.
     */
    @Override
    public void engage(){
        Reporter.report(this,Reporter.Msg.ENGAGING);
        this.changeSwitch(true);
        if(this.turned_on){
            this.turnOn();
        }
    }

    /**
     * Constructor of Appliance.
     * @param name The name of the appliance.
     * @param source The source of the appliance.
     * @param rate The rate of the appliance
     */
    public Appliance(String name, Component source, int rate) {
        super(name, source);
        this.getSource().attach(this);
        this.rate = rate;
        this.turned_on = false;
    }

    /**
     * Returns the rating of the appliance.
     */
    public int getRating(){
        return this.rate;
    }

    /**
     * If the source is engaged and the appliance's switch is on, change draw.
     * If the source is engaged and the appliance's switch is not on, notify the user that it is turned on, and
     * change draw.
     * If the source is not on, and this method is called, only turn the switch on.
     */
    public void turnOn(){
        if(this.getSource().engaged() && this.turned_on){
            this.changeDraw(this.rate);}
        else if(!this.turned_on && this.getSource().engaged()){
            this.turned_on = true;
            Reporter.report(this,Reporter.Msg.SWITCHING_ON);
            this.changeDraw(this.rate);
        }else{
            this.turned_on = true;
        }
    }

    /**
     * Notify the user that the appliance is turning off, and if the source is engaged, change draw.
     */
    public void turnOff(){
        Reporter.report(this,Reporter.Msg.SWITCHING_OFF);
        this.turned_on = false;
        if(this.getSource().engaged()){
            this.changeDraw(-this.rate);
        }
    }


    /**
     * Notify the user that it is disengaging. Only change draw when the switch is on.
     */
    @Override
    public void disengage(){
        Reporter.report(this,Reporter.Msg.DISENGAGING);
        if(this.turned_on = true){
            this.changeDraw(-this.rate);
        }
    }

    @Override
    public String toString(){
        if(this.engaged()){
            return "Appliance" + this.getName() + "(on; draw " + this.rate + ")";
        }else{
            return "Appliance " + this.getName() + "(off; rating " + this.rate + ")";
        }
    }

    /**
     * Return whether the switch is on.
     */
    @Override
    public boolean isSwitchOn() {
        return this.turned_on;
    }
}
