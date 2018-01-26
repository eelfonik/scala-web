const path = require('path');
const webpack = require('webpack');

module.exports = {
  entry: './front/index.js',
  output: {
    filename: 'bundle.js',
    path: path.resolve(__dirname, 'public/compiled')
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
        use: ['style-loader','css-loader']
      },
      {
        test: /\.(png|svg|jpg|gif)$/,
        use: ['file-loader']
      }
    ],
  }
}