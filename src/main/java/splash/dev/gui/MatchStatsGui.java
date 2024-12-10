package splash.dev.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import splash.dev.data.MatchInfo;
import splash.dev.data.StoredMatchData;
import splash.dev.recording.ItemUsed;
import splash.dev.util.Renderer2D;

import java.awt.*;

import static splash.dev.BetterCpvp.mc;

public class MatchStatsGui {
    int id, y, width, height;
    MatchInfo match;
    private int scrollOffset;

    public MatchStatsGui(int id, int y, int width, int height) {
        this.id = id;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scrollOffset = 0;
        match = StoredMatchData.getMatchId(id);
    }

    public static void drawItemCount(DrawContext drawContext, ItemStack itemStack, int x, int y, float size, String count) {
        MatrixStack matrices = drawContext.getMatrices();
        matrices.push();
        matrices.scale(size, size, 1f);
        int scaledX = (int) (x / size);
        int scaledY = (int) (y / size);
        drawContext.drawItem(itemStack, scaledX, scaledY);
        drawContext.drawStackOverlay(mc.textRenderer, itemStack, scaledX, scaledY, count);
        matrices.pop();
    }

    private void renderText(DrawContext context, String label, String value, int x, int y, int width) {
        int rightEdgeX = x + width - mc.textRenderer.getWidth(value) - 28;
        context.drawText(mc.textRenderer, label, x + 10, y, Color.WHITE.getRGB(), false);
        context.drawText(mc.textRenderer, value, rightEdgeX, y, Color.WHITE.getRGB(), false);
    }

    private void renderHeading(DrawContext context, String headingText, int x, int y, int width) {
        int textWidth = mc.textRenderer.getWidth(headingText);
        int centeredX = x + (width - textWidth) / 2;
        int gradientY = y + mc.textRenderer.fontHeight / 2;
        Renderer2D.fillGradient(context,
                x, gradientY, centeredX, gradientY + 1,
                new Color(255, 255, 255, 0).getRGB(), new Color(255, 255, 255, 255).getRGB()
        );
        Renderer2D.fillGradient(context,
                centeredX + textWidth, gradientY, x + width, gradientY + 1,
                new Color(255, 255, 255, 255).getRGB(), new Color(255, 255, 255, 0).getRGB()
        );
        context.drawText(mc.textRenderer, headingText, centeredX, y, Color.WHITE.getRGB(), false);
    }

    public void render(DrawContext context, int mouseX, int mouseY) {
        int y = this.y - scrollOffset;
        int x = (context.getScaledWindowWidth() - width) / 2;
        int topMargin = 9;
        if (match == null) {
            String missingText = "Match info missing / corrupt?";
            int textWidth = mc.textRenderer.getWidth(missingText);
            int centeredX = x + (width - textWidth) / 2;
            context.drawText(mc.textRenderer, missingText, centeredX, y + topMargin, -1, false);
            return;
        }
        int currentY = y + topMargin;
        renderHeading(context, "Items used", x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;
        int itemCount = match.getItemUsed().size();
        int index = 0;

        for (ItemUsed itemUsed : match.getItemUsed()) {
            ItemStack itemStack = itemUsed.item();
            String itemName = itemStack.getName().getString();
            context.drawText(mc.textRenderer, itemName, x + 10, currentY, -1, false);
            int itemIconX = x + width - 40;
            drawItemCount(context, itemStack, itemIconX, currentY, 1.0f, String.valueOf(itemUsed.count()));
            currentY += (index == itemCount - 1) ? 10 : 20;
            index++;
        }

        currentY += topMargin;
        renderHeading(context, "Damage", x, currentY, width);

        String dealtDamage = String.valueOf(match.getDamageInfo().getDealtDamage());

        String healing = String.valueOf(match.getDamageInfo().getTotalHealing());

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Dealt Damage: ", dealtDamage, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Healing: ", healing, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "Attack", x, currentY, width);
        String crits = String.valueOf(match.getAttackInfo().getCrits());
        String misses = String.valueOf(match.getAttackInfo().getMisses());

        String longestCombo = String.valueOf(match.getAttackInfo().getLongestCombo());
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Crits: ", crits, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Misses: ", misses, x, currentY, width);

        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Longest Combo: ", longestCombo, x, currentY, width);
        currentY += topMargin;

        renderHeading(context, "Match", x, currentY, width);

        String time = String.valueOf(match.getMatchOutline().getTime());
        String matchId = String.valueOf(match.getMatchOutline().getId());
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Time: ", time, x, currentY, width);
        currentY += mc.textRenderer.fontHeight + topMargin;

        renderText(context, "Match ID: ", matchId, x, currentY, width);
    }

    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        scrollOffset -= (int) (verticalAmount * 10);
        scrollOffset = Math.max(0, scrollOffset);
    }
}