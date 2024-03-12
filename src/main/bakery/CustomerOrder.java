package bakery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class represents the Customer Order cards in the game, so have a recipe, an optional garnish, and a difficulty level associated with them.
 */
public class CustomerOrder
{
    private List<Ingredient> garnish;
    private int level;
    private String name;
    private List<Ingredient> recipe;
    private CustomerOrderStatus status;
    private static final long serialVersionUID =1;

    public enum CustomerOrderStatus
    {
        WAITING,
        FULFILLED,
        GARNISHED,
        IMPATIENT,
        GIVEN_UP
    }

    /**
     * 
     * @param name: The name of the customer order.
     * @param recipe: The list of ingredients needed to make this order.
     * @param garnish: The list of ingredients needed to make the optional garnish for a bonus.
     * @param level: The difficulty level of the order
     */
    public CustomerOrder(String name, List<Ingredient> recipe, List<Ingredient> garnish, int level)
    {
        if(recipe == null)
            throw new WrongIngredientsException("Cannot have a null recipe in constructor.");
        if(recipe.isEmpty())
            throw new WrongIngredientsException("Cannot have an empty recipe in constructor.");
        
        this.name = name;
        this.recipe = recipe;
        this.garnish = garnish;
        this.level = level;
    }

    /** 
     * Sets the status of the order to mark that it has not and cannot be fulfilled.
     */
    public void abandon()
    {
        status = CustomerOrderStatus.GIVEN_UP;
    }

    /**
     * This method will take a list of ingredients and see if it has enough ingredients/Helpful Ducks to make the recipe of this order.
     * @param ingredients: The list of ingredients being checked
     * @return boolean of if the the recipe can be made
     */
    public boolean canFulfill( List<Ingredient> ingredients)
    {
        return canMake(recipe, ingredients);
    }

    /**
     * This method will take a list of ingredients and see if it has enough ingredients/Helpful Ducks to make the garnish of this order. 
     * This does not look at if the recipe AND garnish can be made, just the garnish.
     * @param ingredients: The list of ingredients being checked
     * @return boolean of if the the garnish can be made
     */
    public boolean canGarnish( List<Ingredient> ingredients)
    {
        return canMake(garnish, ingredients);
    }
    private boolean canMake(List<Ingredient> thingToMake, List<Ingredient> ingredients)
    {
        HashMap<Ingredient, Integer> quantities = new HashMap<>();
        for(Ingredient ingredient: thingToMake) // this builds up the amount needed for each ingredients
        {
            if(!quantities.containsKey(ingredient))
                quantities.put(ingredient, 1);
            else
                quantities.replace(ingredient, quantities.get(ingredient) + 1);
        }

        int numDucks =0;

        for (Ingredient ingredient : ingredients) {
            if(quantities.containsKey(ingredient))
                quantities.replace(ingredient, quantities.get(ingredient) - 1);
            
            if(quantities.get(ingredient) == 0)
                quantities.remove(ingredient);
            if(ingredient.equals(Ingredient.HELPFUL_DUCK))
                numDucks++;
        } // so at the end of the loop it will have counted down all of the ingredients, so then we just have to see if they have enough helpful ducks.
        
        int count = 0;
        for (Ingredient ingredient : quantities.keySet()) { // return false if there is a Layer unaccounted for.
            if (ingredient instanceof Layer)
                return false;
        }
        for(Integer num: quantities.values()) // this gets the number of items in the recipe not accounted for by the normal ingredients.
        {
            count += num;
        }
            
        return numDucks >= count; // This will work if the normal ingredients cover it, since then it'll be 0 or more >= 0.
    }

    /**
     * This will attempt to fulfil the order, possibly with the garnish
     * @param ingredients: The list of ingredients being used.
     * @param garnish: This is a flag marking if the garnish is being made as well.
     * @return The list of ingredients used to fulfill the order, so counting Helpful Ducks if they were needed.
     * @throws WrongIngredientException if the ingredients passed 
     */
    public List<Ingredient> fulfill(List<Ingredient> ingredients, boolean garnish)
    {
        List<Ingredient>used = new ArrayList<>(); 
        
        if(canFulfill(ingredients))
        {
            
            for (Ingredient ingredient : recipe) 
            {
                if(ingredients.contains(ingredient))
                {
                    ingredients.remove(ingredient);
                    used.add(ingredient);
                }
                    
                else
                {
                    ingredients.remove(Ingredient.HELPFUL_DUCK);
                    used.add(Ingredient.HELPFUL_DUCK);
                }
            }
            if(garnish && canGarnish(ingredients))
            {
                for (Ingredient ingredient : this.garnish) 
                {
                    if(ingredients.contains(ingredient))
                    {
                        ingredients.remove(ingredient);
                        used.add(ingredient);
                    }
                    else
                    {
                        ingredients.remove(Ingredient.HELPFUL_DUCK);
                        used.add(Ingredient.HELPFUL_DUCK);
                    }
                        
                }
                status = CustomerOrderStatus.GARNISHED;   
            }
            else
                status = CustomerOrderStatus.FULFILLED;
        }
        else
            throw new WrongIngredientsException("Don't have the ingredients to fulfill the order");

        return used;
    }

    /**
     * The toString() function returns the name of the order.
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * 
     * @return the list of ingredients that make the garnish attribute
     */
    public List<Ingredient> getGarnish() 
    {
        return garnish;
    }

    /**
     * 
     * @return the difficulty level of this order
     */
    public int getLevel() 
    {
        return level;
    }

    /**
     * 
     * @return The list of ingredients that make the recipe attribute
     */
    public List<Ingredient> getRecipe() 
    {
        return recipe;
    }
    /**
     * 
     * @return the CustomerOrderStatus enum that marks the status of this order
     */
    public CustomerOrderStatus getStatus() 
    {
        return status;
    }

    /**
     * 
     * @param status The CustomerOrderStatus to set this order to.
     */
    public void setStatus(CustomerOrderStatus status) 
    {
        this.status = status;
    }
    /**
     * 
     * @return a string giving a comma separated list of ingredients in the recipe:
     * e.g. "Chocolate, Eggs (x2), Sugar"
     */
    public String getRecipeDescription()
    {
        return IngredientListUtil.stringFromIngList(recipe);
    }
    /**
     * 
     * @return a string giving a comma separated list of ingredients in the garnish:
     * e.g. "Chocolate, Eggs (x2), Sugar"
     */
    public String getGarnishDescription()
    {
        return IngredientListUtil.stringFromIngList(garnish);
    }
    // private String ingredientListToString(List<Ingredient> list)
    // {
    //     String csList = "";
    //     for( Ingredient ing: list)
    //     {
    //         csList += ing.toString() + ", ";
    //     }
    //     csList =  csList.substring(0, csList.length() -2);
    //     return csList;
    // }

}
