package com.skyresourcesclassic.technology.gui;

import com.skyresourcesclassic.RandomHelper;
import com.skyresourcesclassic.References;
import com.skyresourcesclassic.base.gui.GuiHelper;
import com.skyresourcesclassic.technology.gui.container.ContainerWildlifeAttractor;
import com.skyresourcesclassic.technology.tile.TileWildlifeAttractor;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class GuiWildlifeAttractor extends GuiContainer {

    private IInventory playerInv;
    private TileWildlifeAttractor tile;

    public GuiWildlifeAttractor(IInventory playerInv, TileWildlifeAttractor te) {
        super(new ContainerWildlifeAttractor(playerInv, te));

        this.playerInv = playerInv;
        this.tile = te;

        this.xSize = 176;
        this.ySize = 189;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager()
                .bindTexture(new ResourceLocation(References.ModID, "textures/gui/wildlife_attractor.png"));
        this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.ModID, "textures/gui/gui_icons.png"));
        if (tile.getEnergyStored() > 0) {
            int height = tile.getEnergyStored() * 58 / tile.getMaxEnergyStored();
            this.drawTexturedModalRect(22 + guiLeft, 30 + 58 - height + guiTop, 51, 59 - height, 8, height);
        }

        RandomHelper.renderGuiTank(tile.getTank().getFluid(), tile.getTank().getCapacity(),
                tile.getTank().getFluidAmount(), 142 + guiLeft, 29 + guiTop, 16, 59);

        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.ModID, "textures/gui/gui_icons.png"));
        this.drawTexturedModalRect(142 + guiLeft, 30 + guiTop, 34, 0, 16, 59);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String s = tile.getDisplayName().getUnformattedComponentText();
        this.fontRenderer.drawString(s, 88 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
        this.fontRenderer.drawString(this.playerInv.getDisplayName().getUnformattedComponentText(), 3, 96, 4210752);


        GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(new ResourceLocation(References.ModID, "textures/gui/gui_icons.png"));
        this.drawTexturedModalRect(81, 43, 85, 0, 14, 13);
        int burn = (int) ((float) tile.getMatterLeft() * 13F
                / (float) 320);
        this.drawTexturedModalRect(81, 56 - burn, 59, 13 - burn, 14, burn);

        if (GuiHelper.isMouseInRect(142 + guiLeft, 30 + guiTop, 16, 59, mouseX, mouseY)) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            List list = new ArrayList<>();
            list.add("Water:");
            list.add(tile.getTank().getFluidAmount() + " mB / " + tile.getTank().getCapacity() + " mB");
            this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
        } else if (GuiHelper.isMouseInRect(22 + guiLeft, 30 + guiTop, 8, 59, mouseX, mouseY)) {
            int k = (this.width - this.xSize) / 2;
            int l = (this.height - this.ySize) / 2;
            List list = new ArrayList<>();
            list.add(TextFormatting.RED + "Power:");
            list.add(TextFormatting.RED + (tile.getEnergyStored() + " RF / " + tile.getMaxEnergyStored() + " RF"));
            this.drawHoveringText(list, mouseX - k, mouseY - l, fontRenderer);
        }

    }
}