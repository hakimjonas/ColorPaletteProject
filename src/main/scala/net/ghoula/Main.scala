package net.ghoula

import net.ghoula.io.{ImageReader, PaletteWriter}
import net.ghoula.modeling.SolarizedModel
import net.ghoula.palette_generation.PaletteGenerator

object Main {
  def main(args: Array[String]): Unit = {
    val imageReader = new ImageReader(
      "src/main/resources/luxembourg_gardens_2014.79.47.jpg"
    )
    val paletteGenerator =
      new PaletteGenerator(
        imageReader.getDownscaledImage(0.5),
        Seq(SolarizedModel)
      )
    val palettes = paletteGenerator.generatePalettes()

    val paletteWriter = new PaletteWriter()
    palettes.foreach { palette =>
      paletteWriter.writeSvg(
        palette,
        s"src/main/resources/output.svg"
      )
      paletteWriter.printHexCodes(palette)
    }
  }
}
