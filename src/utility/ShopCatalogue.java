package utility;

import java.util.ArrayList;
import java.util.List;

import main.renderer.TextureRegion;

public class ShopCatalogue {
    private final int id;
    private final String name;
	private List<Object> contents = new ArrayList<Object>();
	private TextureRegion icon;

    public ShopCatalogue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public List<Object> getContents() {
    	return contents;
    }
    public void setContents(List<Object> contents) {
    	this.contents = contents;;
    }
    public void setIcon(TextureRegion icon) {
		this.icon = icon;
	}
    public TextureRegion getIcon() {
		return icon;
	}
}
