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
	
	// If the deviceID is there and matches with the token, then create the video record.
    // Later encapsulate in function checkDeviceIdentifiers(){}	 and / or function checkToken(){}
	 Video.app.models.Device.findOne({where:{token: token, deviceID: deviceID}}, 
	   function(err, model){
		   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(model == null)
			   cb(null,null,null,null,null,null,{code:3,msg:"Device was not found with the provided token."});
		   else{
			     	// TODO: Implement check the digital signature function checkDigSig(){}
// Generate a new token for communication
					var newtoken = generateNewToken(deviceID, oldtoken); 
		// Parse the metadata into a JavaScript Object
	var parsedMetadata = null;
					
					var signature = null;
					try{
					parsedMetadata = JSON.parse(metadata);
					// If the metadata has the standard VAP digital signature, collect it in a separate variable also.
					if(parsedMetadata.digitalSignature)
						signature = parsedMetadata.digitalSignature;
					}
					catch(e){
						console.log(e); // If it fails, just log the error for now.
					}
		model.oldtoken = oldtoken;
		model.token = newtoken;
// Save the new token in the database and record the old one as well.		
	model.save(null, function(err, instance){
			   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   
// Then, store the metadata of the video, if the purpose wasn't confirmation, associated with the device.
if(purpose === constants.RECORD)	 { 
	  Video.app.models.Video.create({filehash:filehash,signature:signature,confirm:constants.RECORD,deviceID: deviceID, metadata: metadata.toString()},
	   function(err, newVideoModel){
		 if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(newVideoModel == null)
			   cb(null,null,null,null,null,null,{code:1,msg:"Database was unavailable."});
		  
		cb(null,filehash,crypto.createHash('sha1').update(metadata).digest("hex").toString(),newtoken,oldtoken, newVideoModel.videoID,null);       		   	    
	   });
} else{
	//Update video if videoID is valid with device and confirm, otherwise return nothing or an error message.
Video.app.models.Video.findOne({where:{deviceID: deviceID, videoID: purpose, confirm: constants.RECORD}}, 
  function(err, videoModel){
		 if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
		   else if(videoModel == null)
			   cb(null,null,null,null,null,null,{code:2,msg:"Video not found."});
		   videoModel.confirm = constants.CONFIRM_RECORD;
		   videoModel.save(null, function(err, instance){
			   if(error){
			   error = err;
			   cb(null,null,null,null,null,null,error);
			   }
				cb(null,filehash,null,newtoken,oldtoken, purpose,null);   
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
/*
	// A function to verify that the signature matches the public key
	// DEPRECATED: This function may not be necessary for the prototype,
	// since the client may do all the checking of the digital signature.
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
	*/
	
	
	// Define verify REST function
	Video.remoteMethod(
        'verify', 
        {
          accepts: [{arg: 'device', type: 'string'},{arg: 'video', type: 'string'}, {arg: 'signature', type: 'string'}],
          returns: [{arg: 'valid', type: 'boolean'},{arg: 'metadata', type: 'string'}, {arg: 'error', type: 'object'}]
        }
    );
	
	/* Video.verify() 
	A function to record a video's metadata
     @Parameters	
	 device: In a protocol, it could be any public identifier of the device. The public key will be used in this prototype.
     video: The filehash or public identifier of the video
	 signature:
	@Return
	valid: Boolean yes or no.
	metadata: Any metadata that the manufacturer wishes to return.
	Also, in theory, watermark or fingerprint information could be provided, anything useful to make public.
	error: Any error messages 
	*/
	Video.verify = function (device, video, signature, cb) {
		var valid = false;
		
		if(device == null)
				cb(null,valid,null,{msg: "No device identifier was sent, such as the public key."});
		if(video == null)
				cb(null,valid,null,{msg: "No video identifier was sent, such as the file hash."});
		if(signature == null)
				cb(null,valid,null,{msg: "No digital signature was provided."});
		
		var metadata = null;
		var error = null;
		
		Video.app.models.Device.findOne({where:{publickey:device, confirm: constants.CONFIRM_RECORD}}, 
  function(err, deviceModel){
	  if(err){
		error = err;		
		}
		if(!err & deviceModel ==null)
		{
		error = {msg: "The device is not registered."};
		}
		if(deviceModel){
if(deviceModel.deviceID){			
		Video.app.models.Video.findOne({where:{filehash: video,deviceID:deviceModel.deviceID, confirm: constants.CONFIRM_RECORD}}, 
  function(err, videoModel){
	  if(err){error = err;}
	  if(!err & videoModel ==null)
		{
		error = {msg: "The video is not registered or confirmed as authenticated."};
		}
	  if(videoModel){metadata = videoModel.metadata; valid= true;}
		cb(null,valid,metadata,error);
  });// end Video find	
} // end if deviceID
else{
				cb(null,valid,metadata,error);
}
		} // end if deviceModel
else{
				cb(null,valid,metadata,error);
}  
	});
	} // end Video.verify()
	
	
	
/* find50 function returns some device models	
*
*/
		Video.find50 = function (cb) {
      

		  Video.app.models.Video.find({limit:50, order: 'videoID DESC',include:'device'}, 
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
