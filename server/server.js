var loopback = require('loopback');
var boot = require('loopback-boot');
// To simulate a hardware chip protected by encrypted communication, we will use https.
var https = require('https');
var sslConfig = require('./ssl-config');

 var options = {
      key: sslConfig.privateKey,
      cert: sslConfig.certificate
    };

var app = module.exports = loopback();



app.start = function(httpOnly) {
  if(httpOnly === undefined) {
    httpOnly = process.env.HTTP;
  }
  var server = null;
  if(!httpOnly) {
    var options = {
      key: sslConfig.privateKey,
      cert: sslConfig.certificate
    };
    server = https.createServer(options, app);
  } else {
    server = http.createServer(app);
  }
  server.listen(app.get('port'), function() {
    var baseUrl = (httpOnly? 'http://' : 'https://') + app.get('host') + ':' + app.get('port');
    app.emit('started', baseUrl);
    console.log('LoopBack server listening @ %s%s', baseUrl, '/');
  });
  return server;
};

/*
app.start = function() {
  // start the web server
  var port = process.env.PORT || 5000;
  return app.listen(port, function() {
    app.emit('started');
    console.log('Web server listening at: %s', app.get('url'));
  });
};
*/
// Bootstrap the application, configure models, datasources and middleware.
// Sub-apps like REST API are mounted via boot scripts.
boot(app, __dirname, function(err) {
  if (err) throw err;

  // start the server if `$ node server.js`
  if (require.main === module)
    app.start();
});
