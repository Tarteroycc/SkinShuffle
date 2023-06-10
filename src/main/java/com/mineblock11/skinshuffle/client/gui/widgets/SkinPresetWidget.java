package com.mineblock11.skinshuffle.client.gui.widgets;

import com.mineblock11.skinshuffle.client.gui.SkinCarouselScreen;
import com.mineblock11.skinshuffle.client.gui.cursed.DummyClientPlayerEntity;
import com.mineblock11.skinshuffle.client.gui.cursed.GuiEntityRenderer;
import com.mineblock11.skinshuffle.client.preset.SkinPreset;
import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.widget.AbstractSpruceWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.entity.LivingEntity;

import java.util.UUID;

public class SkinPresetWidget extends AbstractSpruceWidget {
    private final SkinPreset skinPreset;
    private final SkinCarouselScreen parent;
    private Position position;
    private boolean isCentral;
    private LivingEntity entity;
    private double scaleFactor;

    public SkinPresetWidget(SkinCarouselScreen parent, Position position, int width, int height, SkinPreset skinPreset) {
        super(position);

        this.parent = parent;
        this.skinPreset = skinPreset;
        this.width = width;
        this.height = height;

        var skin = skinPreset.getSkin();
        entity = new DummyClientPlayerEntity(
                null, UUID.randomUUID(),
                skin::getTexture, skin::getModel
        );
    }

    public void overridePosition(Position newPosition) {
        this.position = newPosition;
    }

    public void overrideDimensions(int newWidth, int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
    }

    @Override
    public int getX() {
        return this.position.getX();
    }

    @Override
    public int getY() {
        return this.position.getY();
    }

    @Override
    protected void renderBackground(DrawContext graphics, int mouseX, int mouseY, float delta) {
        graphics.drawBorder(getX(), getY(), getWidth(), getHeight(), this.active ? 0xDF000000 : 0x5F000000);
        graphics.fill(getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, this.active ? 0x7F000000 : 0x0D000000);
    }

    @Override
    protected void renderWidget(DrawContext graphics, int mouseX, int mouseY, float delta) {
        graphics.drawTextWithShadow(this.client.textRenderer, this.skinPreset.getName() != null ? this.skinPreset.getName() : "Unnamed Preset", getX() + (this.width / 2) - (this.client.textRenderer.getWidth(this.skinPreset.getName() != null ? this.skinPreset.getName() : "Unnamed Preset")) / 2, getY() + this.client.textRenderer.fontHeight / 2, this.active ? 0xFFFFFFFF : 0xFF808080);

        GuiEntityRenderer.drawEntity(
                graphics.getMatrices(), getX() + (this.getWidth() / 2), this.getY() + this.height / 2,
                (int) this.scaleFactor * 16, getEntityRotation(), 0, 0, entity
        );
    }

    public void setScaleFactor(double scaleFactor) {
        this.scaleFactor = scaleFactor;
    }

    private float getEntityRotation() {
        return isActive() ? (float) (GlfwUtil.getTime() - parent.getLastCardSwitchTime()) * 35.0f : 0.0f;
    }
}
