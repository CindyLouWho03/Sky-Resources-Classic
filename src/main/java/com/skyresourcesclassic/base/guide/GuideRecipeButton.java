package com.skyresourcesclassic.base.guide;

import com.skyresourcesclassic.jei.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;

import java.util.ArrayList;
import java.util.List;

public class GuideRecipeButton extends GuidePageButton {

    public GuideRecipeButton(ItemStack stack) {
        super(stack.getDisplayName(), stack);
    }

    @Override
    public String getIdentifier() {
        return "recipe";
    }

    @Override
    public boolean onClicked() {
        if (Loader.isModLoaded("jei")) {
            JEIPlugin.openRecipesGui(this.stackDisplay);
        }
        return true;
    }

    @Override
    public String getDisplay() {
        return getItemDisplay().getDisplayName();
    }

    @Override
    public void setArguments(String[] allArgs) {
    }

    @Override
    public GuidePageButton clone() {
        return new GuideRecipeButton(this.stackDisplay);
    }
}
