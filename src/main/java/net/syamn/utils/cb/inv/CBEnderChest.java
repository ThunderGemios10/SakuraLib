/**
 * SakuraLib - Package: net.syamn.utils.cb.inv
 * Created: 2013/02/05 2:47:11
 */
package net.syamn.utils.cb.inv;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R1.inventory.CraftInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import net.minecraft.server.v1_7_R1.EntityHuman;
import net.minecraft.server.v1_7_R1.InventoryEnderChest;
import net.minecraft.server.v1_7_R1.InventorySubcontainer;
import net.minecraft.server.v1_7_R1.ItemStack;

/**
 * CBEnderChest (CBEnderChest.java)
 */
public class CBEnderChest extends InventorySubcontainer {
    public static HashMap<String, CBEnderChest> chests = new HashMap<String, CBEnderChest>();
    
    public List<HumanEntity> transaction = new ArrayList<HumanEntity>();
    public boolean playerOnline = false;
    private CraftPlayer owner;
    private InventoryEnderChest enderChest;
    private int maxStack = MAX_STACK;
    private CraftInventory inventory = new CraftInventory(this);
    
    @Deprecated
    public CBEnderChest(Player p, Boolean online) {
        //TODO fix it, broken on 1.5
        super(((CraftPlayer) p).getHandle().getEnderChest().getInventoryName(), true, ((CraftPlayer) p).getHandle().getEnderChest().getSize());
        //super(((CraftPlayer) p).getHandle().getEnderChest().getName(), ((CraftPlayer) p).getHandle().getEnderChest().getSize());
        
        CraftPlayer player = (CraftPlayer) p;
        this.enderChest = player.getHandle().getEnderChest();
        this.owner = player;
        this.items = enderChest.getContents();
    }
    
    public Inventory getBukkitInventory() {
        return inventory;
    }
    
    public void InventoryRemovalCheck() {
        if (transaction.isEmpty() && !playerOnline) {
            owner.saveData();
            chests.remove(owner.getName().toLowerCase());
        }
    }
    
    public void PlayerGoOnline(Player p) {
        if (!playerOnline) {
            try {
                InventoryEnderChest playerEnderChest = ((CraftPlayer) p).getHandle().getEnderChest();
                Field field = playerEnderChest.getClass().getField("items");
                field.setAccessible(true);
                field.set(playerEnderChest, this.items);
            } catch (Exception ignore) {}
            p.saveData();
            playerOnline = true;
        }
    }
    
    
    public void PlayerGoOffline() {
        playerOnline = false;
    }
    
    @Override
    public ItemStack[] getContents() {
        return this.items;
    }
    
    @Override
    public void onOpen(CraftHumanEntity who) {
        transaction.add(who);
    }
    
    @Override
    public void onClose(CraftHumanEntity who) {
        transaction.remove(who);
    }
    
    @Override
    public List<HumanEntity> getViewers() {
        return transaction;
    }
    
    @Override
    public InventoryHolder getOwner() {
        return this.owner;
    }
    
    @Override
    public void setMaxStackSize(int size) {
        maxStack = size;
    }
    
    @Override
    public int getMaxStackSize() {
        return maxStack;
    }
    
    @Override
    public boolean a(EntityHuman entityhuman) {
        return true;
    }
    
    @Override
    public void startOpen() {
        
    }
    
    @Override
    public void l_() {
        
    }
    
    @Override
    public void update() {
        enderChest.update();
    }
}
