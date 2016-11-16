package com.example.examplemod.ProjectTableExample;

import com.google.common.collect.ImmutableList;
import mod.steamnsteel.mcgui.client.gui.GuiRenderer;
import mod.steamnsteel.mcgui.client.gui.GuiTexture;
import mod.steamnsteel.mcgui.client.gui.IGuiTemplate;
import mod.steamnsteel.mcgui.client.gui.IModelView;
import mod.steamnsteel.mcgui.client.gui.controls.ButtonControl;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class ProjectTableRecipeControl extends ButtonControl implements IGuiTemplate<ProjectTableRecipeControl>, IModelView<ProjectTableRecipe>
{
    private final GuiTexture craftableTexture;
    private final GuiTexture uncraftableTexture;
    private ProjectTableRecipe recipe = null;

    public ProjectTableRecipeControl(GuiRenderer guiRenderer, GuiTexture craftableTexture, GuiTexture uncraftableTexture)
    {
        super(guiRenderer, new Rectangle(0, 0, craftableTexture.getBounds().getWidth(), craftableTexture.getBounds().getHeight()));
        this.craftableTexture = craftableTexture;
        this.uncraftableTexture = uncraftableTexture;

        setDefaultTexture(craftableTexture);
        setDisabledTexture(uncraftableTexture);
        setHoverTexture(craftableTexture);
        setPressedTexture(uncraftableTexture);
    }

    @Override
    public void draw() {
        if (recipe == null) { return; }
        super.draw();

        GlStateManager.enableRescaleNormal();
        final ImmutableList<ItemStack> output = recipe.getOutput();
        final ItemStack outputItemStack = output.get(0);
        if (output.size() == 1 && outputItemStack.getItem() != null)
        {
            RenderHelper.enableGUIStandardItemLighting();
            getGuiRenderer().renderItem(this, outputItemStack, 2, 3);
            RenderHelper.disableStandardItemLighting();

            if (outputItemStack.func_190916_E() > 1)
            {
                final String craftedItemCount = String.format("%d", outputItemStack.func_190916_E());
                final int textWidth = getGuiRenderer().getStringWidth(craftedItemCount);

                GlStateManager.depthFunc(GL11.GL_ALWAYS);
                getGuiRenderer().drawStringWithShadow(this, craftedItemCount, 16 - textWidth + 2, 12, 16777215);
                GlStateManager.depthFunc(GL11.GL_LEQUAL);

            }
            getGuiRenderer().drawStringWithShadow(this, recipe.getDisplayName(), 2 + 20, 8, 16777215);
        }

        final int inputItemCount = recipe.getConsolidatedInput().size();

        for (int j = 0; j < inputItemCount; ++j) {
            final ItemStack inputItemStack = recipe.getConsolidatedInput().get(j);

            final String requiredItemCount = String.format("%d", inputItemStack.func_190916_E());
            final int textWidth = getGuiRenderer().getStringWidth(requiredItemCount);

            final int border = 1;
            final int padding = 2;
            final int itemSize = 16;

            getGuiRenderer().renderItem(this, inputItemStack, getBounds().getWidth() - border - (itemSize + padding) * (j + border), padding + border);

            GlStateManager.depthFunc(GL11.GL_ALWAYS);
            getGuiRenderer().drawStringWithShadow(this, requiredItemCount, getBounds().getWidth() - border - (itemSize + padding) * j - textWidth - border , 12, 16777215);
            GlStateManager.depthFunc(GL11.GL_LEQUAL);
        }

        GlStateManager.disableRescaleNormal();

    }

    public ProjectTableRecipe getRecipe()
    {
        return recipe;
    }

    public void setRecipe(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    public ProjectTableRecipeControl construct()
    {
        final ProjectTableRecipeControl concreteControl = new ProjectTableRecipeControl(getGuiRenderer(), craftableTexture, uncraftableTexture);

        concreteControl.recipeCraftingEventListeners = recipeCraftingEventListeners;

        return concreteControl;
    }

    @Override
    public void setModel(ProjectTableRecipe recipe)
    {
        this.recipe = recipe;
    }

    @Override
    protected void onButtonPressed() {
        onRecipeCraftingInternal();
    }

    /////////////////////////////////////////////////////////////////////////////
    // On Recipe Crafting Event Handling
    /////////////////////////////////////////////////////////////////////////////

    private void onRecipeCraftingInternal() {
        onRecipeCrafting();

        fireRecipeCraftingEvent();
    }

    protected void onRecipeCrafting() {
    }

    private void fireRecipeCraftingEvent()
    {
        for (final IRecipeCraftingEventListener eventListener : recipeCraftingEventListeners)
        {
            try {
                eventListener.onRecipeCrafting(recipe);
            } catch (final RuntimeException e) {
                System.out.println(String.format("Exception in an IRecipeCraftingEventListener %s", e));
            }
        }
    }

    private List<IRecipeCraftingEventListener> recipeCraftingEventListeners = new ArrayList<IRecipeCraftingEventListener>(1);

    @SuppressWarnings("unused")
    public void addOnRecipeCraftingEventListener(IRecipeCraftingEventListener listener) {
        recipeCraftingEventListeners.add(listener);
    }
    @SuppressWarnings("unused")
    public void removeOnRecipeCraftingEventListener(IRecipeCraftingEventListener listener) {
        recipeCraftingEventListeners.remove(listener);
    }
}
