import Card from "./card";

/**
 * twitter:image
 * 
 * A URL to a unique image representing the content of the page.
 * You should not use a generic image such as your website logo,
 * author photo, or other image that spans multiple pages.
 * Images for this Card support an aspect ratio of 1:1 width minimum
 * dimensions of 144x144 or maximum of 4096x4096 pixels.
 * Images must be less than 5MB in size.
 * Ths image will be cropped to an square of all platforms.
 * JPG, PNG, WEBP and GIF formats are supported.
 * Only the first frame of an animated GIF will be used.
 * SVG is not supported.
 * 
 * twitter:image:alt
 * 
 * A text description of the image conveying the essential nature of an image
 * of to users who are visually impaired.
 * Maximum 420 characters.
 */
export default class Summary extends Card {

    constructor(url, alt) {
        super('summary');

        this._url = url;
        this._alt = alt;
    }

    get type(){ return this._type; }

    html() {
        return `
        <meta name="twitter:image" content="${this._url}">
        <meta name="twitter:image:alt" content="${this._alt}">`;
    }
}