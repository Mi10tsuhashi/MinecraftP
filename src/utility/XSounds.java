package utility;


import org.bukkit.Material;
import org.bukkit.Sound;

public enum XSounds {
	BLOCK_NOTE_BLOCK_XYLOPHONE("BLOCK_NOTE_XYLOPHONE"),
	BLOCK_NOTE_BLOCK_GUITAR("BLOCK_NOTE_GUITAR"),
	BLOCK_METAL_PRESSURE_PLATE_CLICK_ON("BLOCK_METAL_PRESSUREPLATE_CLICK_ON"),
	BLOCK_NOTE_BLOCK_BELL("BLOCK_NOTE_BELL"),
	;
	String s;
	XSounds(String s){
		this.s = s;
	}
	public Sound parseSounds(){
        Sound s = Sound.valueOf(isNewVersion()?this.toString():this.s.toString());
        return s;
	}
	public static boolean isNewVersion(){
        Material mat = Material.getMaterial("RED_WOOL");
        if(mat != null){
            return true;
        } return false;
    }

}
