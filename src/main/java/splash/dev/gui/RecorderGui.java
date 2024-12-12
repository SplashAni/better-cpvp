package splash.dev.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import splash.dev.PVPStatsPlus;
import splash.dev.data.Category;
import splash.dev.recording.Recorder;

import static splash.dev.PVPStatsPlus.mc;

public class RecorderGui extends Screen {
    private final int dropdownWidth = 100;
    private final int dropdownHeight = 20;
    private boolean isDropdownOpen = false;
    private int baseY;
    int centerX = this.width / 2;
    int recordButtonY = baseY - dropdownHeight / 2;
    int scoreButtonY = recordButtonY - dropdownHeight * 2;

    public RecorderGui() {
        super(Text.of("recorder.gui"));
        if (PVPStatsPlus.recorder != null && PVPStatsPlus.getRecorder().isRecording()) {
            PVPStatsPlus.getRecorder().stopRecording(true);
            mc.setScreen(null);
        }

        baseY = this.height / 2 - 50;

    }


    @Override
    protected void init() {

        this.addDrawableChild(ButtonWidget.builder(Text.of("Show Score"), button -> {
            PVPStatsPlus.toggleRenderScore();
        }).size(dropdownWidth, dropdownHeight).build());

        this.addDrawableChild(ButtonWidget.builder(Text.of("Record GameMode"), button -> {
            isDropdownOpen = !isDropdownOpen;
        }).size(dropdownWidth, dropdownHeight).build());

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);



        this.children().stream()
                .filter(widget -> widget instanceof ButtonWidget)
                .forEach(widget -> {
                    ButtonWidget button = (ButtonWidget) widget;
                    if (button.getMessage().getString().equals("Record GameMode")) {
                        button.setY(recordButtonY);
                    } else if (button.getMessage().getString().equals("Show Score")) {
                        button.setY(scoreButtonY);
                    }
                    button.setX(centerX - dropdownWidth / 2);
                });

        if (isDropdownOpen) {
            int optionY = baseY + dropdownHeight / 2 + 5;

            for (Category category : Category.values()) {
                int optionX = centerX - dropdownWidth / 2;
                int optionHeight = dropdownHeight;

                context.fill(optionX, optionY, optionX + dropdownWidth, optionY + optionHeight, 0xFFAAAAAA);

                context.drawCenteredTextWithShadow(this.textRenderer, category.name(), centerX, optionY + 5, 0xFFFFFF);

                if (mouseX >= optionX && mouseX <= optionX + dropdownWidth && mouseY >= optionY && mouseY <= optionY + optionHeight) {
                    context.fill(optionX, optionY, optionX + dropdownWidth, optionY + optionHeight, 0x77FFFFFF);
                }

                optionY += optionHeight + 2;
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isDropdownOpen) {
            int centerX = this.width / 2;

            int optionY = baseY + dropdownHeight / 2 + 5;

            for (Category category : Category.values()) {
                int optionX = centerX - dropdownWidth / 2;
                int optionHeight = dropdownHeight;

                if (mouseX >= optionX && mouseX <= optionX + dropdownWidth && mouseY >= optionY && mouseY <= optionY + optionHeight) {
                    PVPStatsPlus.recorder = new Recorder();
                    PVPStatsPlus.getRecorder().startRecording(category);
                    isDropdownOpen = false;
                    mc.setScreen(null);
                    return true;
                }

                optionY += optionHeight + 2;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }
}
