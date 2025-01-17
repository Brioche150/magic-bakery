package bakery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * This class is just here to give the method for converting a list of ingredients to a pretty string, because this 
 * is a common requirement for classes that don't inherit.
 * 
 * Another way to do this would be to make a wrapper class for List<Ingredient> that does this specifically, but that would slightly change the attributes of existing parts of the UML diagram.
 * @author Antony Salta
 * @version 1.1
 *
 * This isn't the correct version number, but I haven't been tracking until now
 */
public abstract class IngredientListUtil {
    /**
     * 
     * @param list
     * A list is inputted, will then be sorted.
     * This list will be used to generate a comma separated string of the items in the list.
     * @param capitalised setting whether the items should be capitalised or not
     * @param sorted setting whether the itemse should be sorted or not
     * @return csList
     * the string csList, comma separated list.
     * This list will mark duplicate ingredients instead of outputting it twice. 
     * e.g. it will be: "Chocolate, Eggs (x2), Sugar"
     * instead of "Chocolate, Eggs, Eggs, Sugar"
     */
    protected static String stringFromIngList(List<Ingredient> list, boolean capitalised, boolean sorted)
    { //TODO: fix this to count duplicates even if it's not sorted.
        if(list == null || list.isEmpty())
            return ""; // This should basically just come up when passing in the garnish for a customerOrder that doesn't have a garnish.
            
        List<Ingredient> copy = new ArrayList<>(list); //Make sure it doesn't sort the list passed in, since some don't want that.
        if(sorted)
            Collections.sort(copy);

        String csList = "";
        int numSame = 0;
        // e.g. c, e, e, e , s
        HashMap<Ingredient, Integer> quantities = new HashMap<>();
        for( Ingredient ing: copy)
        {
            if(quantities.containsKey(ing))
                quantities.put(ing, quantities.get(ing) +1);
            else
                quantities.put(ing, 1);
        }
        for (Ingredient ing: copy)
        {
            String name;
            String original = ing.toString();
            if(csList.toLowerCase().contains(original.toLowerCase())) // So skip if it's already been put in.
                continue;
            if(capitalised)
            {
                name = original.substring(0,1).toUpperCase();
                name += original.substring(1);
            }
            else
                name = original;

            csList += name;
            if(quantities.get(ing) > 1)
                csList += " (x" + quantities.get(ing) + ")";
            csList += ", ";
        }
        csList = csList.substring(0,csList.length()-2);
        return csList;
    }
}
    