package model.interfaces;

public interface LoggingCallback {

	public void showWayByNode(String wayId, String nodeId);

	public void showSpeedByWay(String wayId, String string);

	public void showWayByName(String wayId, String wayName);

	public void showWayByNameWithSpeed(String wayId, String wayName, String maxspeed);

	public void showNoRecord(); 
}
