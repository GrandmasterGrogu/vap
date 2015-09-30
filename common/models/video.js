var crypto = require('crypto') // Cryptographic functions
var ecdsa = require('ecdsa') // Elliptical Curve Digital Signatures
module.exports = function(Video) {

    var constants = {
		RECORD : 0,
		CONFIRM_RECORD : 1
    };
	

	
// A function to record a video's metadata

    Video.greet = function (filehash, deviceID, token, metadata, purpose, cb) {
	var error = null;    
	var oldtoken = token;
	var newtoken = generateNewToken(deviceID, oldtoken); 

	// If the deviceID is there and matches with the token, then create the video record.
    // Later encapsulate in function checkDeviceIdentifiers(){}	 and / or function checkToken(){}
	 Video.app.models.Device.findOne({where:{token: token, deviceID: deviceID}}, 
	   function(err, model){
		   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(model == null)
			   cb(null,null,null,null,null,null,null);
		   else{
			     	// TODO: Implement check the digital signature function checkDigSig(){}
					 
		model.oldtoken = oldtoken;
		model.token = newtoken;
		   model.save(null, function(err, instance){
			   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   
		 
					// Then, store the metadata of the video, if the purpose wasn't confirmation, associated with the device.
if(purpose === constants.RECORD)	 { 
	  Video.app.models.Video.create({confirm:constants.RECORD,deviceID: deviceID, metadata: metadata.toString()},
	   function(err, newVideoModel){
		 if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(newVideoModel == null)
			   cb(null,null,null,null,null,null,null);
		  
		cb(null,filehash,crypto.createHash('sha1').update(metadata).digest("hex").toString(),newtoken,oldtoken, newVideoModel.videoID,error);       		   	    
	   });
} else{
	// TODO: update video if videoID is valid with device and confirm, otherwise return nothing or an error message.
Video.app.models.Video.findOne({where:{deviceID: deviceID, videoID: purpose, confirm: constants.RECORD}}, 
  function(err, videoModel){
		 if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(videoModel == null)
			   cb(null,null,null,null,null,null,null);
		   videoModel.confirm = constants.CONFIRM_RECORD;
		   videoModel.save(null, function(err, instance){
			   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
				cb(null,filehash,null,newtoken,oldtoken, purpose,error);   
		   }); // End video model save

      		   	    
	   }); // End Find the video model with id and not confirmed
	} // End else if is not RECORD purpose
        }); // End Save device new token
		   } // End else if device with id and token found
			   
		  }
	   );
        
        	}; 
     
    Video.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'filehash', type: 'string'},{arg: 'deviceID', type: 'number'}, {arg: 'token', type: 'string'}, {arg: 'metadata', type: 'string'}, {arg: 'purpose', type: 'number'}],
          returns: [{arg: 'filehash', type: 'string'},{arg: 'metadatahash', type: 'string'},{arg: 'token', type: 'string'}, {arg: 'oldtoken', type: 'string'}, {arg: 'purpose', type: 'number'}, {arg: 'error', type: 'object'}]
        }
    );

	// A function to generate a new token for a registered device.
		// Used code from a stackoverflow to create random hash
		// http://stackoverflow.com/questions/9407892/how-to-generate-random-sha1-hash-to-use-as-id-in-node-js
	function generateNewToken(deviceIdentifier, oldtoken){
var current_date = (new Date()).valueOf().toString();
var random = Math.random().toString();
return crypto.createHash('sha1').update(oldtoken.toString() + deviceIdentifier.toString() + current_date + random).digest('hex').toString();		
	}

	// A function to verify that the signature matches the public key
	function checkDigSig(publickey, signature ){
		var verifyObject = crypto.createVerify('rsa');
		var YeaOrNay = false;
		try{
			verifyObject.update(data);
			YeaOrNay = verifyObject.verify(publickey, signature, 'base64');
		}
		catch(e){
			console.log(e);
		}
		return YeaOrNay;
	}
	
	// Define verify REST function
	Video.remoteMethod(
        'verify', 
        {
          accepts: [{arg: 'device', type: 'string'},{arg: 'video', type: 'string'}, {arg: 'signature', type: 'string'}],
          returns: [{arg: 'valid', type: 'boolean'},{arg: 'metadata', type: 'object'}, {arg: 'error', type: 'object'}]
        }
    );
	
	// A function to record a video's metadata
    Video.verify = function (device, video, signature) {
		var valid = false;
		valid = checkDigSig();
		var metadata = null;
		var error = null;
		cb(null,valid,metadata,error);  
	}
	
	
	
/* find50 function returns some device models	
*
*/
		Video.find50 = function (cb) {
      

		  Video.app.models.Video.find({limit:50, order: 'videoID DESC'}, 
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
   Video.remoteMethod(
        'find50', 
        {
          returns: {arg: 'videos', type: 'array'}
        }
    );	
	
};
