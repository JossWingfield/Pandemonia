package utility.save;

import java.util.ArrayList;
import java.util.List;

import entity.items.Food;

public class BuildingSaveData extends SaveData {
	
	public int x;
	public int y;
	public int preset = -1;
	public String name;
	public int decorType = -1;
	public String string1;
	public boolean boolean1 =false;
	
	public int attribute1 = -1;
	public int attribute2 = -1;
	
	public List<String> fridgeContents = new ArrayList<>();
	public List<Integer> fridgeContentStates = new ArrayList<>();
	public int fridgeType = -1;
}
