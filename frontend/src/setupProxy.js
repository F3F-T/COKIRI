const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        '/routes', //추후에 파일 경로 추가
        createProxyMiddleware({
            target: 'http://localhost:8080',
            changeOrigin: true,
        })
    );
};