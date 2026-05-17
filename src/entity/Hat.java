package entity;

public class Hat {

	private int index;
	
	public Hat(int index) {
	    this.index = index;
	}
	public int getIndex() {
	    return index;
	}
	public boolean doesHideHead() {
	    switch(index) {
	    case 2:
	    	return true;
	    	default: 
	    		return false;
	    }
	    
	}
	
	public boolean doesHideHair() {
	    switch(index) {
	    case 2, 3:
	    	return true;
	    	default: 
	    		return false;
	    }
	    
	}
	
}
