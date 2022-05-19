package dev.jadss.jadapi.bukkitImpl.misc;

import dev.jadss.jadapi.JadAPIPlugin;
import dev.jadss.jadapi.bukkitImpl.enums.JVersion;
import dev.jadss.jadapi.bukkitImpl.item.JItemStack;
import dev.jadss.jadapi.bukkitImpl.item.JMaterial;
import dev.jadss.jadapi.bukkitImpl.sub.BukkitUtils;
import dev.jadss.jadapi.utils.JReflection;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class JShapedCraft {

    private final JadAPIPlugin plugin;
    private final String recipeName;

    private ShapedRecipe recipe;

    private JItemStack result;
    private String[] shape;
    private Map<Character, JMaterial> ingredients = new HashMap<>();

    /**
     * Create a ShapedCraft!
     * @param plugin The plugin that is registering this craft.
     * @param recipeName the name of the recipe!
     */
    public JShapedCraft(JadAPIPlugin plugin, String recipeName) {
        this.plugin = plugin;
        this.recipeName = recipeName;
    }

    /**
     * Set the result of this craft.
     *
     * @param result The result of this craft.
     * @return itself.
     */
    public JShapedCraft setResultItem(JItemStack result) {
        this.result = result;
        return this;
    }

    /**
     * Get the result of this craft.
     *
     * @return The result of this craft.
     */
    public JItemStack getResultItem() {
        return result;
    }

    /**
     * Sets the shape of the craft.
     * <p>Example of a shape:</p>
     * <p>Row 1 - LEL</p>
     * <p>Row 2 - EGE</p>
     * <p>Row 3 - LEL</p>
     * <p>The letters represent the ingredients.</p>
     * <p>Where L means Iron block</p>
     * <p>E means Emerald Block</p>
     * <p>G means Glass block</p>
     * <p>And this could give the special item for a plugin, for example!</p>
     *
     * @param row1 represents the first row of the crafting table space.
     * @param row2 represents the second row of the crafting table space.
     * @param row3 represents the third row of the crafting table space.
     * @return itself.
     */
    public JShapedCraft setShape(String row1, String row2, String row3) {
        if (row1.length() != 3 || row2.length() != 3 || row3.length() != 3) {
            throw new IllegalArgumentException("The shape must be 3 lenght for each row of the crafting table.!");
        }
        this.shape = new String[]{row1, row2, row3};
        return this;
    }

    /**
     * Gets the shape of this craft.
     * <p>Every string in the array represents an array, so the first string is row1, second row2, third row3!</p>
     *
     * @return The shape of this craft.
     */
    public String[] getShape() {
        return shape;
    }

    /**
     * Add an ingredient to this craft.
     *
     * @param character The character of the ingredient.
     * @param material  The material of the ingredient.
     * @return itself.
     */
    public JShapedCraft addIngredient(Character character, JMaterial material) {
        ingredients.put(character, material);
        return this;
    }

    /**
     * Set the ingredients of this craft.
     *
     * @param ingredients The ingredients of this craft.
     * @return itself.
     */
    public JShapedCraft setIngredients(Map<Character, JMaterial> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    /**
     * Gets the ingredients of this craft.
     *
     * @return The ingredients list reference of this craft!
     */
    public Map<Character, JMaterial> getIngredients() {
        return ingredients;
    }


    /**
     * Register this craft to the server.
     *
     * @param register if we should register it.
     */
    public void register(boolean register) {
        if (result == null || shape == null || ingredients == null)
            throw new IllegalStateException("You must set the result, shape, and ingredients before registering!");

        if (register) {
            if (plugin.getCrafts().contains(this))
                throw new IllegalStateException("This craft is already registered!");

            //register it.
            this.recipe = createRecipe(result, plugin.getJavaPlugin(), recipeName).shape(shape[0], shape[1], shape[2]);
            this.ingredients.entrySet().forEach(entry -> recipe.setIngredient(entry.getKey(), entry.getValue().getMaterial(JMaterial.Type.ITEM).getKey()));
            Bukkit.addRecipe(recipe);
            plugin.getCrafts().add(this);
        } else {
            throw new UnsupportedOperationException("Unregistering is not supported yet!");
        }
    }

    private static ShapedRecipe createRecipe(JItemStack result, Plugin plugin, String recipeName) {
        if (JVersion.getServerVersion().isNewerOrEqual(JVersion.v1_13)) {
            //updated
            return (ShapedRecipe) JReflection.executeConstructor(ShapedRecipe.class, new Class[]{JReflection.getReflectionClass("org.bukkit.NamespacedKey"), ItemStack.class}, BukkitUtils.buildNamespacedKey(plugin, recipeName), result.buildItemStack());
        } else {
            //outdated
            return (ShapedRecipe) JReflection.executeConstructor(ShapedRecipe.class, new Class[]{ItemStack.class}, result.buildItemStack());
        }
    }

    /**
     * Get the result of this craft.
     *
     * @return The result of this craft, null if not registered.
     */
    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
