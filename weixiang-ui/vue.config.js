const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  lintOnSave: false,
  transpileDependencies: true,
  devServer: {
    client: {
      overlay: {
        errors: true,
        warnings: false
      }
    }
  },
  configureWebpack: {
    resolve: {
      fallback: {
        // 解决stompjs依赖的net模块在浏览器环境中不存在的问题
        net: false
      }
    }
  }
});
