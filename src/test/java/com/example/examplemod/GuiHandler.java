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

package com.example.examplemod;

import com.example.examplemod.ProjectTableExample.ProjectTableContainer;
import com.example.examplemod.ProjectTableExample.ProjectTableGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public enum GuiHandler implements IGuiHandler
{
    INSTANCE;

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        final ModGuis gui = ModGuis.fromId(id);
        switch(gui)
        {
            case PROJECT_TABLE:
                return new ProjectTableContainer(player.inventory);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
        final ModGuis gui = ModGuis.fromId(id);
        switch(gui)
        {
            case PROJECT_TABLE:
                return new ProjectTableGui(player.inventory);
        }
        return null;
    }
}
