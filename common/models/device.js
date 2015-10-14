var crypto = require('crypto') // Cryptographic functions
var ecdsa = require('ecdsa') // Elliptical Curve Digital Signatures
module.exports = function (Device) {

    var constants = {
		REGISTER : 0,
        CONFIRM_REGISTER : 1,
		HARDWARE_RESET : -1 // TODO: Implement a function to simulate a hardware reset or secure firmware update of the chip
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
	function generateToken(deviceIdentifier, oldtoken){
		if(oldtoken==null)
			oldtoken = "No old token";
var current_date = (new Date()).valueOf().toString();
var random = Math.random().toString();
return crypto.createHash('sha1').update(oldtoken.toString() + deviceIdentifier.toString() + current_date + random).digest('hex').toString();		
	}
// A function to register a device and give it an encryption public key to communicate further 

    Device.greet = function (deviceIdentifier, digitalSignaturePublicKey, metadata, purpose, cb) {
        //console.log(Device);
		if(deviceIdentifier == null)
			deviceIdentifier = "Null case";
		if(digitalSignaturePublicKey == null)
			digitalSignaturePublicKey = "Null case";
		if(metadata == null)
			metadata = "Null case";
		if(purpose == null)
			purpose = constants.REGISTER;
		if(purpose===constants.REGISTER){
		var pubkey = generatePublicKey(deviceIdentifier);
		var token = generateToken(deviceIdentifier);
        var error = null;
		  Device.app.models.Device.create({uid: deviceIdentifier, publickey:digitalSignaturePublicKey, token:token, oldtoken:null, metadata: metadata, confirm: 0},
	   function(err, model){
		   if(err)
			   cb(null, null, null, null, error);
            cb(null,model.deviceID, pubkey, token, null);
	   });
	   }
	   else if(purpose>constants.REGISTER)
	   {
		   // Instead of making a new argument, re-use old ones to pass data.
		   // This is more of a convenience for coding than design.
		   var token = digitalSignaturePublicKey;
		  Device.app.models.Device.findOne({where:{deviceID: purpose, confirm: constants.REGISTER}}, 
  function(err, model){
		 if(error){
			   error = err;
			   cb(null,null,null,null,error);
			   }
		   else if(model == null)
			   cb(null,null,null,null,{code:1,msg:"Database was unavailable."});
	    var oldtoken = model.oldtoken;
		var newtoken = generateToken(deviceIdentifier, oldtoken);  
		model.oldtoken = model.token;
		model.token = newtoken;
		model.confirm = constants.CONFIRM_REGISTER;
		   model.save(null, function(err, instance){
			   
		cb(null,purpose,token,newtoken,null);    
		   });
		   		   	    
	   });
	   }
   else{
	   cb(null,null,null,null,{code:4,msg:"Purpose did not match any action."});
   }
        	}; 

/* find50 function returns some device models	
*
*/
		Device.find50 = function (cb) {
      

		  Device.app.models.Device.find({limit:50, order: 'deviceID DESC'}, 
  function(err, models){
		 if(err){
			  
			   cb(null,err);
			   }
	
		cb(null,models);    
		   });		   		   	    
	 	}; 	
			
/* DEMO Function
	* This is for the purpose of demo-ing the VAP.
	* It gets data from the database according to a query. 
	* In a real VAP, this would be ludicrously insecure and break confidentiality and the security model.
	*/
   Device.remoteMethod(
        'find50', 
        {
          returns: {arg: 'devices', type: 'array'}
        }
    );	
	
// At the manufacturer, a public key, to be used for encrypted communication, may be burned in the chip and further protected by a token. 
// To simulate this, the method will receive the device's identifier and return a public key in base64.			

// The digital signature public key that the device sends is a hardware protected key, 
// while the private key is accessible to the user and device, for signing only and not viewing, 
// it serves the purpose of proving data came from the device, or identification.
    Device.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'deviceIdentifier', type: 'string'}, {arg:'digitalSignaturePublicKey', type: 'string'}, {arg: 'metadata', type: 'string'}, {arg: 'purpose', type: 'number'}],
          returns: [{arg: 'deviceID', type: 'number'},{arg: 'publickey', type: 'string'}, {arg: 'token', type: 'string'}, {arg: 'error', type: 'object'}]
        }
    );
	
	
};
