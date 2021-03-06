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

public enum ModGuis
{
    PROJECT_TABLE;

    private static final ModGuis[] cache = values();

    public int getID()
    {
        // Not used for persistent data, so ordinal is perfect here!
        return ordinal();
    }

    public static ModGuis fromId(int id)
    {
        return cache[id];
    }
}
