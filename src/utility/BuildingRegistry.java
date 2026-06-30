package utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.buildings.Bed;
import entity.buildings.Bin;
import entity.buildings.Breaker;
import entity.buildings.Building;
import entity.buildings.Calendar;
import entity.buildings.Candle;
import entity.buildings.Cauldron;
import entity.buildings.ChefPortrait;
import entity.buildings.ChoppingBoard;
import entity.buildings.ClothesRail;
import entity.buildings.Computer;
import entity.buildings.CornerTable;
import entity.buildings.CursedDecor;
import entity.buildings.Door;
import entity.buildings.EscapeHole;
import entity.buildings.Fireplace;
import entity.buildings.FloorDecor_Building;
import entity.buildings.FoodStore;
import entity.buildings.Freezer;
import entity.buildings.FreezerLight;
import entity.buildings.Fridge;
import entity.buildings.FryingStation;
import entity.buildings.Gate;
import entity.buildings.HerbBasket;
import entity.buildings.KitchenCounter;
import entity.buildings.Lantern;
import entity.buildings.LargeTable;
import entity.buildings.MenuBook;
import entity.buildings.MenuSign;
import entity.buildings.Oven;
import entity.buildings.RoomSpawn;
import entity.buildings.Rubble;
import entity.buildings.Shelf;
import entity.buildings.Sink;
import entity.buildings.SoulLantern;
import entity.buildings.SpiceTable;
import entity.buildings.Stairs;
import entity.buildings.StorageFridge;
import entity.buildings.Stove;
import entity.buildings.Table;
import entity.buildings.TipJar;
import entity.buildings.Toilet;
import entity.buildings.ToiletDoor;
import entity.buildings.Torch;
import entity.buildings.Trapdoor;
import entity.buildings.Turntable;
import entity.buildings.WallDecor_Building;
import entity.buildings.Window;
import entity.items.Food;
import main.GamePanel;
import utility.save.BuildingSaveData;

public class BuildingRegistry {

    @FunctionalInterface
    interface BuildingFactory {
        Building create(int x, int y, int preset);
    }

    private final Map<String, BuildingFactory> registry = new HashMap<>();
    private final GamePanel gp;

    public BuildingRegistry(GamePanel gp) {
        this.gp = gp;

        // ---- No preset ----
        registry.put("Bed",             (x, y, p) -> new Bed(gp, x, y));
        registry.put("Breaker",         (x, y, p) -> new Breaker(gp, x, y));
        registry.put("Calendar",        (x, y, p) -> new Calendar(gp, x, y));
        registry.put("Candle",        (x, y, p) -> new Candle(gp, x, y, p));
        registry.put("Cauldron",        (x, y, p) -> new Cauldron(gp, x, y));
        registry.put("Clothes Rail",    (x, y, p) -> new ClothesRail(gp, x, y));
        registry.put("Escape Hole",     (x, y, p) -> new EscapeHole(gp, x, y));
        registry.put("Lantern",         (x, y, p) -> new Lantern(gp, x, y));
        registry.put("Menu Sign",       (x, y, p) -> new MenuSign(gp, x, y));
        registry.put("Oven",            (x, y, p) -> new Oven(gp, x, y));
        registry.put("Kitchen Sink 1",  (x, y, p) -> new Sink(gp, x, y));
        registry.put("Menu Book",       (x, y, p) -> new MenuBook(gp, x, y));
        registry.put("Soul Lantern",    (x, y, p) -> new SoulLantern(gp, x, y));
        registry.put("Stove",           (x, y, p) -> new Stove(gp, x, y));
        registry.put("Frying Station",  (x, y, p) -> new FryingStation(gp, x, y));
        registry.put("Chopping Board",  (x, y, p) -> new ChoppingBoard(gp, x, y));
        registry.put("Turntable",       (x, y, p) -> new Turntable(gp, x, y));
        registry.put("Tip Jar",         (x, y, p) -> new TipJar(gp, x, y));
        registry.put("Herb Basket",     (x, y, p) -> new HerbBasket(gp, x, y));
        registry.put("Room Spawn",      (x, y, p) -> new RoomSpawn(gp, x, y));
        registry.put("Chef Portrait",   (x, y, p) -> new ChefPortrait(gp, x, y, p));
        registry.put("Torch",           (x, y, p) -> new Torch(gp, x, y));
        registry.put("Computer",        (x, y, p) -> new Computer(gp, x, y));
        registry.put("Fireplace",       (x, y, p) -> new Fireplace(gp, x, y));
        registry.put("Stairs",          (x, y, p) -> new Stairs(gp, x, y));
        registry.put("Freezer",         (x, y, p) -> new Freezer(gp, x, y));
        registry.put("Freezer Light",   (x, y, p) -> new FreezerLight(gp, x, y));
        registry.put("Spice Table",     (x, y, p) -> new SpiceTable(gp, x, y));

        // ---- With preset ----
        registry.put("Table Corner 1",  (x, y, p) -> new CornerTable(gp, x, y, p));
        registry.put("Food Store",      (x, y, p) -> new FoodStore(gp, x, y, p));
        registry.put("Toilet 1",        (x, y, p) -> new Toilet(gp, x, y, p));
        registry.put("Toilet Door 1",   (x, y, p) -> new ToiletDoor(gp, x, y, p));
        registry.put("Trapdoor 1",      (x, y, p) -> new Trapdoor(gp, x, y, p));
        registry.put("Shelf",           (x, y, p) -> new Shelf(gp, x, y, p));
        registry.put("Kitchen Counter", (x, y, p) -> new KitchenCounter(gp, x, y, p));
        registry.put("Gate",            (x, y, p) -> new Gate(gp, x, y, p));
        registry.put("Window",          (x, y, p) -> new Window(gp, x, y, p));
        registry.put("Large Table",     (x, y, p) -> new LargeTable(gp, x, y, p));
        registry.put("Bin 1",           (x, y, p) -> new Bin(gp, x, y, p));

        // ---- Decor ----
        registry.put("Floor Decor",     (x, y, p) -> new FloorDecor_Building(gp, x, y, p));
        registry.put("Wall Decor",      (x, y, p) -> new WallDecor_Building(gp, x, y, p));
        registry.put("Cursed Decor",    (x, y, p) -> new CursedDecor(gp, x, y, p));
        registry.put("Rubble",          (x, y, p) -> new Rubble(gp, x, y, p));

        // ---- Special cases handled via saveTo/loadFrom ----
        registry.put("Door 1",          (x, y, p) -> new Door(gp, x, y, 0, p));
        registry.put("Fridge",          (x, y, p) -> new Fridge(gp, x, y));
        registry.put("Storage Fridge",  (x, y, p) -> new StorageFridge(gp, x, y, false));
        registry.put("Table 1",         (x, y, p) -> new Table(gp, x, y, null, false));
    }

