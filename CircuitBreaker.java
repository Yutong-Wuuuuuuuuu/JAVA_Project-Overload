package components;

public class CircuitBreaker extends Component {
    private int limit;
    private boolean check_turned_on;

    /**
     * Constructor of a circuit breaker.
     * @param name Name of the circuit breaker.
     * @param source Source of the circuit breaker.
     * @param limit Limit of the circuit breaker.
     */
    public CircuitBreaker(String name, Component source, int limit) {
        super(name, source);
        this.limit = 0;
        this.getSource().attach(this);
        this.check_turned_on = false;
        this.limit = limit;
    }

    /**
     * Notify the user that it is engaging.
     */
    @Override
    public void engage(){
        this.changeSwitch(true);
        Reporter.report(this, Reporter.Msg.ENGAGING);
    }

    /**
     * Returns the limit of the circuit breaker.
     */
    public int getLimit() {
        return this.limit;
    }

    /**
     * Notify the user that the circuit breaker is turining on, also engage its load.
     */
    public void turnOn(){
        Reporter.report(this,Reporter.Msg.SWITCHING_ON);
        this.check_turned_on = true;
        this.engageLoads();
    }

    /**
     * Draw change to its source.
     * Also checks whether the change drawed on itself exceeds the limit. If so, show
     * the blown up message, and disengage itself and all of its load.
     * @param delta the change to be drawn.
     */
    @Override
    public void changeDraw(int delta){
        this.setDraw(delta);
        Reporter.report(this,Reporter.Msg.DRAW_CHANGE,delta);
        if(this.getDraw()>this.limit){
            Reporter.report(this, Reporter.Msg.BLOWN,this.getDraw());
            this.turnOff();
            return;
        }
        if(this.getSource() != null) {
            this.getSource().changeDraw(delta);
        }

    }

    /**
     * Turn off the circuit breaker.
     */
    public void turnOff(){
        Reporter.report(this, Reporter.Msg.SWITCHING_OFF);
        this.check_turned_on = false;
        this.getSource().changeDraw(-this.getSource().getDraw());
        this.setDraw(-this.getDraw());
        this.disengageLoads();
    }

    @Override
    public String toString(){
        if(this.engaged()){
            return "CircuitBreaker " + this.getName() + "(on; draw" + this.getDraw()+ "; " + this.limit + ")";
        }else{
            return "CircuitBreaker " + this.getName() + "(off; draw" + this.getDraw()+ "; " + this.limit + ")";        }
    }

    /**
     * Return whether the switch is on.
     */
    @Override
    public boolean isSwitchOn() {
        return this.check_turned_on;
    }
}
