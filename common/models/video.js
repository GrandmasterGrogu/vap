module.exports = function(Video) {

    var constants = {
		RECORD : 0,
		CONFIRM_RECORD : 1
    };
	

	
// A function to record a video's metadata

    Video.greet = function (deviceID, token, metadata, purpose, cb) {
var error = null;    
var oldtoken = token;

	// If the deviceID is there and matches with the token, then create the video record.
    // Later encapsulate in function checkDeviceIdentifiers(){}	 and / or function checkToken(){}
	 Video.app.models.Device.findOne({where:{token: token, deviceID: deviceID}}, 
	   function(err, model){
		   if(error){
			   error = err;
			   cb(null,null,null,error);
			   }
		   else if(model == null)
			   cb(null,null,null,null);
		   else{
			     	// TODO: Implement check the digital signature function checkDigSig(){}
					
					// Then, store the metadata of the video, if the purpose wasn't confirmation, associated with the device.
if(purpose === 0)	 { 
	  Video.app.models.Video.create({confirm:0,deviceID: deviceID, metadata: metadata.toString()},
	   function(err, models){
		 if(error){
			   error = err;
			   cb(null,null,null,error);
			   }
		   else if(models == null)
			   cb(null,null,null,null);
		   token = generateNewToken(deviceID, oldtoken);
		cb(token,oldtoken, purpose,error);       		   	    
	   });
} else{
	// TODO: update video if videoID is valid with device and confirm, otherwise return nothing or an error message.
  Video.app.models.Video.upsert({videoID: purpose, confirm:1, deviceID: deviceID, metadata: metadata.toString()},
	   function(err, models){
		 if(error){
			   error = err;
			   cb(null,null,null,error);
			   }
		   else if(models == null)
			   cb(null,null,null,null);
		   token = generateNewToken(deviceID, oldtoken);
		cb(token,oldtoken, purpose,error);       		   	    
	   });
	}
      
		   }
			   
		  }
	   );
        
        	}; 
     
    Video.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'deviceID', type: 'string'}, {arg: 'token', type: 'string'}, arg: 'metadata', type: 'object'}, {arg: 'purpose', type: 'number'}],
          returns: [{arg: 'token', type: 'string'}, {arg: 'oldtoken', type: 'string'}, {arg: 'purpose', type: 'number'}, {arg: 'error', type: 'object'}]
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

};
