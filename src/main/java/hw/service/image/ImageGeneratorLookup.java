package hw.service.image;

import hw.exception.ImageGeneratorNotFoundException;
import hw.model.ImageOutputType;

import java.util.Map;

public class ImageGeneratorLookup {

    private Map<ImageOutputType, ImageGenerator> generators;

    public ImageGeneratorLookup(Map<ImageOutputType, ImageGenerator> generators) {
        this.generators = generators;
    }

    public ImageGenerator lookup(ImageOutputType type) {
        ImageGenerator generator = generators.get(type);
        if (generator == null) {
            throw new ImageGeneratorNotFoundException("No generator found for type " + type);
        }
        return generator;
    }

}
