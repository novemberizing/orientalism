import React from 'react';
import {Helmet} from "react-helmet";

import Reacts from '../util/reacts';

/**
 * @property    title
 *              The title of your object as it should appear within the graph, e.g., "The Rock".
 * @property    type
 *              The type of your object, e.g, "video.movie".
 *              Depending on the type your specify, other properties may also be required.
 * @property    image
 *              An image URL which should represent your object within the graph.
 * @property    url
 *              The canonical URL of your object atht will be used as its permanent id int the graph, e.g., "https://www.imdb.com/title/tt0117500/".
 * 
 * @property    audio optional
 *              A URL to an audio file to accompany this object.
 * @property    description optional      
 *              A one to two sentence description of your object.
 * 
 * @property    determiner optional
 *              The word that appears before object's title in a sentence.
 *              An enum of (a, an, the, "", auto).
 *              If auto is chosen, the consumer of your data should chose
 *              between "a" or "an". Default is "" (blank).
 * @property    locale optional
 *              The locale these tags are marked up in.
 *              Of the format language_TERRITORY.
 *              Default is en_US.
 * @property    site_name optional array
 *              An array of other locales this page is available in.
 * @property    sitename optional
 *              If your object is part of a larger web size,
 *              the name which should be displayed for the overall site. e.g., "IMDb".
 * @property    video optional
 *              A URL to a video file that complements this object.

 * 
 * <meta property="og:audio" content="https://example.com/bond/theme.mp3" />
 * 
<meta property="og:description" 
  content="Sean Connery found fame and fortune as the
           suave, sophisticated British agent, James Bond." />
<meta property="og:determiner" content="the" />

<meta property="og:locale" content="en_GB" />
<meta property="og:locale:alternate" content="fr_FR" />
<meta property="og:locale:alternate" content="es_ES" />
<meta property="og:site_name" content="IMDb" />
<meta property="og:video" content="https://example.com/bond/trailer.swf" />
 * 
 * @see     opengraph   [opengraph protocol](https://ogp.me/)
 */
export default class Opengraph extends React.Component {
    render() {
        return (
            <Helmet>
                <meta property="og:title" content={this.props.title} />
                <meta property="og:type" content={this.props.type} />
                <meta property="og:url" content={this.props.url} />
                <meta property="og:image" content={this.props.image} />
                {Reacts.render(this.props.audio, <meta property="og:image" content={this.props.audio} />)}
                {Reacts.render(this.props.description, <meta property="og:description" content={this.props.description} />)}
                {Reacts.render(this.props.determiner, <meta property="og:determiner" content={this.props.determiner} />)}
                {Reacts.render(this.props.locale, <meta property="og:locale" content={this.props.locale} />)}
                {Reacts.map(this.props.locales, (locale) => <meta property="og:locale:alternate" content={locale} />)}
                {Reacts.render(this.props.sitename, <meta property="og:site_name" content={this.props.sitename} />)}
                {Reacts.render(this.props.video, <meta property="og:video" content={this.props.video} />)}
            </Helmet>
        );
    }
}