var crypto = require('crypto') // Cryptographic functions
var ecdsa = require('ecdsa') // Elliptical Curve Digital Signatures
module.exports = function (Device) {
    var constants = {
		REGISTER : 0,
        CONFIRM_REGISTER : 1,
		HARDWARE_RESET : 2
    };
	
	// A function to return a Public Key for a non-registered device.
	
	function generatePublicKey(deviceIdentifier){
	// Instead of burning a public key to a protected hardware chip,
	// the app will simulate this part by using HTTPS.
	return 'SIMULATING USING SSL';	
	}
	
		// A function to generate a token for a registered device.
		// Used code from a stackoverflow to create random hash
		// http://stackoverflow.com/questions/9407892/how-to-generate-random-sha1-hash-to-use-as-id-in-node-js
	function generateToken(deviceIdentifier){
var current_date = (new Date()).valueOf().toString();
var random = Math.random().toString();
return crypto.createHash('sha1').update(deviceIdentifier.toString() + current_date + random).digest('hex').toString();		
	}
// A function to register a device and give it a public key to communicate further 

    Device.greet = function (deviceIdentifier, digitalSignaturePublicKey, metadata, purpose, cb) {
        //console.log(Device);
		var pubkey = generatePublicKey(deviceIdentifier);
		var token = generateToken(deviceIdentifier);
        var error = null;
            cb(null, pubkey, token, error);
        
        	}; 

			
		
			
			
// At the manufacturer, a public key, to be used for encrypted communication, may be burned in the chip and further protected by a token. 
// To simulate this, the method will receive the device's identifier and return a public key in base64.			

// The digital signature public key that the device sends is a hardware protected key, 
// while the private key is accessible to the user and device, for signing only and not viewing, 
// it serves the purpose of proving data came from the device, or identification.
    Device.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'deviceIdentifier', type: 'string'}, {arg:'digitalSignaturePublicKey', type: 'string'}, {arg: 'metadata', type: 'object'}, {arg: 'purpose', type: 'number'}],
          returns: [{arg: 'publickey', type: 'string'}, {arg: 'token', type: 'string'}, {arg: 'error', type: 'object'}]
        }
    );

};
