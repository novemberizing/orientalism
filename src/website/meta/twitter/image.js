import Card from "./card";

export default class Image extends Card {
    constructor(url, alt) {
        super('summary_large_image');

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