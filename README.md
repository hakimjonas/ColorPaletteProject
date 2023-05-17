# ColorPaletteProject

This project generates a color palette from an image, based on the Solarized color scheme.

## Features

- Extracts dominant colors from an image using K-Means clustering
- Maps dominant colors to the nearest Solarized color
- Generates a new color palette by adjusting Solarized colors to match the image's color scheme
- Outputs a SVG representation of the generated color palette

## Usage

1. Clone the repository to your local machine.
2. In the `ImageAnalyzer` object, update the `imagePath` to point to your image.
3. Run the `ImageAnalyzer` object. The new palette will be printed to the console, and a SVG representation will be saved to `src/main/resources/output.svg`.

## Requirements

- Scala 2.13.7
- sbt 1.8.2

## Limitations

- Currently, the tool only works with images that have at least two dominant colors.
- The tool uses the RGB color space for color distance calculations, which may not always yield perceptually accurate results.

## Future Enhancements

- Support for images with less than two dominant colors
- Improved color distance calculation using the LAB color space

## License

This project is licensed under the terms of the GNU General Public License v3.0 (GPL-3.0). See [LICENSE](LICENSE) file for more details.
