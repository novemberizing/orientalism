
export default class Image {
    constructor(url) {
        this._url = url;
    }

    html() {
        return `        <meta property="og:image" content="${this._url}" />\n`;
    }
}