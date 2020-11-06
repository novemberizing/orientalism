/**
 * 
 * https://ogp.me/
 */

 import Image from './image';

export default class Opengraph {

    static data(meta){
        return new Image(meta.opengraph.image.url);
    }

    static from(meta) {
        const option = {
            sitename: meta.sitename,
            locale: meta.locale
        };
        const data = Opengraph.data(meta);
        return new Opengraph("website", meta.title, meta.description, meta.url, option, data);
    }

    constructor(type, title, description, url, option, data) {
        this._type = type;
        this._title = title;
        this._url = url;
        this._data = data;
        this._description = description;
        this._determiner = (option && option.determiner);
        this._locale = (option && option.locale);
        this._sitename = (option && option.sitename);
    }

    html() {
        let o = `<meta property="og:title" content="${this._title}" />
        <meta property="og:type" content="${this._type}" />
        <meta property="og:url" content="${this._url}" />\n`;

        o += ((this._description && `        <meta property="og:description" content="${this._description}">\n`) || '');
        o += ((this._determiner && `        <meta property="og:determiner" content="${this._determiner}">\n`) || '');

        if(this._locale) {
            if(this._locale.lang && this._locale.territory) {
                o += `        <meta property="og:locale" content="${this._locale.lang}_${this._locale.territory}">\n`;
            }
    
            if(this._locale.alternate) {
                this._locale.alternate.forEach(locale => {
                    if(locale && locale.lang && locale.territory) {
                        o += `        <meta property="og:locale:alternate" content="${locale.lang}_${locale.territory}">\n`;
                    }
                });
            }
        }

        o += ((this._sitename && `        <meta property="og:site_name" content="${this._sitename}">\n`) || '');

        o += this._data.html();

        return o.trim();
    }
}