package com.skyresourcesclassic.jei.freezer;

import com.skyresourcesclassic.References;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import java.util.List;

public class FreezerRecipeCategory implements IRecipeCategory {
    private static final int slotInputStack = 0;
    private static final int slotOutput = 1;
    public static final String UUID = References.ModID + ":freezer";

    private final IDrawable background;

    private final String localizedName = I18n.translateToLocalFormatted("jei.skyresourcesclassic.recipe.freezer");

    public FreezerRecipeCategory(IGuiHelper guiHelper) {
        super();
        background = guiHelper.createDrawable(
                new ResourceLocation(References.ModID, "textures/gui/jei/concentrator.png"), 0, 40, 90, 40);
    }

    @Override
    public void drawExtras(Minecraft minecraft) {
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public String getUid() {
        return UUID;
    }

    @Override
    public void setRecipe(IRecipeLayout layout, IRecipeWrapper wrapper, IIngredients ingredients) {
        layout.getItemStacks().init(slotInputStack, true, 1, 5);
        layout.getItemStacks().init(slotOutput, false, 64, 5);

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        layout.getItemStacks().set(slotInputStack, inputs.get(0));
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);
        layout.getItemStacks().set(slotOutput, outputs.get(0));
    }

    @Override
    public String getModName() {
        return References.ModName;
    }

}
