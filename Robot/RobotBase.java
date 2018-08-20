package org.firstinspires.ftc.teamcode.FTC_API.Robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.FTC_API.Robot.SubSystems.DriveSystemTemplate;
import org.firstinspires.ftc.teamcode.FTC_API.Robot.SubSystems.SubSystem;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Ethan Hampton on 8/19/17.
 * <p>
 * Main robot class that should be extended by all robot configurations
 */

public class RobotBase {

    private HashMap<String, SubSystem> subSystems = new HashMap<>();
    private String driveSystem = "";

    private ElapsedTime time = new ElapsedTime();

    protected void addSubSystem(SubSystem sub) {
        String subID = sub.ID().equals("") ? sub.getClass().getSimpleName() : sub.ID();
        //if the ID class hasn't been overridden, then use class name, else use the ID
        subSystems.put(subID, sub);

        if (sub instanceof DriveSystemTemplate) {
            driveSystem = subID;
        }
    }

    public Collection<SubSystem> getSubSystems() {
        return subSystems.values();
    }

    public SubSystem getSubSystem(String name) {
        return subSystems.get(name);
    }

    public DriveSystemTemplate getDriveSystem(){
        if(!driveSystem.isEmpty()) {
            return (DriveSystemTemplate) subSystems.get(driveSystem);
        }else{
            return null;
        }
    }

    //registers all of the subsystems and prepares the robot
    public boolean init(HardwareMap hardwareMap) {
        boolean noErrors = true;
        for (SubSystem s : subSystems.values()) {
            s.init(hardwareMap);//init each system

            if (!s.isFunctioning() || !s.isInitialized()) {//insure all systems are ready to go
                noErrors = false;
            }
        }

        startTime();//start time keeping
        return noErrors;
    }

    //called to stop all subsystems
    public void stop() {
        for (SubSystem s :
                subSystems.values()) {
            s.stop();
        }
    }

    /**
     * Tick method that should be called in the tick method of {@link com.qualcomm.robotcore.eventloop.opmode.OpMode}
     * to insure all submodules have a chance to update and get info from sensors and motors
     *
     * It may incur a slight performance disadvantage but shouldn't be too impacting
     */
    public void tick() {
        for (SubSystem s :
                subSystems.values()) {
            s.tick();
        }
    }

    private void startTime() {
        time.startTime();
    }

    protected void resetTime() {
        time.reset();
    }

    public double getTimeMilliseconds() {
        return time.milliseconds();
    }
}
