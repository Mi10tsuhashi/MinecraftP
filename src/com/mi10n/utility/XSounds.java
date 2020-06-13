package com.mi10n.utility;


import org.bukkit.Sound;

public enum XSounds {
	BLOCK_NOTE_BLOCK_XYLOPHONE("BLOCK_NOTE_XYLOPHONE","BLOCK_NOTE_HAT","NOTE_STICKS"),
	BLOCK_NOTE_BLOCK_GUITAR("BLOCK_NOTE_GUITAR","BLOCK_NOTE_PLING","NOTE_PLING"),
	BLOCK_METAL_PRESSURE_PLATE_CLICK_ON("BLOCK_METAL_PRESSUREPLATE_CLICK_ON","CLICK"),
	BLOCK_NOTE_BLOCK_BELL("BLOCK_NOTE_BELL","BLOCK_NOTE_SNARE","NOTE_SNARE_DRUM"),
	ENTITY_ITEM_BREAK("ITEM_BREAK"),
	BLOCK_CHEST_CLOSE("CHEST_CLOSE"),
	BLOCK_CHEST_OPEN("CHEST_OPEN"),
	;
	String[] alias;
	XSounds(String... args){
		this.alias = args;
	}

	 public Sound parseSound(){
		 try {
	        Sound sound = Sound.valueOf(this.toString());
	        return sound;
		 }catch (IllegalArgumentException e) {
			 return parseSound(0);
		 }
	    }
	 private Sound parseSound(int i) {
		 Sound sound = null;
		 int tmp = 0;
		 try {
			 for(int k =i;k<alias.length;k++) {
				 tmp=k+1;
				 sound = Sound.valueOf(alias[k]);
				 if(sound!=null) {
				 return sound;
				 }
			 }

		 } catch(IllegalArgumentException e) {
			 return parseSound(tmp);
		 }
		return null;

	 }


}
