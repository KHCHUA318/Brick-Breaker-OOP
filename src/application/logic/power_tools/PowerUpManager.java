package application.logic.power_tools;

import java.util.*;

/* Manages activation, duration tracking, and deactivate of power-ups.
 * Handles concurrent power-ups and provides special handling for protection types.
 */
public class PowerUpManager {
    private static final long DEFAULT_DURATION = 5000; // 5 seconds
    // Active power-up tracking
    private final List<ActivePowerUp> activePowerUps = new ArrayList<>();
    
    //Internal class tracking active power-ups and their expiration
    private class ActivePowerUp {
        PowerTool_Interface tool;
        long expireTime;
        
        ActivePowerUp(PowerTool_Interface tool, long duration) {
            this.tool = tool;
            this.expireTime = System.currentTimeMillis() + duration;
        }
    }
    
    //activates a power-up and sets its duration
    public void activatePowerUp(PowerTool_Interface powerUp) {
        if (powerUp == null) return;
        
        //deactivate previous instance of the same type
        deactivateSameType(powerUp.getClass());
        //activate the current power
        powerUp.activate();
        activePowerUps.add(new ActivePowerUp(powerUp, getDuration(powerUp)
        ));
    }
    
    //Updates all active power-ups, deactivating expired ones
    public void update() {
        Iterator<ActivePowerUp> it = activePowerUps.iterator();
        long currentTime = System.currentTimeMillis();
        							
        while (it.hasNext()) {								//Updates power-up states 
            ActivePowerUp entry = it.next();
            if (currentTime >= entry.expireTime) {
                entry.tool.deactivate();					//removes expired one
                it.remove();
            }
        }
    }
    
    public void clearAll() {
        activePowerUps.forEach(entry -> entry.tool.deactivate());
        activePowerUps.clear();   							//Clear all active power-ups immediately
    }
    
//-------------for BALL protection-------------
    public boolean hasActiveProtection() {
        for (ActivePowerUp entry : activePowerUps) {
            if (entry.tool instanceof BallProtection) {
                return true;								//return true if any protection power-up is active
            }
        }
        return false;
    }
    public BallProtection getActiveProtection() {
        for (ActivePowerUp entry : activePowerUps) {
            if (entry.tool instanceof BallProtection) {
                return (BallProtection) entry.tool;			 //Get the currently active ball protection
            }
        }
        return null;
    }
    
/*--------------HELPER METHOD--------------
 */
    private void deactivateSameType(Class<?> powerUpType) {
        activePowerUps.removeIf(entry -> {
            if (entry.tool.getClass().equals(powerUpType)) {
                entry.tool.deactivate();					//deactivate all power-ups of the specified type
                return true;
            }
            return false;
        });
    }
    
    private long getDuration(PowerTool_Interface powerUp) {	//get the duration for a specific power-up type
        // Custom durations per power-up type
        return switch (powerUp.getClass().getSimpleName()) {
        	case "PaddleSlow" -> 10_000;    	// 10 seconds
            case "BallProtection" -> 10_000; 	// 10 seconds
            default -> DEFAULT_DURATION;    	// other default 5 seconds
        };
    }    
}