    public Building create(String name, int x, int y) {
        return create(name, x, y, -1);
    }

    public Building create(String name, int x, int y, int preset) {
        BuildingFactory factory = registry.get(name);
        //if (factory == null) throw new IllegalArgumentException("Unknown building: " + name);
        if (factory == null) return null;
        return factory.create(x, y, preset);
    }

    // ----------------------------------------------------------------
    //  SAVE
    // ----------------------------------------------------------------
    public List<BuildingSaveData> saveBuildings(List<Building> buildings) {
        List<BuildingSaveData> saved = new ArrayList<>();
        for (Building b : buildings) {
            if (b == null) continue;

            BuildingSaveData data = new BuildingSaveData();
            data.name   = b.getRegistryName();
            data.x      = (int) b.hitbox.x;
            data.y      = (int) b.hitbox.y;
            data.preset = b.getPreset(); // works for everything now

            // Only keep special cases that have EXTRA data beyond preset
            if (b instanceof Rubble c) {
                data.attribute1 = c.barricade ? 1 : 0;
            } else if (b instanceof Door d) {
                data.attribute1 = d.facing;
                data.attribute2 = d.doorRoomNum;
            } else if (b instanceof Fridge d) {
                data.fridgeType = 0;
                for (Food f : d.getContents()) {
                    data.fridgeContents.add(f.getName());
                    data.fridgeContentStates.add(f.getState());
                }
            } else if (b instanceof StorageFridge d) {
                data.fridgeType = 1;
                data.attribute1 = d.starterFridge ? 1 : 0;
                for (Food f : d.contents) {
                    data.fridgeContents.add(f.getName());
                    data.fridgeContentStates.add(f.getState());
                }
            } else if (b instanceof Table c) {
                data.string1  = c.chairFacing;
                data.boolean1 = c.doubleChaired;
            }

            saved.add(data);
        }
        return saved;
    }

    // ----------------------------------------------------------------
    //  LOAD
    // ----------------------------------------------------------------
    public List<Building> unpackSavedBuildings(List<BuildingSaveData> savedBuildings) {
        List<Building> buildings = new ArrayList<>();

        for (BuildingSaveData data : savedBuildings) {
            Building b = create(data.name, data.x, data.y, data.preset);
            if (b == null) continue;

            if (b instanceof Rubble r && data.attribute1 == 1) {
                r.setBarricade();
            } else if (b instanceof Door d) {
                d.facing = data.attribute1;
                d.setDoorNum(data.attribute2);
                d.importImages();
            } else if (b instanceof Fridge d) {
                d.setContents(data.fridgeContents, data.fridgeContentStates);
                d.importImages();
            } else if (b instanceof StorageFridge d) {
                d.starterFridge = data.attribute1 == 1;
                d.setContents(data.fridgeContents, data.fridgeContentStates);
                d.importImages();
            } else if (b instanceof Table t) {
                t.chairFacing   = data.string1;
                t.doubleChaired = data.boolean1;
                t.importImages();
            }

            buildings.add(b);
        }

        return buildings;
    }
}