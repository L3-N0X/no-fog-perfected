package de.lenox.client

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.network.chat.TextColor

object NoFogCommandFormatting {
    val pastelBlue = TextColor.fromRgb(0x80DEEA)      // Accent color for fog names/labels (Soft Cyan)
    val pastelGreen = TextColor.fromRgb(0xA8E6CF)     // ON status (Pastel Light Green)
    val pastelRed = TextColor.fromRgb(0xFF8B94)       // OFF status (Pastel Light Red)
    val pastelYellow = TextColor.fromRgb(0xFFD3B6)    // Numerical values / percentages (Pastel Light Yellow/Orange)
    val pastelLavender = TextColor.fromRgb(0xD1C4E9)  // Brackets, separators (Pastel Lavender)
    val white = TextColor.fromRgb(0xFFFFFF)           // Main text

    fun text(str: String, color: TextColor? = null): MutableComponent {
        val comp = Component.literal(str)
        return if (color != null) {
            comp.setStyle(Style.EMPTY.withColor(color))
        } else {
            comp
        }
    }

    fun label(str: String): MutableComponent = text(str, pastelBlue)
    fun value(str: String): MutableComponent = text(str, pastelYellow)
    fun separator(str: String): MutableComponent = text(str, pastelLavender)

    fun status(isOn: Boolean): MutableComponent {
        val bracketOpen = separator("[")
        val statusText = if (isOn) text("ON", pastelGreen) else text("OFF", pastelRed)
        val bracketClose = separator("]")
        return bracketOpen.append(statusText).append(bracketClose)
    }

    fun formatLine(labelName: String, isOn: Boolean, extra: String? = null, extraValue: String? = null): MutableComponent {
        val comp = text(" ").append(label(labelName))
            .append(separator(" : "))
            .append(status(isOn))
        
        if (extra != null && extraValue != null) {
            comp.append(text(" "))
                .append(separator("|"))
                .append(text(" $extra: "))
                .append(value(extraValue))
        }
        return comp
    }

    fun formatHeader(title: String): MutableComponent {
        return separator("-- ").append(text(title, white)).append(separator(" -------------"))
    }

    fun formatFeedback(labelName: String, isOn: Boolean): MutableComponent {
        return label(labelName)
            .append(separator(" : "))
            .append(status(isOn))
    }

    fun formatFeedback(labelName: String, valueStr: String): MutableComponent {
        return label(labelName)
            .append(separator(" : "))
            .append(value(valueStr))
    }
}
