package utility.save;

import main.GamePanel;

public class SettingsSaveData extends SaveData {
	
    public boolean fullScreen;
    public boolean showUsernames;
    public boolean fancyLighting;
    public boolean bloomEnabled;
	
	public void applySaveData(GamePanel gp) {
		gp.world.applySettingsData(this);
	}
    
}
