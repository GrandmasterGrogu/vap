var crypto = require('crypto') // Cryptographic functions
var ecdsa = require('ecdsa') // Elliptical Curve Digital Signatures
module.exports = function (Device) {
    var constants = {
		REGISTER : 0,
        CONFIRM_REGISTER : 1
    };
	
	// A function to return a Public Key for a non-registered device.
	function generatePublicKey(deviceIdentifier){
		
	}
// A function to register a device and give it a public key to communicate further 

    Device.greet = function (deviceIdentifier, purpose, cb) {
        //console.log(Device);
		generatePublicKey(deviceIdentifier);
        Device.app.models.Container.download('Devicecript', 'test.py', {"type":"text/plain"},function (err,res) {
            if (err) {
            }else{
            cb(null, res);
            }
        });
        	}; 

			
		
			
			
// At the manufacturer, a public key may be burned in the chip. 
// To simulate this, the method will receive the device's identifier and return a public key in base64.			
    Device.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'deviceIdentifier', type: 'string'}, {arg: 'purpose', type: 'number'}],
          returns: {arg: 'publickey', type: 'string'}
        }
    );

};
