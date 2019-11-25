// vue.config.js

var path = require("path");
function resolve(dir) {
  return path.join(__dirname, dir);
}

module.exports = {
  chainWebpack: config => {
    // 配置别名
    config.resolve.alias.set("@", resolve("src"));
  }
};
