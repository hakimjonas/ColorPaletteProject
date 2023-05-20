package net.ghoula.io

import net.ghoula.modeling.Palette

import java.io.PrintWriter

class PaletteWriter {
  def writeSvg(palette: Palette, path: String): Unit = {
    val svgHeader =
      "<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 400 400\">"
    val svgFooter = "</svg>"
    val colorSize = 400 / 4
    val colorRects = palette.colors.zipWithIndex
      .map { case (color, idx) =>
        val colorHex = color.toHex
        val colorX = (idx % 4) * colorSize
        val colorY = (idx / 4) * colorSize
        s"""<rect x="$colorX" y="$colorY" width="$colorSize" height="$colorSize" fill="$colorHex" />"""
      }
      .mkString("\n")
    val svgContent = svgHeader + "\n" + colorRects + "\n" + svgFooter

    new PrintWriter(path) {
      write(svgContent)
      close()
    }
  }

  def printHexCodes(palette: Palette): Unit = {
    palette.colors.foreach(color => println(color.toHex))
  }
}
