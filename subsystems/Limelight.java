
package org.usfirst.frc2797.Robot2019v2.subsystems;

import org.usfirst.frc2797.Robot2019v2.Robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



public class Limelight extends Subsystem implements PIDSource{
    public enum PIDMode{ktx, kty, kta, kts}
    private NetworkTable table;
    private NetworkTableEntry tx; //Horizontal angle offset
    private NetworkTableEntry ty; //Vertical angle offset
    private NetworkTableEntry ta; //Area of target
    private NetworkTableEntry ts; //Skew of target
    private NetworkTableEntry tv; //Valid target available
    
    private PIDController llPID;
    private PIDSourceType type;
    private PIDMode mode;

    private Spark dummy3;

    private Drivetrain drivetrain;
    private boolean isProcessing;


    public void initDefaultCommand(){

    }

    @Override
    public void periodic(){
        if(table.getEntry("camMode").getNumber(0).equals(0)){
            isProcessing = true;
        }else{
            isProcessing = false;
        }
        SmartDashboard.putBoolean("Limelight Pro Mode", isProcessing);
    }

    public Limelight(){
        //region Network Table Initilization
        table = NetworkTableInstance.getDefault().getTable("limelight");
        tx = table.getEntry("tx");
        ty = table.getEntry("ty");
        ta = table.getEntry("ta");
        ts = table.getEntry("ts");
        tv = table.getEntry("tv");
        //endregion Network Table Initialization

        //region PIDController Component Initialization
        dummy3 = new Spark(13);
        this.type = PIDSourceType.kDisplacement;
        mode = PIDMode.ktx;
        drivetrain = Robot.drivetrain;
        //endregion PIDController Component Initialization

        //region PIDController Initialization
        llPID = new PIDController(0.065, 0.1, 0.15, 0.5, this, dummy3);
        llPID.setAbsoluteTolerance(2.0);
        llPID.setInputRange(-27.0, 27.0);
        llPID.setOutputRange(-1.0, 1.0);
        llPID.setContinuous(false);
        llPID.disable();
        //endregion PIDController Initialization

        llPromode();
    }
    //region Network Table Getters
    public double gettx(){return tx.getDouble(0);}
    public double getty(){return ty.getDouble(0);}
    public double getta(){return ta.getDouble(0);}
    public double getts(){return ts.getDouble(0);}
    public double gettv(){return tv.getDouble(0);}
    //endregion Network Table Getters

    //region PIDMode Getter/Setter
    public PIDMode getPIDMode(){return mode;}
    public void setPIDMode(PIDMode mode){this.mode = mode;}
    //endregion PIDMode Getter/Setter

    //region PIDSource Methods
    public PIDSourceType getPIDSourceType(){return type;}
    public void setPIDSourceType(PIDSourceType type){this.type = type;}

    public double pidGet(){
        if(mode == PIDMode.ktx)
            return gettx();
        else if(mode == PIDMode.kty)
            return getty();
        else if(mode == PIDMode.kta)
            return getta();
        else if(mode == PIDMode.kts)
            return getts();
        else
            return 0;
    }
    //endregion PIDSource Methods

    public void resetPID(){llPID.reset();}
    public void disablePID(){llPID.disable();}
    public void enablePID(){llPID.enable();}

    public void center(double speed){
        disablePID();
        resetPID();
        //llPromode();

        llPID.setOutputRange(-speed, speed);
        llPID.setSetpoint(0);

        enablePID();

        while(!llPID.onTarget()){
            if(gettv() == 0){
                System.out.println("ERROR: Target Lost!\nStopping PIDController.");
                break;
            }
            drivetrain.drive(dummy3.get(), -dummy3.get());
        }
        disablePID();
        resetPID();
        //llCammode();
    }
    
    public void llCammode(){
        table.getEntry("camMode").setNumber(1);
    }

    public void llPromode(){
        table.getEntry("camMode").setNumber(0);
    }
}
