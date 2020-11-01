const path = require('path');
const toml = require('toml'); 
const yaml = require('yamljs');
const json5 = require('json5');
const webpack = require('webpack');

const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
    mode: 'development',
    entry: './src/index.js',
    devtool: 'inline-source-map',
    output: {
        filename: 'novemberizing.orientalism.js',
        path: path.resolve(__dirname, '../docs')
    },
    devServer: {
        contentBase: path.join(__dirname, 'build'),
        hot: true,
        host: '0.0.0.0',
        port: 8081,
        publicPath: '/orientalism',
        contentBasePublicPath: '/orientalism',
        disableHostCheck: true
    },
    plugins: [
        new CleanWebpackPlugin({ cleanStaleWebpackAssets: false }),
        new HtmlWebpackPlugin({
            filename: 'index.html',
            template: 'index.html',
            inject: true
        }),
        new CopyWebpackPlugin({
            patterns: [
                { from: 'static' }
            ]
        }),
        new webpack.HotModuleReplacementPlugin()
    ],
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: '/node_modules',
                use: ['babel-loader']
            },
            {
                test: /\.html$/,
                use: [
                    {
                        loader: 'html-loader',
                        options: { minimize: false }
                    }
                ]
            },
            {
                test: /\.scss$/,
                use: [MiniCssExtractPlugin.loader, "css-loader", "sass-loader"]
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(png|svg|jpg|gif)$/,
                use: ['file-loader']
            },
            {
                test: /\.(woff|woff2|eot|ttf|otf)$/,
                use: ['file-loader']
            },
            {
                test: /\.xml$/,
                use: ['xml-loader']
            },
            {
                test: /\.toml$/,
                type: 'json',
                parser: {
                    parse: toml.parse
                }
            },
            {
                test: /\.yaml$/,
                type: 'json',
                parser: {
                    parse: yaml.parse
                }
            },
            {
                test: /\.json$/,
                type: 'json',
                parser: {
                    parse: json5.parse
                }
            }
        ]
    }
}
