module.exports = function(Video) {

    var constants = {
		RECORD : 0,
		CONFIRM_RECORD : 1
    };
	

// A function to record a video's metadata

    Video.greet = function (metadata, purpose, cb) {
       
            cb(null, res);
        
        	}; 
     
    Video.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'metadata', type: 'object'}, {arg: 'purpose', type: 'number'}],
          returns: [{arg: 'token', type: 'string'}, {arg: 'oldtoken', type: 'string'}, {arg: 'purpose', type: 'number'}]
        }
    );


// A function to verify the device is registered and is the actual device.	
function verifyDevice()
{

    // Check the device identifiers
	
	// Check the token

	// Check the digital signature
}

function checkDeviceIdentifiers(){}

function checkToken(){}

function checkDigSig(){}

};
