const path = require('path');
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CleanWebpackPlugin = require('clean-webpack-plugin');

module.exports = {
  entry: ['babel-polyfill', 'react-hot-loader/patch', './front/index.js'],
  devtool: 'inline-source-map',
  plugins: [
    new CleanWebpackPlugin(['public/compiled']),
    new HtmlWebpackPlugin({
      template: './front/index.html'
    }),
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin()
  ],
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'public/compiled')
  },
  devServer: {
    hot: true,
    historyApiFallback: true,
    proxy: {
      '/api/**': {
        target:'http://localhost:9000'
      }
    }
  },
  module: {
    rules: [
      {
        test: /\.jsx?$/,
        exclude: ['/node_modules/'],
        use: ['babel-loader']
      },
      {
        test: /\.css$/,
        use: ['style-loader', 'css-loader']
      },
      {
        test: /\.(png|svg|jpg|gif)$/,
        use: ['file-loader']
      }
    ],
  }
}