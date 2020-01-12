package org.usfirst.frc2797.Robot2019v2.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class LandingGear extends Subsystem {

    private static Solenoid solenoid;
    //private final Solenoid solenoid2;   

    @Override
    protected void initDefaultCommand() {
    }

    public LandingGear(){
        solenoid = new Solenoid(7);
        //solenoid2 = new Solenoid(7);

        solenoid.set(false);
        //solenoid2.set(false);
    }

    public static  boolean toggle(){
        solenoid.set(!solenoid.get());
        return true;
        //solenoid2.set(!solenoid2.get());
    }

    public void extend(){
        solenoid.set(true);
        //solenoid2.set(true);
    }

    public void retract(){
        solenoid.set(false);
        //solenoid2.set(false);
    }

    @Override
    public void periodic(){
        boolean extended;
        if(solenoid.get() == true){
            extended = true; 
        }
        else{
            extended = false;
        }
        SmartDashboard.putBoolean("Landing Extended", extended);
    }

}