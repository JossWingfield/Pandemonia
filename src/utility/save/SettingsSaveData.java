package utility.save;

import main.GamePanel;

public class SettingsSaveData extends SaveData {
	
    public boolean fullScreen;
    public boolean showUsernames;
    public boolean fancyLighting;
    public boolean bloomEnabled;
    public boolean godRaysEnabled;
    public boolean occlusionEnabled;
    public boolean shadowsEnabled;
	
	public void applySaveData(GamePanel gp) {
		gp.world.gameM.applySettingsData(this);
	}
    
}
