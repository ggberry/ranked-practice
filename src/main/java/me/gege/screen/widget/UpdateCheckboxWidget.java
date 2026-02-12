package me.gege.screen.widget;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateCheckboxWidget extends CheckboxWidget {
    private final Consumer<Boolean> setter;

    public UpdateCheckboxWidget(int x, int y, int width, int height, Text text, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(x, y, width, height, text, getter.get());

        this.setter = setter;
    }

    @Override
    public void onPress() {
        super.onPress();

        this.setter.accept(this.isChecked());
    }
}
