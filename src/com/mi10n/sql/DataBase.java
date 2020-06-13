package com.mi10n.sql;

import java.nio.ByteBuffer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.mi10n.main.MinecraftP;

public abstract class DataBase {
     private Connection connection;
     public DataBaseType type;
     public static void initSQL() {
    	 DataBase database;
    	 MinecraftP plugin = MinecraftP.getPlugin(MinecraftP.class);
         YamlConfiguration file = plugin.getSQLConfig();
    	 if(file.getBoolean("Enable")) {
    		 Bukkit.getConsoleSender().sendMessage("MySQL is enabled");
    		 database = new MySQL();
             plugin.setDatabase(database);
             database.type = DataBaseType.MySQL;

    	 }else {
    		 Bukkit.getConsoleSender().sendMessage("MySQL is disabled");
    		 database = new SQLite();
    		 plugin.setDatabase(database);
    		 database.type = DataBaseType.SQLite;
    	 }
    	 plugin.getDatabase().openConnection();
    	 String createtable=null;
         switch(plugin.getDatabase().type) {
         case MySQL:

        	  createtable = "CREATE TABLE IF NOT EXISTS MinecraftPcourse (courseId INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR(15) NOT NULL UNIQUE, uuid VARBINARY(16) NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);";
              plugin.getDatabase().updateQuery(createtable, success->{
            	  if(success) {Bukkit.getConsoleSender().sendMessage("[MinecraftP] The course table was created successfully. (MySQL)");}
              });

        	  createtable = "CREATE TABLE IF NOT EXISTS MinecraftPtime (timeId INTEGER PRIMARY KEY AUTO_INCREMENT, courseId INTEGER NOT NULL, uuid VARBINARY(16) NOT NULL, time DECIMAL(13,0) NOT NULL,FOREIGN KEY (courseId) REFERENCES MinecraftPcourse(courseId) ON DELETE CASCADE ON UPDATE CASCADE); ";
              plugin.getDatabase().updateQuery(createtable, success->{
            	  if(success) {Bukkit.getConsoleSender().sendMessage("[MinecraftP] The time table was created successfully. (MySQL)");}
              });

              break;
         case SQLite:

        	 createtable = "CREATE TABLE IF NOT EXISTS MinecraftPcourse (courseId INTEGER PRIMARY KEY, name VARCHAR(15) NOT NULL UNIQUE, uuid VARBINARY(16) NOT NULL, created TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL);";
        	 plugin.getDatabase().updateQuery(createtable, success->{
        		 if(success) {Bukkit.getConsoleSender().sendMessage("[MinecraftP] The course table was created successfully. (SQLite)");}
             });


        	  createtable = "CREATE TABLE IF NOT EXISTS MinecraftPtime (timeId INTEGER PRIMARY KEY, courseId INTEGER NOT NULL, uuid VARBINARY(16) NOT NULL, time DECIMAL(13,0) NOT NULL,FOREIGN KEY (courseId) REFERENCES MinecraftPcourse(courseId) ON DELETE CASCADE ON UPDATE CASCADE);";
              plugin.getDatabase().updateQuery(createtable, success->{
            	  if(success) {Bukkit.getConsoleSender().sendMessage("[MinecraftP] The time table was created successfully. (SQLite)");}
              });

        	  break;
         default:
        	 break;
         }
     }
     protected  abstract Connection openConnection();
     public final boolean closeConnection() {
    	 if(checkConnection()) {
    		 try {
    		 this.connection.close();
    		 return true;
    		 } catch(SQLException e) {
    			 return false;
    		 }
    	 }
    	 return false;
     }
     public  String getCourseName(int id) {
    	 if (!checkConnection()) {
    	      openConnection();
    	    }
   	 String coursename = null;
   	 try(Statement s = this.connection.createStatement(); ResultSet r = s.executeQuery("SELECT name FROM MinecraftPcourse WHERE courseId ="+id+";");){
   		 if(r.next()) {
   		 coursename = r.getString("name");
   		 }

   	 }   catch(SQLException e) {
          e.printStackTrace();
   	 }
   	 return coursename;
   }
     protected final boolean setConnection(Supplier<Connection> supplier){
    	 if(!checkConnection()) {
    	 this.connection = supplier.get();
    	 return this.connection==null ? false: true;
    	 }
    	 return false;
     }
     protected final Connection getConnection() {
    	 return checkConnection()? this.connection : null;
    	 }
     public final boolean checkConnection() {
    	 try {
    	 if(this.connection!=null&&!this.connection.isClosed()) {
    		 return true;
    	 } else {
    		 return false;
    	 }
    	 } catch(SQLException e) {
    		 return false;
    	 }
     }
     public boolean createCource(String name,Player player) {
    	 if (!checkConnection()) {
   	      openConnection();
   	    }
    	 try( PreparedStatement state = this.connection.prepareStatement("INSERT INTO `MinecraftPcourse` (`name`, `uuid`) VALUES (?, ?);")){
    		state.setString(1, name);
    		state.setBytes(2, UUIDtoByte(player.getUniqueId()));
     		state.executeUpdate();
  		} catch (SQLException e) {
  			return false;
  		}
    	 return true;
     }
     public boolean deleteCource(String name) {
    	 if (!checkConnection()) {
      	      openConnection();
      	    }
       	 try( PreparedStatement state = this.connection.prepareStatement("DELETE FROM `MinecraftPtime` WHERE `courseId`=?")){
       		 Integer id = getCourseId(name);
       		 if(id!=null) {
    	        state.setInt(1, id);
     		state.executeUpdate();
       		 }
  		} catch (SQLException e) {
  			return false;
  		}
       	 try( PreparedStatement state = this.connection.prepareStatement("DELETE FROM `MinecraftPcourse` WHERE `name`=?")){
       	        state.setString(1, name);
        		state.executeUpdate();
     		} catch (SQLException e) {
     			return false;
     		}

       	 return true;
     }
     public static byte[] UUIDtoByte(UUID uuid) {
    	 ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
         bb.putLong(uuid.getMostSignificantBits());
         bb.putLong(uuid.getLeastSignificantBits());
         return bb.array();
     }
     public static UUID BytetoUUID(byte[] bytearray) {
    	 ByteBuffer byteBuffer = ByteBuffer.wrap(bytearray);
         long high = byteBuffer.getLong();
         long low = byteBuffer.getLong();
         return new UUID(high, low);
     }
     public boolean executeQuery(String statement,Function<ResultSet,Boolean> function) {
    	 if (!checkConnection()) {
    	      openConnection();
    	    }
    	   try( Statement state = this.connection.createStatement()){
    		   return function.apply(state.executeQuery(statement));
		} catch (SQLException e) {
			return false;
		}
     }
     public boolean executeQuery(String statement,Player player,BiFunction<ResultSet,Player,Boolean> function) {
    	 if (!checkConnection()) {
    	      openConnection();
    	    }
    	   try( Statement state = this.connection.createStatement();ResultSet r = state.executeQuery(statement)){
    		   return function.apply(r,player);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
     }

     public boolean isBest(Player player,String parkourname,long time) {
    	 if (!checkConnection()) {
      	      openConnection();
      	    }
       	long best = getBest(player,parkourname);
       	if(best<0) {
       		return true;
       	} else {
       		return best<time ? false:true;
       	}
     }
     public void sendPlayedList(Player player, BiConsumer<ResultSet,Player> f) {
    	 if (!checkConnection()) {
     	      openConnection();
     	    }
    	 ResultSet result = null;
       	 try(PreparedStatement statement = this.connection.prepareStatement("SELECT courseId, time FROM MinecraftPtime WHERE uuid=? ORDER BY time;")){
       		 statement.setBytes(1, DataBase.UUIDtoByte(player.getUniqueId()));
       		 result = statement.executeQuery();
       		 f.accept(result, player);
       	 } catch(SQLException e) {
       		 e.printStackTrace();
       	 } finally {
       		 try {
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
       	 }
     }
     public long getBest(Player player,String parkourname) {
    	 if (!checkConnection()) {
      	      openConnection();
      	    }
    	 ResultSet result = null;
       	 try(PreparedStatement statement = this.connection.prepareStatement("SELECT time FROM MinecraftPtime WHERE courseId=? AND uuid=?")){
       		 Integer courseId = getCourseId(parkourname);
                 if(courseId!=null) {
                    statement.setInt(1, courseId);
                  	 statement.setBytes(2, UUIDtoByte(player.getUniqueId()));
                    result = statement.executeQuery();
                   if(result.next()) {
                     long time =result.getLong("time");
                     return time;
                     } else {
                       return -1;
                   		}
                 }
       	 }catch(SQLException e) {
       		 return -1;
       	 } finally {
       		 try {
       			 if(result!=null)
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
       	 }
        return -1;
     }
     public boolean updateTime(Player player,Integer courseId,long time) {
    	 if(courseId==null) {return false;}
    	 if (!checkConnection()) {
     	      openConnection();
     	    }
    	 try(PreparedStatement statement = this.connection.prepareStatement("UPDATE MinecraftPtime SET time=? WHERE courseId=? AND uuid=?;")){
    		 statement.setLong(1, time);
    		 statement.setInt(2, courseId);
    		 statement.setBytes(3, UUIDtoByte(player.getUniqueId()));
    		 statement.executeUpdate();
    		 return true;
    	 }catch(SQLException e) {e.printStackTrace();return  false;}
     }
     public boolean insertTime(Player player,Integer courseId,long time) {
    	 if(courseId==null) {return false;}
    	 if (!checkConnection()) {
     	      openConnection();
     	    }
    	 try(PreparedStatement statement = this.connection.prepareStatement("INSERT INTO MinecraftPtime (courseId, uuid, time) VALUES (?,?,?);")){
    		 statement.setInt(1, courseId);
    		 statement.setBytes(2, UUIDtoByte(player.getUniqueId()));
    		 statement.setLong(3, time);
    		 statement.executeUpdate();
    		 return true;
    	 }catch(SQLException e) {
    		 e.printStackTrace();
    		 return false;
    		 }
     }
     public Integer getCourseId(String name) {
    	 if (!checkConnection()) {
     	      openConnection();
     	    }
    	 ResultSet result = null;
    	 Integer courseid = null;
    	 try(PreparedStatement statement = this.connection.prepareStatement("SELECT courseId FROM MinecraftPcourse WHERE name =?;");){
    		 statement.setString(1, name);
    		 result = statement.executeQuery();
    		 if(result.next()) {
    			 courseid = result.getInt("courseId");
    		 }
    	 }catch(SQLException e) {
              e.printStackTrace();
    	 } finally {
    		 try {
    			 if(result!=null)
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	 }
    	 return courseid;
     }
     public boolean updateQuery(String statement,Consumer<Boolean> consumer) {
    	 if (!checkConnection()) {
    	      openConnection();
    	    }
    	 boolean success = false;;
    	  try( Statement state = this.connection.createStatement()){
   		   state.executeUpdate(statement);
   		   success = true;
		} catch (SQLException e) {
			success = false;
		} finally {
			consumer.accept(success);
		}
    	  return success;
     }

}
