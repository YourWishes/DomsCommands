package com.domsplace.DomsCommands.Objects;

import com.domsplace.DomsCommands.Bases.Base;
import com.domsplace.DomsCommands.Exceptions.InvalidItemException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

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
            int id = 0;
            short idata = BAD_DATA;
            String author = null;
            String name = null;
            
            if(data.containsKey("size")) {
                count = Base.getInt(data.get("size"));
            }
            
            if(data.containsKey("id")) {
                if(Base.isInt(id)) {
                    id = Base.getInt(data.get("id"));
                } else {
                    id = DomsItem.guessID(data.get("id"));
                }
            }
            
            if(data.containsKey("data")) {
                idata = Base.getShort(data.get("data"));
            }
            
            if(data.containsKey("author")) {
                author = data.get("author");
            }
            
            if(data.containsKey("name")) {
                name = Base.colorise(data.get("name"));
            }
            
            List<DomsItem> items = new ArrayList<DomsItem>();
            for(int i = 0; i < count; i++) {
                DomsItem item = new DomsItem(id, idata);
                item.setPages(pages);
                item.setLores(lores);
                item.setEnchantments(enchants);
                item.setAuthor(author);
                item.setName(name);
                
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
            Base.debug("Checking if bank contains " + item.toHumanString());
            
            boolean found = false;
            DomsItem remove = null;
            for(DomsItem i : doesCopy) {
                Base.debug("Bank contains " + i.toHumanString());
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
        return s.replaceAll("\\\"", "&q").replaceAll("\\\\n", "\\n");
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
        
        int id = DomsItem.guessID(parts[0]);
        short data = BAD_DATA;
        
        if(parts.length > 1) {
            if(Base.isShort(parts[1])) {
                data = Base.getShort(parts[1]);
            }
        }
        
        if(id < 1) throw new InvalidItemException(s);
        
        Material m = Material.getMaterial(id);
        if(m == null) throw new InvalidItemException(s);
        
        return new DomsItem(id, data);
    }
    
    public static List<DomsItem> multiply(DomsItem item, int amount) {
        List<DomsItem> items = new ArrayList<DomsItem>();
        for(int i = 0; i < amount; i++) {
            items.add(item.copy());
        }
        return items;
    }
    
    public static int guessID(String l) {
        if(Base.isInt(l)) return Base.getInt(l);
        l = l.toLowerCase().replaceAll(" ", "").replaceAll("_", "");
        for(Material m : Material.values()) {
            String n = m.name().toLowerCase();
            n = n.replaceAll("_", "").replaceAll(" ", "");
            if(n.startsWith(l)) return m.getId();
            if(n.contains(l)) return m.getId();
        }
        
        return DomsItem.BAD_DATA;
    }
    
    //Instance
    private int id;
    private short data = BAD_DATA;
    private Map<Enchantment, Integer> enchants;
    private List<String> bookPages;
    private String author;
    private String name;
    private List<String> lores;
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, List<String> pages, String name, List<String> lores) {
        this.id = id;
        this.data = data;
        this.enchants = enchants;
        this.bookPages = pages;
        this.name = name;
        this.lores = lores;
    }
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, List<String> pages, String name) {
        this(id, data, enchants, pages, name, null);
    }
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, List<String> pages, List<String> lores) {
        this(id, data, enchants, pages, null, lores);
    }
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, String name, List<String> lores) {
        this(id, data, enchants, null, name, lores);
    }
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, String name) {
        this(id, data, enchants, null, name, null);
    }
    
    public DomsItem(int id, short data, Map<Enchantment, Integer> enchants, List<String> lores) {
        this(id, data, enchants, null, null, lores);
    }
    
    public DomsItem(int id, short data, List<String> pages, String name, List<String> lores) {
        this(id, data, null, pages, name, lores);
    }
    
    public DomsItem(int id, short data, String name, List<String> lores) {
        this(id, data, null, null, name, lores);
    }
    
    public DomsItem(int id, short data, String name) {
        this(id, data, name, null);
    }
    
    public DomsItem(int id, short data, List<String> lores) {
        this(id, data, null, null, null, lores);
    }
    
    public DomsItem(Material m, short data) {
        this(m.getId(), data);
    }
    
    public DomsItem(int id, short data) {
        this(id, data, null, null, null, null);
    }
    
    public DomsItem(int id) {
        this(id, BAD_DATA);
    }
    
    public DomsItem(Material m) {
        this(m.getId());
    }
    
    public DomsItem(ItemStack is) {
        this(
            is.getType().getId(),
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
        }
        
        this.enchants = new HashMap<Enchantment, Integer>(is.getEnchantments());
    }
    
    public int getID() {return this.id;}
    public short getData() {return this.data;}
    public Map<Enchantment, Integer> getEnchantments() {return this.enchants;}
    public List<String> getBookPages() {return this.bookPages;}
    public String getBookAuthor() {return this.author;}
    public String getName() {return this.name;}
    public List<String> getLores() {return this.lores;}
    public String getTypeName() {
        return Base.capitalizeEachWord(this.getMaterial().name().replaceAll("_", " ").toLowerCase());
    }

    public boolean isAir() {return this.getID() < 1;}
    public boolean isBook() {return this.getMaterial().equals(Material.BOOK_AND_QUILL) || this.getMaterial().equals(Material.WRITTEN_BOOK);}

    public boolean hasData() {return this.data != DomsItem.BAD_DATA;}
    
    public void setID(int id) {this.id = id;}
    public void setData(short data) {this.data = data;}
    public void setLores(List<String> lores) {this.lores = lores;}
    public void setPages(List<String> pages) {this.bookPages = pages;}
    public void setAuthor(String author) {this.author = author;}
    public void setName(String name) {this.name = name;}
    public void setEnchantments(Map<Enchantment, Integer> e) {this.enchants = e;}

    public void setPage(int page, String l) {this.bookPages.set(page, l);}
    
    public Material getMaterial() {return Material.getMaterial(this.id);}
    public ItemMeta getItemMeta(ItemStack is) {
        ItemMeta im = is.getItemMeta();
        if(this.name != null && !this.name.equals("")) {
            im.setDisplayName(this.name);
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
        
        if(this.lores != null) {
            im.setLore(this.lores);
        }
        
        return im;
    }
    public ItemStack getItemStack() throws InvalidItemException {return getItemStack(64);}
    public ItemStack getItemStack(int amt) throws InvalidItemException {
        try {
            ItemStack is = new ItemStack(this.id, amt, this.data);
            is.setItemMeta(this.getItemMeta(is));
            if(this.enchants != null && this.enchants.size() > 0) {
                is.addUnsafeEnchantments(enchants);
            }
            return is;
        } catch(Exception e) {
            throw new InvalidItemException(this.toString());
        }
    }
    public Block setBlock(Block b) {
        b.setTypeIdAndData(id, (byte)data, true);
        return b;
    }
    
    public boolean compare(DomsItem item) {
        if(item.id != this.id) return false;
        if(this.data >= 0 && item.data >= 0) {
            if(this.data != item.data) return false;
        }
        
        if(this.name != null || item.name != null) {
            if(this.name == null || item.name == null) return false;
            if(!this.name.equals(item.name)) return false;
        }
        
        if(this.author != null || item.author != null) {
            if(this.author == null || item.author == null) return false;
            if(!this.author.equals(item.author)) return false;
        }
        
        if(this.lores != null || item.lores != null) {
            if(this.lores == null || item.lores == null) return false;
            if(this.lores.size() != item.lores.size()) return false;
            for(String s : this.lores) {
                if(!item.lores.contains(s)) return false;
            }
        }
        
        if(this.bookPages != null || item.bookPages != null) {
            if(this.bookPages == null || item.bookPages == null) return false;
            if(this.bookPages.size() != item.bookPages.size()) return false;
            for(String s : this.bookPages) {
                if(!item.bookPages.contains(s)) return false;
            }
        }
        
        if(this.enchants != null || item.enchants != null) {
            if(this.enchants == null || item.enchants == null) return false;
            if(this.enchants.size() != item.enchants.size()) return false;
            for(Enchantment e : this.enchants.keySet()) {
                if(!item.enchants.containsKey(e)) return false;
                if(item.enchants.get(e) != this.enchants.get(e)) return false;
            }
        }
        
        return true;
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
        String msg = "{id:\"" + this.id + "\"}";
        
        if(this.data >= 0) {
            msg += ",{data:\"" + Short.toString(this.data) + "\"}";
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
                msg += ",{enchantment: \"" + e.getName() + "*" + this.enchants.get(e) + "\"}";
            }
        }
        
        return msg;
    }

    public String toHumanString() {
        String s = this.getTypeName();
        
        if(this.data >= 0) {
            s += ", with type of " + this.data;
        }
        
        if(this.name != null && !this.name.equals("")) {
            s += ", named " + this.name;
        }
        
        if(this.enchants != null && this.enchants.size() > 0) {
            s += ", with the enchantments";
            for(Enchantment e : this.enchants.keySet()) {
                if(e == null) continue;
                s += ", " + Base.capitalizeEachWord(e.getName().replaceAll("_", " ").toLowerCase()) + " at level " + enchants.get(e)    ;
            }
        }
        
        if(this.lores != null && this.lores.size() > 0) {
            s += ", with the lores";
            for(String l : lores) {
                s += ", " + l;
            }
        }
        
        if(this.bookPages != null && this.bookPages.size() > 0) {
            s += ", and with the pages ";
            for(String p : this.bookPages) {
                s += ", \"" + p + "\"";
            }
        }
        
        if(this.author != null && !this.author.equals("")) {
            s += ", written by " + this.author;
        }
        
        return s;
    }
}
