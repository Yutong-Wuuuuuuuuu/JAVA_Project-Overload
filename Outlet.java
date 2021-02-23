package components;

public class Outlet extends Component {
    /**
     * Constructor of the outlet class.
     * @param name Name of the outlet.
     * @param source Source of the outlet.
     */
    public Outlet(String name, Component source) {
        super(name, source);
        this.getSource().attach(this);
    }

    @Override
    public String toString(){
        if(this.engaged()){
            return "Outlet " + this.getName() + "(on; draw" + this.getDraw() + ")";
        }else{
            return "Outlet " + this.getName() + "(off; draw" + this.getDraw() + ")";        }
    }

    /**
     * This is an unswitchable item it always return false.
     */
    @Override
    public boolean isSwitchOn() {
        return false;
    }
}
