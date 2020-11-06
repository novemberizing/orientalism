import Card from "./card";

export default class Player extends Card {
    constructor(url, width, height, stream) {
        super('player');

        this._url = url;
        this._width = width;
        this._height = height;
        this.stream = stream;
    }

    get type(){ return this._type; }

    html() {
        return `
        <meta name="twitter:player" content="${this._url}">
        <meta name="twitter:player:width" content="${this._width}">
        <meta name="twitter:player:height" content="${this._height}">
        <meta name="twitter:player:stream" content="${this._stream}">`;
    }
}