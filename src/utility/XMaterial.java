package utility;



import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	 ROSE_RED(1, "INK_SACK","RED_DYE"),//1.14 非互換
	 //parseItem only
	 SIGN(0,"OAK_SIGN","SPRUCE_SIGN","BIRCH_SIGN","JUNGLE_SIGN","ACACIA_SIGN","DARK_OAK_SIGN"),
	 //Material only
	 SIGN_POST(0,"SIGN","OAK_SIGN","SPRUCE_SIGN","BIRCH_SIGN","JUNGLE_SIGN","ACACIA_SIGN","DARK_OAK_SIGN"),
	 WALL_SIGN(0, "OAK_WALL_SIGN","SPRUCE_WALL_SIGN","BIRCH_WALL_SIGN","JUNGLE_WALL_SIGN","ACACIA_WALL_SIGN","DARK_OAK_WALL_SIGN"),
	 ;
	String[] alias;
    int data;

    XMaterial(int data, String... m){
        this.alias = m;
        this.data = data;
    }
    public ItemStack parseItem(){
        Material mat = parseMaterial();
        if(isLessThan1_12()){
        	return new ItemStack(mat,1,(byte) data);
        }
        return new ItemStack(mat);
    }
    public ItemStack[] parseItems(){
        Set<Material> resultSet = parseMaterials();
        List<ItemStack> rs = new ArrayList<>();
        resultSet.forEach(e->{
        	if(isLessThan1_12()){
            	rs.add(new ItemStack(e,1,(byte) data));
            }else {
                rs.add(new ItemStack(e));
            }
        });
        return (ItemStack[])rs.toArray();

    }
    public static boolean isLessThan1_12(){
        Material mat = Material.getMaterial("RED_WOOL");
        if(mat == null){
            return true;
        }
        return false;
    }
   public static boolean isMoreThan1_14() {
	   Material mat = Material.getMaterial("OAK_WALL_SIGN");
	   if(mat == null){
           return false;
       }
	   return true;
   }
    public Material parseMaterial(){
        Material mat = Material.matchMaterial(this.toString());
        if(mat!=null){
            return mat;
        }for(int i=0; i<alias.length; i++) {
        mat = Material.matchMaterial(alias[i]);
        if(mat!=null) {return mat;}
        }
        return mat;
    }
    public Set<Material> parseMaterials(){
        Set<Material> result = new HashSet<>();
        Material mat = Material.matchMaterial(this.toString());
        if(mat!=null){
            result.add(mat);
        }for(int i=0; i<alias.length; i++) {
        mat = Material.matchMaterial(alias[i]);
        if(mat!=null) {result.add(mat);}
        }
        return result;
    }



}
