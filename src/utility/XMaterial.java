package utility;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public enum XMaterial {
	 BOOK(0, "BOOK"),
	 BONE(0, "BONE"),
	 CLOCK(0, "WATCH"),
	 ENDER_EYE(0, "EYE_OF_ENDER"),
     GHAST_TEAR(0, "GHAST_TEAR"),
     GOLD_BLOCK(0, "GOLD_BLOCK"),
     GRAY_DYE(8, "INK_SACK"),
     HEAVY_WEIGHTED_PRESSURE_PLATE(0, "IRON_PLATE"),
     LIGHT_WEIGHTED_PRESSURE_PLATE(0, "GOLD_PLATE"),
     LIME_WOOL(5, "WOOL"),
	 RED_WOOL(14, "WOOL"),
	 ROSE_RED(1, "INK_SACK"),
	 SIGN(0,"SIGN","SIGN_POST"),
	 WALL_SIGN(0, "WALL_SIGN"),
	 ;
	String[] m;
    int data;

    XMaterial(int data, String... m){
        this.m = m;
        this.data = data;
    }
    @SuppressWarnings("deprecation")
	public ItemStack parseItem(){
        Material mat = parseMaterial();
        if(isNewVersion()){
            return new ItemStack(mat);
        }
        return new ItemStack(mat,1,(byte) data);
    }
    public static boolean isNewVersion(){
        Material mat = Material.getMaterial("RED_WOOL");
        if(mat != null){
            return true;
        }
        return false;
    }
    public Material parseMaterial(){
        Material mat = Material.matchMaterial(this.toString());
        if(mat != null){
            return mat;
        }
        return Material.matchMaterial(m[0]);
    }
    public Material parseMaterial(int i){
        Material mat = Material.matchMaterial(this.toString());
        if(isNewVersion()){
            return mat;
        }
        return Material.valueOf(m[i]);
    }


}
