/*
 * Copyright (c) 2014 Rosie Alexander and Scott Killen.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses>.
 */

package mod.steamnsteel.mcgui.client.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import org.lwjgl.input.Mouse;
import java.io.IOException;

public abstract class McGUIContainerScreen extends GuiContainer
{
    private static final int TEXT_COLOR = 4210752;

    private static final String INVENTORY = "container.inventory";

    private final McGUIMixin mixin;
    private ControlBase rootControl = null;

    protected McGUIContainerScreen(Container container)
    {

        super(container);
        mixin = new McGUIMixin();
    }

    protected abstract ResourceLocation getResourceLocation(String path);

    protected abstract String getInventoryName();

    public final void setRootControl(ControlBase rootControl) {
        this.rootControl = rootControl;
        mixin.rootControl = rootControl;
    }

    protected final void addChild(ControlBase childControl) {
        rootControl.addChild(childControl);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        if (rootControl == null) return;

        final int xStart = (width - xSize) / 2;
        final int yStart = (height - ySize) / 2;

        rootControl.setLocation(xStart, yStart);
        rootControl.draw();
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (rootControl == null) return;

        final int eventX = Mouse.getEventX() * width / mc.displayWidth;
        final int eventY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        final int eventButton = Mouse.getEventButton();
        final boolean isTouchscreen = mc.gameSettings.touchscreen;

        mixin.handleMouseInput(eventX, eventY, eventButton, isTouchscreen);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseZ)
    {
        final String name = I18n.translateToLocal(getInventoryName());
        fontRendererObj.drawString(name, xSize / 2 - fontRendererObj.getStringWidth(name) / 2, 6, TEXT_COLOR);
        fontRendererObj.drawString(I18n.translateToLocal(INVENTORY), 8, ySize - 96 + 2, TEXT_COLOR);
    }
}
