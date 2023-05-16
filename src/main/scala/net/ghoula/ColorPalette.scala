package net.ghoula

case class ImageColor(r: Int, g: Int, b: Int) {
  def toLinear: ImageColor = {
    def convert(c: Int): Int = {
      val sRGB = c / 255.0
      (if (sRGB <= 0.04045) sRGB / 12.92 else Math.pow((sRGB + 0.055) / 1.055, 2.4) * 255.0).toInt
    }
    ImageColor(convert(r), convert(g), convert(b))
  }
}

object ImageColor {
  def rgbToHSL(color: ImageColor): HSL = {
    val r = color.r / 255.0
    val g = color.g / 255.0
    val b = color.b / 255.0
    val max = Math.max(Math.max(r, g), b)
    val min = Math.min(Math.min(r, g), b)

    var h = (max + min) / 2.0
    var s = h
    val l = h

    if (max == min) {
      h = 0.0
      s = 0.0
    } else {
      val d = max - min
      s = if (l > 0.5) d / (2.0 - max - min) else d / (max + min)
      h = max match {
        case x if x == r => (g - b) / d + (if (g < b) 6 else 0)
        case x if x == g => (b - r) / d + 2
        case _ => (r - g) / d + 4
      }
      h /= 6.0
    }

    HSL(h * 360, s, l)
  }
}

case class HSL(h: Double, s: Double, l: Double)

object ColorPalette {
  private def relativeLuminance(color: ImageColor): Double = {
    val r = color.r / 255.0
    val g = color.g / 255.0
    val b = color.b / 255.0
    0.2126 * r + 0.7152 * g + 0.0722 * b
  }

  def contrastRatio(color1: ImageColor, color2: ImageColor): Double = {
    val l1 = relativeLuminance(color1.toLinear)
    val l2 = relativeLuminance(color2.toLinear)

    if (l1 > l2) (l1 + 0.05) / (l2 + 0.05)
    else (l2 + 0.05) / (l1 + 0.05)
  }

  def main(args: Array[String]): Unit = {
    val baseLight = ImageColor(253, 246, 227) // Solarized base3
    val baseDark = ImageColor(0, 43, 54) // Solarized base03
    val textLight = ImageColor(101, 123, 131) // Solarized base00
    val textDark = ImageColor(131, 148, 150) // Solarized base0

    println(contrastRatio(baseLight, textLight)) // should print the contrast ratio for the light theme
    println(contrastRatio(baseDark, textDark)) // should print the contrast ratio for the dark theme

    val hslBaseLight = ImageColor.rgbToHSL(baseLight)
    val hslBaseDark = ImageColor.rgbToHSL(baseDark)
    val hslTextLight = ImageColor.rgbToHSL(textLight)
    val hslTextDark = ImageColor.rgbToHSL(textDark)

    println(hslBaseLight)
    println(hslBaseDark)
    println(hslTextLight)
    println(hslTextDark)
  }
}
