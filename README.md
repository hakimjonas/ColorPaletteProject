# ColorPaletteProject

This project generates a color palette from an image. It also includes support for the Solarized color scheme. 

## Features

- Extracts dominant colors from an image using K-Means++ clustering.
- Scales the image to optimize processing time without sacrificing quality.
- Provides an abstract `PaletteModel` for defining new color palette models.
- Maps dominant colors to the nearest colors defined in the `PaletteModel`.
- Generates a new color palette by adjusting colors based on the `PaletteModel`.
- Outputs a SVG representation of the generated color palette.
- Prints the hex codes of the colors in the generated color palette to the console.

## Usage

1. Clone the repository to your local machine.
2. In the `Main` object, update the `imagePath` to point to your image.
3. Run the `Main` object. The new palette(s) will be printed to the console, and a SVG representation will be saved to your specified path.
   
## Requirements

- Scala 2.13.7
- sbt 1.8.2

## Limitations

- The tool uses the RGB color space for color distance calculations, which may not always yield perceptually accurate results.

## Future Enhancements

- Improved color distance calculation using the LAB color space.
- Support for other color spaces and color palette models.
- Parallel processing for increased performance on multi-core systems.

## License

This project is licensed under the terms of the GNU General Public License v3.0 (GPL-3.0). See [LICENSE](LICENSE) file for more details.

## Image Credit

The image used in this project, "The Corcoran Collection (Museum Purchase, William A. Clark Fund)", is in the public domain.

Source: [Corcoran Collection](https://www.nga.gov/collection/art-object-page.166500.html)
