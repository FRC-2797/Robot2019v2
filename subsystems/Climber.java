package org.usfirst.frc2797.Robot2019v2.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber extends Subsystem {
    //Creating Class Motors

    private static  DoubleSolenoid solenoid;

    public Climber(){
        solenoid = new DoubleSolenoid(4,5);

        solenoid.set(Value.kReverse);
    }

    public void initDefaultCommand(){

    }

    public static boolean toggleClimber(){
        solenoid.set((solenoid.get().equals(Value.kForward) ? Value.kReverse:Value.kForward)); 
        return true; 
    }

    public void extend(){
        solenoid.set(Value.kForward);
    }

    public void retract(){
        solenoid.set(Value.kReverse);
    }

    @Override
    public void periodic(){
        boolean extended;
        if(solenoid.get() == Value.kForward){
            extended = true; 
        }
        else{
            extended = false;
        }
        SmartDashboard.putBoolean("Climber Extended", extended);
    }
}