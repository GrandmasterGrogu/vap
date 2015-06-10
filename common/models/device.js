module.exports = function(Device) {

// A function to register the device and give it a token to communicate further 

    Device.greet = function (msg, cb) {
        //console.log(Device);
        Device.app.models.Container.download('Devicecript', 'test.py', {"type":"text/plain"},function (err,res) {
            if (err) {
            }else{
            cb(null, res);
            }
        });
        	}; 
     
    Device.remoteMethod(
        'greet', 
        {
          accepts: {arg: 'msg', type: 'string'},
          returns: {arg: 'greeting', type: 'string'}
        }
    );

	

};
