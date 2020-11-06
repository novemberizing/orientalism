/**
 * 
 * @see     https://developer.twitter.com/en/docs/twitter-for-websites/cards/overview/markup
 * 
 * @member  card                twitter:card
 *                              The card type.
 *                              Used with all cards.
 *                              og:type
 *                              If an og:type, og:title, and og:description exist in the markup
 *                              but twitter:card is absent, then a summary card may be rendered.
 * 
 * @member  site                twitter:site
 *                              `@username` of website.
 *                              Either twitter:site or twitter:site:id is required.
 *                              Used with summary, summary_larget_image, app, player cards.
 * 
 * @member  creator             twitter:creator
 *                              `@username` of content creator.
 *                              Used with summary_large_image cards.
 * 
 * @member  description         twitter:description
 *                              Description of content (maximum 200 characters).
 *                              Used with summary, summary_large_image, player, cards.
 *                              og:description
 * 
 * @member  title               twitter:title
 *                              Title of content (max 70 characters)
 *                              Used with summary, summary_large_image, player cards.
 *                              og:title
 * 
 * @member  image.url           twitter:image
 *                              URL of image to use in the card.
 *                              Images must be less than 5MB in size.
 *                              JPG, PNG, WEBP and GIF formats are supported.
 *                              Only the first frame of an animiated GIF will be used.
 *                              SVG is not supported.
 *                              Used with summary, summary_large_image, player cards.
 *                              og:image
 *         
 * @member image.alt            twitter:image:alt
 *                              A text description of the image conveying the essential nature of
 *                              an image users who are visible impaired. Maximum 420 characters.
 *                              Used with summary, summary_large_image, player cards.
 * 
 * @member player.url           twitter:player
 *                              HTTPS URL of player iframe
 *                              Used with player card
 * 
 * @member player.width         Width of iframe in pixels
 *                              Used with player card
 * 
 * @member player.height        Height of iframe in pixels
 *                              Used with player card
 * 
 * @member player.stream        twitter:player:stream
 *                              URL to raw video or audio stream
 *                              Used with player card
 * 
 * @member  app.name.iphone     Name of your iphone app
 *                              Used with app card
 * 
 * @member  app.id.iphone       Your app id in the iTunes App Store (Note: NOT your bundle ID)
 *                              Used with app card
 * 
 * @member  app.url.iphone      Your app's custom URL scheme 
 *                              (you must include "://" after your scheme name)
 *                              Used with app card
 * 
 * @member  app.name.ipad       Name of your iPad optimized app
 *                              Used with app card
 * 
 * @member  app.id.ipad         Your app id in the iTunes App Store
 *                              Used with app card
 * 
 * @member  app.url.ipad        Your app's custom URL scheme
 *                              Used with app card
 * 
 * @member  app.name.googleplay Name of your Android app
 *                              Used with app card
 * 
 * @member  app.id.googleplay   Your app id in the Google Play Store
 *                              Used with app card
 * 
 * @member  app.url.googleplay  Your app's custom URL scheme
 *                              Used with app card.
 * 
 */
import Summary from "./summary";
import Image from "./image";

export default class Twitter {
    static get SummaryCard(){ return "summary"; }
    static get SummaryLargeImageCard(){ return "summary_large_image"; }
    static get PlayerCard(){ return "player"; }
    static get AppCard(){ return "app"; }

    static card(meta) {
        if(meta.type === Twitter.SummaryCard) {
            return new Summary(meta.url, meta.alt);
        } else if(meta.type === Twitter.SummaryLargeImageCard) {
            return new Image(meta.url, meta.alt);
        } else if(meta.type === Twitter.PlayerCard) {
            throw new Error();
        } else if(meta.type === Twitter.AppCard) {
            throw new Error();
        } else {
            throw new Error();
        }
    }

    static from(meta) {
        const o = new Twitter(meta.title, meta.description, meta.twitter.username, Twitter.card(meta.twitter));
        
        return o;
    }

    constructor(title, description, username, card) {
        this._card = card;
        this._site = username;
        this._creator = username;
        this._title = title;
        this._description = description;
    }

    html() {
        const prefix = `<meta name="twitter:card" content="${this._card.type}">
        <meta name="twitter:site" content="${this._site}">
        <meta name="twitter:creator" content="${this._creator}">
        <meta name="twitter:title" content="${this._title}">
        <meta name="twitter:description" content="${this._description}">`;

        return prefix + this._card.html();
    }
}