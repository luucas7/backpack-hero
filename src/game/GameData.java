package game;

import java.util.List;
import dungeon.Enemies;
import dungeon.Healer;
import items.Armor;
import items.Arrow;
import items.Bow;
import items.Food;
import items.Gold;
import items.InstantHeal;
import items.Item;
import items.ItemShape;
import items.Key;
import items.Mana;
import items.MeleeWeapon;
import items.Potion;
import items.Shield;
import items.Staff;
import living.Effect;
import living.Enemy;
import living.PassiveDamageEnemy;
import living.VanillaEnemy;

public record GameData(ImageLoader loader) {

	public Item[] items() {
		ItemShape square2x2 = new ItemShape(new int[][] { { 1, 1 }, { 1, 1 } });
		ItemShape rectangle1x2 = new ItemShape(new int[][] { { 1 }, { 1 } });
		ItemShape rectangle2x1 = new ItemShape(new int[][] { { 1, 1 } });
		ItemShape rectangle1x3 = new ItemShape(new int[][] { { 1 }, { 1 }, { 1 } });
		ItemShape rectangle1x4 = new ItemShape(new int[][] { { 1 }, { 1 }, { 1 }, { 1 } });
		ItemShape square1x1 = new ItemShape(new int[][] { { 1 } });
		ItemShape croissant2x2 = new ItemShape(new int[][] { { 1, 1 }, { 0, 1 } });

		MeleeWeapon woodenBlade = new MeleeWeapon("Wooden Blade", rectangle1x2, 0, 7, 1,
				loader.image("Wooden_Blade.png"));
		MeleeWeapon woodenSword = new MeleeWeapon("Wooden Sword", rectangle1x3, 0, 7, 1,
				loader.image("Wooden_Sword.png"));
		MeleeWeapon heavyBlade = new MeleeWeapon("Heavy Blade", rectangle1x3, 1, 8, 1, loader.image("Heavy_Blade.png"));
		MeleeWeapon earthstoneBlade = new MeleeWeapon("Earthstone Blade", rectangle1x4, 3, 14, 1,
				loader.image("EarthStone_Blade.png"));
		
		Shield woodenShield = new Shield("Wooden Shield", square2x2, 0, 6, 1, loader.image("Wooden_Shield.png"));
		Shield captainShield = new Shield("Captain Shield", square1x1, 1, 6, 1, loader.image("Captain_Shield.png"));
		Bow mouseBow = new Bow("Mouse Bow", rectangle1x3, 1, 10, 1, loader.image("Mouse_Bow.png"));
		Arrow shortArrow = new Arrow("Short Arrow", square1x1, 0, 7, 2, loader.image("Short_Arrow.png"));
		Arrow egg = new Arrow("Egg", square1x1, 0, 3, 2, loader.image("Egg.png"));
		Mana manaOrb = new Mana(square1x1, 5, loader.image("Mana.png"));
		Staff noviceStaff = new Staff("Novice Staff", rectangle1x3, 1, 12, 1, 1, loader.image("Novice_Staff.png"));
		Armor cap = new Armor("Cap", square1x1, 0, 1, Effect.BLOCK, loader.image("Cap.png"));
		Armor ironHelmet = new Armor("Iron Helmet", rectangle1x2, 1, 2, Effect.BLOCK, loader.image("Iron_Helmet.png"));

		Armor diamondHelmet = new Armor("Diamond Helmet", square1x1, 3, 2, Effect.BLOCK,
				loader.image("Diamond_Helmet.png"));
		Armor diamondChestplate = new Armor("Diamond Chestplate", square2x2, 3, 4, Effect.BLOCK,
				loader.image("Diamond_Chestplate.png"));
		Armor diamondLeggings = new Armor("Diamond Leggings", square2x2, 3, 4, Effect.BLOCK,
				loader.image("Diamond_Leggings.png"));
		Armor diamondBoots = new Armor("Diamond Boots", square1x1, 3, 2, Effect.BLOCK,
				loader.image("Diamond_Boots.png"));

		Armor bronzeChestplate = new Armor("Bronze Chestplate", square2x2, 0, 2, Effect.BLOCK,
				loader.image("Bronze_Breastplate.png"));
		Armor leatherBoots = new Armor("Leather Boots", rectangle2x1, 0, 1, Effect.BLOCK,
				loader.image("Leather_Boots.png"));

		MeleeWeapon fishingHook = new MeleeWeapon("Fishing Hook", croissant2x2, 2, 20, 2,
				loader.image("Fishing_Hook.png"));
		Food apple = new Food("Apple", square1x1, 0, 5, 2, true, loader.image("Apple.png"));
		Food bread = new Food("Bread", square1x1, 0, 3, 3, true, loader.image("Bread.png"));
		Food cookie = new Food("Cookie", square1x1, 1, 3, 6, true, loader.image("Cookie.png"));
		Food strawberry = new Food("Strawberry", square1x1, 1, 2, 2, false, loader.image("Strawberry.png"));
		Food fungus = new Food("Fungus", square1x1, 0, 2, 2, false, loader.image("Fungus.png"));

		Potion staminaPotion = new Potion("Stamina Potion", square1x1, 1, 3, false, loader.image("Stamina_Potion.png"));
		Potion healthPotion = new Potion("Health Potion", square1x1, 1, 7, true, loader.image("Health_Potion.png"));

		List<Item> gameItems = List.of(woodenBlade, heavyBlade, woodenShield, mouseBow, shortArrow, manaOrb,
				noviceStaff, cap, ironHelmet, bronzeChestplate, leatherBoots, fishingHook, apple, bread, cookie,
				diamondHelmet, diamondChestplate, diamondLeggings, diamondBoots, captainShield, egg, earthstoneBlade,
				staminaPotion, healthPotion, strawberry, fungus);

		Healer.getHealingItem(new InstantHeal(square1x1, 5, loader.image("Health_Potion.png")));

		Key.initiateStaticKey(square1x1, loader.image("Key.png"));
		Gold.initiateStaticGold(square1x1, loader.image("Gold.png"));

		GameModel.setItemsData(gameItems);
		
		return new Item[] {woodenSword,Gold.create(),woodenShield.copy()};
	}

