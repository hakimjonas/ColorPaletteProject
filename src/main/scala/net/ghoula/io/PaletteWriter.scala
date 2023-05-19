package net.ghoula.io

import net.ghoula.modeling.Palette

class PaletteWriter {
  def writeSvg(palette: Palette, path: String): Unit = {
    // Create SVG string here
    // Write it to a file at the given path
  }
  def printHexCodes(palette: Palette): Unit = {
    palette.colors.foreach(color => println(color.toHex))
  }

}
