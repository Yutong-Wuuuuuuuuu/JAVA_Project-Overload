package components;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

abstract class Component {
     private Boolean Switch;
     private String Name;
     private Component Source_of_power;
     private ArrayList<Component> load;
     private int draw;

    /**
     * Constructor of the Component class.
     * @param name Name of the component.
     * @param source Source(another component) of the Component.
     */
     public Component(String name,Component source){
         this.Name = name;
         this.Switch = false;
         this.Source_of_power = source;
         this.draw = 0;
         this.load = new ArrayList<Component>();
         Reporter.report(this,Reporter.Msg.CREATING);
     }

    /**
     * @return the name of the component.
     */
     public String getName(){
         return this.Name;
     }

    /**
     * Change the state that indicates whether this component is engaged.
     * @param b True or false, if true then the component is engaged. Vice versa.
     */
     public void changeSwitch(boolean b){
         this.Switch = b;
     }

    /**
     * Engage the component. Also engage its load.
     */
    public void engage(){
         Reporter.report(this,Reporter.Msg.ENGAGING);
         this.Switch = true;
         if(this.load.size()>0){
             this.engageLoads();
         }
     }

    /**
     * Disengage the component and its loads(if it has any)
     */
    public void disengage(){
         Reporter.report(this,Reporter.Msg.DISENGAGING);
         this.Switch = false;
         if(this.load.size()>0){
             this.disengageLoads();
         }
     }

    /**
     * Change the draw of the component.
     * If delta > 0: change draw to all of its source also.
     * If delta < 0: change draw to all of its source unless the source of its current is 0.
     * @param delta The draw to be changed.
     */
     protected void changeDraw(int delta){
         this.setDraw(delta);
         Reporter.report(this,Reporter.Msg.DRAW_CHANGE,delta);
         if(delta>0){
             if(this.getSource() != null) {
                 this.getSource().changeDraw(delta);
             }
         }else{
             if(this.getSource()!=null && this.getSource().getDraw()>0){
                 this.getSource().changeDraw(delta);
             }
         }


     }

    /**
     * Add a new load to the component.
     * @param newload Another Component.
     */
     protected void addLoad(Component newload){
         this.load.add(newload);
     }
     protected void attach(Component load){
        if(this.engaged()){
            load.engage();
        }
         this.addLoad(load);
         Reporter.report(this, load, Reporter.Msg.ATTACHING);
     }

    /**
     * Set the draw of the component.
     * @param draw A positive or negative integer.
     */
     protected void setDraw(int draw){
         this.draw += draw;
     }

    /**
     * @return the draw of the component.
     */
     protected int getDraw(){
         return this.draw;
     }

    /**
     * @return the source of the component.
     */
     protected Component getSource(){
         return this.Source_of_power;
     }

    /**
     * @return All the load of the component
     */
     protected Collection<Component> getLoads(){
         return Collections.unmodifiableCollection( this.load );

     }

    /**
     * @return Whether this component is engaged.
     */
     protected boolean engaged(){
         return this.Switch;
     }

    /**
     * Engage all the load of this component.
     */
    protected void engageLoads(){
         for(Component c: this.getLoads()){
             c.engage();
         }
     }

    /**
     * Disengage all of its loads.
     */
    protected void disengageLoads(){
         for(Component c: this.getLoads()){
             c.disengage();
         }
     }

    /**
     * Prints out nicely the component itself and all of its loads.
     */
    public void display(){
         System.out.println("+ "+ this.toString());
         if(this.load.size()>0){
             for(Component c: this.load){
                 this.helper();
                 c.display();
             }
         }
     }

     public void helper(){
        if(this.getSource() == null){
            System.out.print("\t");
        }else if(this.getSource().getSource() == null){
            System.out.print("\t\t");
        }else{
            System.out.print("\t\t\t");
        }
     }

    /**
     * @return A message representing this component.
     */
     public String toString(){
         return "" + this.getClass() + " " + this.Name + "(draw" + this.draw + ")";
     }

    public abstract boolean isSwitchOn();
    };

