package io.notifye.botengine.action;

import java.io.Serializable;

public interface Action extends Serializable {
	public static String actionName = "Action";
	
	public String getActionName();
	public int getOrderExecution(); 

}
