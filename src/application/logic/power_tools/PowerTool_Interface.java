package application.logic.power_tools;

public interface PowerTool_Interface{
	public void activate();					//activates the power-up effect
	public void deactivate();				//deactivates the power-up effect
	public boolean isActive();				//return true if power-up is currently active
    
}