	public void enemies() {

		Enemy smallWolfRat = new VanillaEnemy("Small Wolf Rat", 20, 6, loader.image("RatWolf.png"));
		Enemy wolfRat = new VanillaEnemy("Wolf Rat", 25, 8, loader.image("RatWolf.png"));
		
		Enemy muskratBrigand = new VanillaEnemy("Muskrat Brigand", 30, 8, loader.image("MuskratBrigand.png"));
		Enemy badger = new VanillaEnemy("Badger", 30, 8, loader.image("Badger.png"));
		Enemy bat = new VanillaEnemy("Bat", 10, 3, loader.image("Bat.png"));
		Enemy crowBandit = new VanillaEnemy("Crow Bandit", 35, 6, loader.image("CrowBandit.png"));

		Enemy livingGhost = new VanillaEnemy("Living Ghost", 27, 3, loader.image("LivingGhost.png"));

		Enemy armadillo = new VanillaEnemy("Armadillo", 45, 9, loader.image("Armadillo.png"));
		Enemy queenBee = new PassiveDamageEnemy("Queen Bee", 45, 6, 2, Effect.POISON, loader.image("QueenBee.png"));
		
		Enemy cobra = new PassiveDamageEnemy("Cobra", 27, 2, 2, Effect.POISON, loader.image("Cobra.png"));
		Enemy beardedDragon = new PassiveDamageEnemy("Bearded Dragon", 33, 6, 3, Effect.BURN,
				loader.image("BeardedDragon.png"));
		Enemy bee = new PassiveDamageEnemy("Bee", 20, 4, 1, Effect.POISON, loader.image("Bee.png"));
		Enemy wizardFrog = new PassiveDamageEnemy("Wizard Frog", 20, 5, 1, Effect.POISON,
				loader.image("WizardToad.png"));
		
		Enemies.addEnemy(smallWolfRat, wolfRat, bee, muskratBrigand, badger, bat, crowBandit, cobra, beardedDragon,
				livingGhost, wizardFrog, queenBee,armadillo);
	}
}
