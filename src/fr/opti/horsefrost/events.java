package fr.opti.horsefrost;

import com.sun.javafx.geom.Vec2d;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class events implements Listener {

    public static double VecDis(Vec2d a, Vec2d b){
        double r = Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow( a.y - b.y, 2));
        return r;
    }

    public static boolean hasEntity(Location l) {
        //Object[] en =l.getWorld().getNearbyEntities(new BoundingBox(l.getBlockX()-2,l.getBlockY(),l.getBlockZ()-2,l.getBlockX()+2,l.getBlockY(),l.getBlockZ()+2)).toArray();
        Entity[] en=l.getChunk().getEntities();
        for (Entity i:en
             ) {
            if(VecDis(new Vec2d(i.getLocation().getBlockX(),i.getLocation().getBlockZ()),new Vec2d(l.getBlockX(),l.getBlockZ()))<=i.getWidth() && i.getLocation().getBlockY()==l.getBlockY()){
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playertick(PlayerMoveEvent e){
        Player player = e.getPlayer();
        if (player.getVehicle() instanceof Horse) {
            Horse horse = (Horse) player.getVehicle();
            if( horse.isOnGround()) {
                ItemStack armor = horse.getInventory().getArmor();
                if (armor == null) {
                    return;
                }
                if (armor.containsEnchantment(Enchantment.FROST_WALKER)) {
                    int level = armor.getEnchantmentLevel(Enchantment.FROST_WALKER);
                    //4 blocs + 2 * lvl

                    Location loc = horse.getLocation().subtract(0, 1, 0);
                    int radius = 2 + level;
                    Block middle = loc.getBlock();

                    //ArrayList<Tuple<Integer,Integer>> enc = new ArrayList<Tuple<Integer,Integer>>();
                    for (int x = radius; x >= -radius; x--) {
                        for (int z = radius; z >= -radius; z--) {
                            if(VecDis(new Vec2d(horse.getLocation().getBlockX()+x,horse.getLocation().getBlockZ()+z),new Vec2d(horse.getLocation().getBlockX(),horse.getLocation().getBlockZ()))<=radius) {
                                if (middle.getRelative(x, 0, z).getType() == Material.WATER) {
                                    Levelled l = (Levelled) middle.getRelative(x, 0, z).getBlockData();
                                        if (l.getLevel() == 0 && middle.getRelative(x, 1, z).getType() != Material.WATER && !hasEntity(middle.getRelative(x, 0, z).getLocation())) {
                                            middle.getRelative(x, 0, z).setType(Material.FROSTED_ICE);
                                        }
                                }

                            }
                        }
                    }

                }
            }
        }

    }
}
