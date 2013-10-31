package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

public class DomsItem {
    /*
     * String Serialization: 
     * {size:4},{id:17},{data:2},{name:"Hey, this is cool {right?}",{author:DOMIN8TRIX25},{page:"This is my page, I love{it}"},{page:"Hey another page!"},{lore:"Some lore, it's cool {ik}"},{lore:"Anotherlore"},{enchantment:ARROW_DAMAGE*3},{enchantment:OXYGEN*3}
     */
    
    public static final Pattern ITEM_META_SEPERATOR_REGEX = Pattern.compile(",\\s*(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    public static final String ITEM_META_ATTRIBUTE_SEPERATOR_REGEX = "\\{(\\s*)(\\w+)\\:(\\s*)(\".*?(?<!\\\\)(\"))(\\s*)\\}";
    public static final short BAD_DATA = -1;
    
    public static List<DomsItem> createAllItems(List<String> list) throws InvalidItemException {
        List<DomsItem> items = new ArrayList<DomsItem>();
        
        for(String s : list) {
            items.addAll(DomsItem.createItems(s));
        }
        
        return items;
    }
    
    public static DomsItem createItem(String line) throws InvalidItemException {
        return createItems(line).get(0);
    }
    
    public static ItemStack createItem(List<DomsItem> item) {
        try {
            return item.get(0).getItemStack(item.size());
        } catch(Exception e) {return null;}
    }
    
    public static List<DomsItem> createItems(String line) throws InvalidItemException {
        try {
            line = line.replaceAll("\\n","\\\\n");
            String[] parts = line.split(ITEM_META_SEPERATOR_REGEX.pattern());
            
            Map<String, String> data = new HashMap<String, String>();
            List<String> lores = new ArrayList<String>();
            List<String> pages = new ArrayList<String>();
            Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
            
            for(String s : parts) {
                Matcher m = Pattern.compile(ITEM_META_ATTRIBUTE_SEPERATOR_REGEX).matcher(s);
                m.find();
                
                String key = m.group(2).toLowerCase();
                String value = m.group(4).replaceFirst("\"", "");
                value = value.substring(0, value.length()-1);
                value = value.replaceAll("&q", "\"");
                
                if(key.equals("page")) {
                    pages.add(Base.colorise(value));
                } else if(key.equals("lore")) {
                    lores.add(Base.colorise(value));
                } else if(key.equals("enchantment")) {
                    String[] e = value.split("\\*");
                    Enchantment enc = Enchantment.getByName(e[0]);
                    int i = Base.getInt(e[1]);
                    enchants.put(enc, i);
                }
                
                data.put(key, value);
            }
            
            int count = 1;
            String material = null;
            short idata = BAD_DATA;
            short damage = BAD_DATA;
            String author = null;
            String name = null;
            OfflinePlayer head = null;
            
            if(data.containsKey("size")) {
                count = Base.getInt(data.get("size"));
            }
            
            if(data.containsKey("id")) {
                material = DomsItem.guessMaterial(data.get("id"));
            }
            
            if(data.containsKey("data")) {
                idata = Base.getShort(data.get("data"));
            }
            
            if(data.containsKey("damage")) {
                damage = Base.getShort(data.get("damage"));
            }
            
            if(data.containsKey("author")) {
                author = data.get("author");
            }
            
            if(data.containsKey("name")) {
                name = Base.colorise(data.get("name"));
            }
            
            if(data.containsKey("head")) {
                head = Bukkit.getOfflinePlayer(data.get("head"));
            }
            
            List<DomsItem> items = new ArrayList<DomsItem>();
            for(int i = 0; i < count; i++) {
                DomsItem item = new DomsItem(material, idata);
                item.setDamage(damage);
                item.setPages(pages);
                item.setLores(lores);
                item.setEnchantments(enchants);
                item.setAuthor(author);
                item.setName(name);
                item.setPlayerHead(head);
                
                items.add(item);
            }
            
            return items;
        } catch(Exception e) {
            throw new InvalidItemException(line);
        }
    }
    
    public static DomsItem copy(DomsItem from) throws InvalidItemException {
        return DomsItem.createItem(from.toString());
    }

    public static List<DomsItem> itemStackToDomsItems(ItemStack is) {
        if(is == null) return null;
        List<DomsItem> items = new ArrayList<DomsItem>();
        
        DomsItem copy = new DomsItem(is);
        
        for(int i = 0; i < is.getAmount(); i++) {
            try {
                items.add(DomsItem.copy(copy));
            } catch(InvalidItemException e) {
                continue;
            }
        }
        
        return items;
    }
    
    public static boolean contains(List<DomsItem> doesThis, DomsItem containThis) {
        List<DomsItem> items = new ArrayList<DomsItem>();
        return contains(doesThis, items);
    }
    
    public static boolean contains(List<DomsItem> doesThis, List<DomsItem> containThis) {
        if(containThis.size() > doesThis.size()) return false;
        
        List<DomsItem> doesCopy = new ArrayList<DomsItem>(doesThis);
        
        for(DomsItem item : containThis) {
            
            boolean found = false;
            DomsItem remove = null;
            for(DomsItem i : doesCopy) {
                if(i.compare(item)) found = true;
                remove = i;
                if(found) break;
            }
            
            if(found) {
                doesCopy.remove(remove);
            }
            
            if(!found) return false;
        }
        
        return true;
    }
    
    public static List<String> getHumanMessages(List<DomsItem> items) {
        List<String> s1 = new ArrayList<String>();
        
        for(DomsItem i : items) {
            s1.add(i.toHumanString());
        }
        
        Map<String, Integer> count = new HashMap<String, Integer>();
        
        for(String s : s1) {
            if(!count.containsKey(s)) count.put(s, 0);
            count.put(s, count.get(s)+1);
        }
        
        List<String> s2 = new ArrayList<String>();
        
        for(String s : count.keySet()) {
            s2.add(count.get(s) + " lots of " + s);
        }
        
        return s2;
    }
    
    private static String escape(String s) {
        return Base.decolorise(s.replaceAll("\\\"", "&q").replaceAll("\\\\n", "\\n"));
    }

    static List<ItemStack> toItemStackArray(List<DomsItem> items) throws InvalidItemException {
        List<ItemStack> i = new ArrayList<ItemStack>();
        List<String> s1 = new ArrayList<String>();
        
        for(DomsItem it : items) {
            s1.add(it.toString());
        }
        
        Map<String, Integer> count = new HashMap<String, Integer>();
        
        for(String s : s1) {
            if(!count.containsKey(s)) count.put(s, 0);
            count.put(s, count.get(s)+1);
        }
        
        for(String s : count.keySet()) {
            DomsItem item = DomsItem.createItem(s);
            if(item.isAir()) continue;
            int amtneeded = count.get(s);
            while(amtneeded > 0) {
                int amttoadd = amtneeded;
                if(amttoadd > 64) amttoadd = 64;
                i.add(item.getItemStack(amttoadd));
                amtneeded -= amttoadd;
            }
        }
        return i;
    }
    
    public static boolean isInventoryFull(Inventory i) {
        List<ItemStack> contents = new ArrayList<ItemStack>();
        for(ItemStack is : i.getContents()) {
            if(is == null) continue;
            if(is.getType() == null) continue;
            if(is.getType().equals(Material.AIR)) continue;
            contents.add(is);
        }
        
        if(contents.size() >= i.getContents().length) {
            return true;
        }
        
        return false;
    }
    
    public static DomsItem guessItem(String s) throws InvalidItemException {
        try {
            return DomsItem.createItem(s);
        } catch(InvalidItemException e) {}
        
        String[] parts = s.split(":");
        
        if(parts.length < 1) throw new InvalidItemException(s);
        
        String material = DomsItem.guessMaterial(parts[0]);
        short data = BAD_DATA;
        
        if(parts.length > 1) {
            if(Base.isShort(parts[1])) {
                data = Base.getShort(parts[1]);
            }
        }
        
        if(material == null) throw new InvalidItemException(s);
        
        Material m = Material.getMaterial(material);
        if(m == null) throw new InvalidItemException(s);
        
        return new DomsItem(material, data);
    }
    
    public static List<DomsItem> multiply(DomsItem item, int amount) {
        List<DomsItem> items = new ArrayList<DomsItem>();
        for(int i = 0; i < amount; i++) {
            items.add(item.copy());
        }
        return items;
    }
    
    public static String guessMaterial(String l) {
        if(Base.isInt(l)) return Material.getMaterial(Base.getInt(l)).name();
        if(Material.getMaterial(l.toUpperCase()) != null) return Material.getMaterial(l.toUpperCase()).name();
        l = l.toLowerCase().replaceAll(" ", "").replaceAll("_", "");
        for(Material m : Material.values()) {
            String n = m.name().toLowerCase();
            n = n.replaceAll("_", "").replaceAll(" ", "");
            if(n.startsWith(l)) return m.name();
            if(n.contains(l)) return m.name();
        }
        
        return null;
    }

    public static DomsItem createItem(ItemStack is) {
        if(is == null) return null;
        List<DomsItem> item = DomsItem.itemStackToDomsItems(is);
        if(item == null || item.isEmpty()) return null;
        return item.get(0);
    }
    
    private static long NEXT_ID = Long.MIN_VALUE;
    
    //Instance
    private String material;
    private short data = BAD_DATA;
    private short damage;
    private Map<Enchantment, Integer> enchants;
    private List<String> bookPages;
    private String author;
    private String name;
    private List<String> lores;
    private long itemID;
    private OfflinePlayer head;
    
    public DomsItem(String material, short data, short damage, Map<Enchantment, Integer> enchants, List<String> pages, String name, List<String> lores) {
        this.material = material;
        this.data = data;
        this.damage = damage;
        this.enchants = enchants;
        this.bookPages = pages;
        this.name = name;
        this.lores = lores;
        this.itemID = NEXT_ID += 1;
    }
    
    public DomsItem(String material, short data, short damage, Map<Enchantment, Integer> enchants, List<String> pages, String name) {
        this(material, data, damage, enchants, pages, name, null);
    }
    
    public DomsItem(String material, short data, short damage, Map<Enchantment, Integer> enchants, List<String> pages, List<String> lores) {
        this(material, data, damage, enchants, pages, null, lores);
    }
    
    public DomsItem(String material, short data, short damage, Map<Enchantment, Integer> enchants, String name, List<String> lores) {
        this(material, data, damage, enchants, null, name, lores);
    }
    
    public DomsItem(String material, short data, Map<Enchantment, Integer> enchants, String name) {
        this(material, data, BAD_DATA, enchants, null, name, null);
    }
    
    public DomsItem(String material, short data, Map<Enchantment, Integer> enchants, List<String> lores) {
        this(material, data, BAD_DATA, enchants, null, null, lores);
    }
    
    public DomsItem(String material, short data, List<String> pages, String name, List<String> lores) {
        this(material, data, BAD_DATA, null, pages, name, lores);
    }
    
    public DomsItem(String material, short data, String name, List<String> lores) {
        this(material, data, BAD_DATA, null, null, name, lores);
    }
    
    public DomsItem(String material, short data, String name) {
        this(material, data, name, null);
    }
    
    public DomsItem(String material, short data, List<String> lores) {
        this(material, data, BAD_DATA, null, null, null, lores);
    }
    
    public DomsItem(Material m, short data) {
        this(m.name(), data);
    }
    
    public DomsItem(String material, short data) {
        this(material, data, BAD_DATA, null, null, null, null);
    }
    
    public DomsItem(String material) {
        this(material, BAD_DATA);
    }
    
    public DomsItem(Material m) {
        this(m.name());
    }
    
    @Deprecated
    public DomsItem(int id) {
        this(Material.getMaterial(id));
    }
    
    public DomsItem(ItemStack is) {
        this(
            is.getType().name(),
            is.getDurability()
        );
        
        if(is.getItemMeta() != null) {
            if(is.getItemMeta().getLore() != null) {
                if(is.getItemMeta().getLore().size() > 0) {
                    this.lores = new ArrayList<String>(is.getItemMeta().getLore());
                }
            }
            if(is.getItemMeta().getDisplayName() != null) {
                if(!is.getItemMeta().getDisplayName().equalsIgnoreCase("")) {
                    this.name = is.getItemMeta().getDisplayName();
                }
            }
            
            if(is.getItemMeta() instanceof BookMeta) {
                BookMeta book = (BookMeta) is.getItemMeta();
                this.bookPages = new ArrayList<String>(book.getPages());
                this.author = book.getAuthor();
            }
            
            if(is.getItemMeta() instanceof SkullMeta) {
                if(((SkullMeta) is.getItemMeta()).getOwner() != null) this.head = Bukkit.getOfflinePlayer(((SkullMeta) is.getItemMeta()).getOwner());
            }
        }
        
        this.enchants = new HashMap<Enchantment, Integer>(is.getEnchantments());
    }
    
    public String getMaterialName() {return this.material;}
    public short getData() {return this.data;}
    public short getDamage() {return this.damage;}
    public Map<Enchantment, Integer> getEnchantments() {return this.enchants;}
    public List<String> getBookPages() {return this.bookPages;}
    public String getBookAuthor() {return this.author;}
    public String getName() {String x = this.name; if(this.isMobNameable()) x = Base.trim(x, 64); return x;}
    public List<String> getLores() {return this.lores;}
    public long getItemID() {return this.itemID;}
    public OfflinePlayer getPlayerHead() {return this.head;}
    @Deprecated public MaterialData getMaterialData() {return this.getMaterial().getNewData((byte) this.data);}

    public boolean isAir() {return this.getMaterial() == null || this.getMaterial().equals(Material.AIR);}
    public boolean isBook() {return this.getMaterial().equals(Material.BOOK_AND_QUILL) || this.getMaterial().equals(Material.WRITTEN_BOOK);}
    public boolean isMobNameable() {return this.getMaterial().equals(Material.MONSTER_EGG) || this.getMaterial().equals(Material.MONSTER_EGGS) || this.getMaterial().equals(Material.NAME_TAG);}
    public boolean isHead() {return this.getMaterial().equals(Material.SKULL) || this.getMaterial().equals(Material.SKULL_ITEM);}

    public boolean hasData() {return this.data != DomsItem.BAD_DATA;}
    
    public void setMaterialName(String material) {this.material = material;}
    public void setData(short data) {this.data = data;}
    public void setDamage(short damage) {this.damage = damage;}
    public void setLores(List<String> lores) {this.lores = lores;}
    public void setPages(List<String> pages) {this.bookPages = pages;}
    public void setAuthor(String author) {this.author = author;}
    public void setName(String name) {this.name = name;}
    public void setEnchantments(Map<Enchantment, Integer> e) {this.enchants = e;}
    public void setPlayerHead(OfflinePlayer player) {this.head = player;}

    public void setPage(int page, String l) {this.bookPages.set(page, l);}
    
    public Material getMaterial() {return Material.getMaterial(this.material);}
    public ItemMeta getItemMeta(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        if(this.name != null && !this.name.equals("")) {
            String name = this.name;
            if(this.isMobNameable()) name = Base.trim(name, 64);
            im.setDisplayName(name);
        }
        
        if(im instanceof BookMeta) {
            BookMeta bm = (BookMeta) im;
            if(this.author != null && !this.author.equals("")) {
                bm.setAuthor(this.author);
            }
            
            if(this.bookPages != null) {
                bm.setPages(this.bookPages);
            }
        }
        
        if(im instanceof SkullMeta && this.head != null) {
            SkullMeta sm = (SkullMeta) im;
            sm.setOwner(this.head.getName());
            this.data = 3;
            is.setDurability(Base.getShort(3));
        }
        
        if(this.lores != null) {
            im.setLore(this.lores);
        }
        
        return im;
    }
    public ItemStack getItemStack() throws InvalidItemException {return getItemStack(64);}
    public ItemStack getItemStack(int amt) throws InvalidItemException {
        try {
            ItemStack is = new ItemStack(this.getMaterial(), amt, this.data);
            if(this.damage != BAD_DATA) {
                is.setDurability(this.damage);
            }
            is.setItemMeta(this.getItemMeta(is));
            if(this.enchants != null && this.enchants.size() > 0) {
                is.addUnsafeEnchantments(enchants);
            }
            return is;
        } catch(Exception e) {
            throw new InvalidItemException(this.toString());
        }
    }
    
    public String getTypeName() {
        String s = this.getMaterial().name();
        s = s.replaceAll("_", " ");
        s = s.toLowerCase();
        s = Base.capitalizeEachWord(s);
        s = s.replaceAll(" Item", "");
        s = s.replaceAll("Tnt", "TNT");
        return s;
    }
    
    public boolean compare(DomsItem item) {
        return item.toString().equalsIgnoreCase(this.toString());
    }
    
    public void addLore(String l) {this.lores.add(l);}
    public void addEnchantment(Enchantment byId, int lvl) {this.enchants.put(byId, lvl);}
    public void addPage(String l) {this.bookPages.add(l);}
    
    public DomsItem copy() {
        try {
            return DomsItem.copy(this);
        } catch(InvalidItemException e) {
            return new DomsItem(Material.AIR);
        }
    }
    
    @Override
    public String toString() {
        String msg = "{id:\"" + this.material + "\"}";
        
        if(this.data != BAD_DATA && this.data != 0) {
            msg += ",{data:\"" + Short.toString(this.data) + "\"}";
        }
        
        if(this.damage != BAD_DATA) {
            msg += ",{damage:\"" + Short.toString(this.damage) + "\"}";
        }
        
        if(this.lores != null) {
            for(String lore : this.lores) {
                msg += ",{lore:\"" + escape(lore) + "\"}";
            }
        }
        
        if(this.bookPages != null) {
            for(String page : this.bookPages) {
                msg += ",{page:\"" + escape(page) + "\"}";
            }
        }
        
        if(this.name != null && !this.name.equals("")) {
            msg += ",{name:\"" + escape(this.name) + "\"}";
        }
        
        if(this.author != null && !this.author.equals("")) {
            msg += ",{author:\"" + this.author + "\"}";
        }
        
        if(this.enchants != null) {
            for(Enchantment e : this.enchants.keySet()) {
                if(e == null) continue;
                msg += ",{enchantment:\"" + e.getName() + "*" + this.enchants.get(e) + "\"}";
            }
        }
        
        if(this.head != null) {
            msg += ",{head:\"" + escape(this.head.getName()) + "\"}";
        }
        
        return msg;
    }

    public String toHumanString() {
        String d = Base.ChatDefault;
        String s = d + this.getTypeName();
        
        if(this.data != BAD_DATA) {
            //s += ", with type of " + this.data;
        }
        
        if(this.damage != BAD_DATA) {
            s += ", with " + this.damage + " damage";
        }
        
        if(this.name != null && !this.name.equals("")) {
            s += ", named " + this.name + d;
        }
        
        if(this.enchants != null && this.enchants.size() > 0) {
            s += ", with the enchantment" + ((this.enchants.size() > 1) ? "s" : "");
            for(Enchantment e : this.enchants.keySet()) {
                if(e == null) continue;
                s += ", " + Base.capitalizeEachWord(e.getName().replaceAll("_", " ").toLowerCase()) + " at level " + enchants.get(e)    ;
            }
        }
        
        if(this.lores != null && this.lores.size() > 0) {
            s += ", with the lore"  + ((this.lores.size() > 1) ? "s" : "");
            for(String l : lores) {
                s += ", " + l + d;
            }
        }
        
        if(this.bookPages != null && this.bookPages.size() > 0) {
            s += ", and with the page" + (this.bookPages.size() > 1 ? "s" : "");
            for(String p : this.bookPages) {
                s += ", \"" + p + d + "\"";
            }
        }
        
        if(this.author != null && !this.author.equals("")) {
            s += ", written by " + this.author + d;
        }
        
        return s;
    }
}
