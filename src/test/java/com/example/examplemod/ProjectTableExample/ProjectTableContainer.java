package com.example.examplemod.ProjectTableExample;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ProjectTableContainer extends Container {

    /** The crafting matrix inventory (3x3). */
    public InventoryCrafting craftMatrix = new InventoryCrafting(this, 1, 32);
    public IInventory craftResult = new InventoryCraftResult();
    private World worldObj;
    /** Position of the workbench */
    private BlockPos pos;

    public ProjectTableContainer(InventoryPlayer playerInventory) {

        addPlayerInventory(playerInventory, 8, 145);
        addSlotToContainer(new ProjectTableCraftingSlot(playerInventory.player, craftMatrix, craftResult, 0));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    private static class ProjectTableCraftingSlot extends SlotCrafting
    {

        public ProjectTableCraftingSlot(EntityPlayer player, InventoryCrafting craftingMaterials, IInventory craftingOutput, int slotIndex)
        {
            super(player, craftingMaterials, craftingOutput, slotIndex, 0, 0);
        }

        @Override
        public ItemStack func_190901_a(EntityPlayer playerIn, ItemStack stack)
        {
            onCrafting(stack);
            return stack;
        }
    }

    private static final int PLAYER_INVENTORY_ROWS = 3;
    private static final int PLAYER_INVENTORY_COLUMNS = 9;

    private static boolean isSlotInRange(int slotIndex, int slotMin, int slotMax, boolean ascending)
    {
        return ascending ? slotIndex >= slotMin : slotIndex < slotMax;
    }

    private static boolean equalsIgnoreStackSize(ItemStack itemStack1, ItemStack itemStack2)
    {
        if (itemStack1 != null && itemStack2 != null)
        {
            if (Item.getIdFromItem(itemStack1.getItem()) - Item.getIdFromItem(itemStack2.getItem()) == 0)
            {
                //noinspection ObjectEquality
                if (itemStack1.getItem() == itemStack2.getItem())
                {
                    if (itemStack1.getItemDamage() == itemStack2.getItemDamage() &&
                            areItemStackTagsEqual(itemStack1, itemStack2)) return true;
                }
            }
        }

        return false;
    }

    private static boolean areItemStackTagsEqual(ItemStack itemStack1, ItemStack itemStack2)
    {
        if (itemStack1.hasTagCompound() && itemStack2.hasTagCompound())
        {
            if (ItemStack.areItemStackTagsEqual(itemStack1, itemStack2))
            {
                return true;
            }
        } else
        {
            return true;
        }
        return false;
    }

    private static ItemStack cloneItemStack(ItemStack itemStack, int stackSize)
    {
        final ItemStack clonedItemStack = itemStack.copy();
        clonedItemStack.func_190920_e(stackSize);
        return clonedItemStack;
    }

    @SuppressWarnings({"MethodWithMultipleLoops", "OverlyLongMethod", "OverlyComplexMethod"})
    @Override
    protected boolean mergeItemStack(ItemStack itemStack, int slotMin, int slotMax, boolean ascending)
    {
        boolean slotFound = false;

        if (itemStack.isStackable())
        {
            int currentSlotIndex = ascending ? slotMax - 1 : slotMin;
            while (itemStack.func_190916_E() > 0 && isSlotInRange(currentSlotIndex, slotMin, slotMax, ascending))
            {
                final Slot slot = inventorySlots.get(currentSlotIndex);
                final ItemStack stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && equalsIgnoreStackSize(itemStack, stackInSlot))
                {
                    final int combinedStackSize = stackInSlot.func_190916_E() + itemStack.func_190916_E();
                    final int slotStackSizeLimit = Math.min(stackInSlot.getMaxStackSize(), slot.getSlotStackLimit());

                    if (combinedStackSize <= slotStackSizeLimit)
                    {
                        itemStack.func_190920_e(0);
                        stackInSlot.func_190920_e(combinedStackSize);
                        slot.onSlotChanged();
                        slotFound = true;
                    } else if (stackInSlot.func_190916_E() < slotStackSizeLimit)
                    {
                        itemStack.func_190918_g(slotStackSizeLimit - stackInSlot.func_190916_E());
                        stackInSlot.func_190920_e(slotStackSizeLimit);
                        slot.onSlotChanged();
                        slotFound = true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        if (itemStack.func_190916_E() > 0)
        {
            int currentSlotIndex = ascending ? slotMax - 1 : slotMin;

            while (isSlotInRange(currentSlotIndex, slotMin, slotMax, ascending))
            {
                final Slot slot = inventorySlots.get(currentSlotIndex);
                final ItemStack stackInSlot = slot.getStack();

                if (slot.isItemValid(itemStack) && stackInSlot == null)
                {
                    slot.putStack(cloneItemStack(itemStack, Math.min(itemStack.func_190916_E(), slot.getSlotStackLimit())));
                    slot.onSlotChanged();

                    if (slot.getStack() != null)
                    {
                        itemStack.func_190918_g(slot.getStack().func_190916_E());
                        return true;
                    }
                }

                currentSlotIndex += ascending ? -1 : 1;
            }
        }

        return slotFound;
    }

    void addPlayerInventory(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int inventoryRowIndex = 0; inventoryRowIndex < PLAYER_INVENTORY_ROWS; ++inventoryRowIndex)
        {
            addInventoryRowSlots(playerInventory, xOffset, yOffset, inventoryRowIndex);
        }

        addActionBarSlots(playerInventory, xOffset, yOffset);
    }

    private void addInventoryRowSlots(InventoryPlayer playerInventory, int xOffset, int yOffset, int rowIndex)
    {
        for (int inventoryColumnIndex = 0; inventoryColumnIndex < PLAYER_INVENTORY_COLUMNS; ++inventoryColumnIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, inventoryColumnIndex + rowIndex * 9 + 9, xOffset + inventoryColumnIndex * 18, yOffset + rowIndex * 18));
        }
    }

    private void addActionBarSlots(InventoryPlayer playerInventory, int xOffset, int yOffset)
    {
        for (int actionBarSlotIndex = 0; actionBarSlotIndex < 9; ++actionBarSlotIndex)
        {
            //noinspection ObjectAllocationInLoop
            addSlotToContainer(new Slot(playerInventory, actionBarSlotIndex, xOffset + actionBarSlotIndex * 18, yOffset + 58));
        }
    }

    boolean didTransferStackInStandardSlot(int slotIndex, ItemStack slotItemStack, int indexFirstStdSlot)
    {
        if (slotIndex >= indexFirstStdSlot && slotIndex < inventorySlots.size() - 9)
        {
            if (!mergeItemStack(slotItemStack, inventorySlots.size() - 9, inventorySlots.size(), false))
            {
                return true;
            }
        } else if (slotIndex >= inventorySlots.size() - 9 && slotIndex < inventorySlots.size())
        {
            if (!mergeItemStack(slotItemStack, indexFirstStdSlot, inventorySlots.size() - 9, false))
            {
                return true;
            }
        }
        return false;
    }
}
