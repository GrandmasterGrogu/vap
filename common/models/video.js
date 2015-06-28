module.exports = function(Video) {

    var constants = {
		RECORD : 0,
		CONFIRM_RECORD : 1
    };
	

// A function to record a video's metadata

    Video.greet = function (metadata, purpose, cb) {
        Video.app.models.Container.download('Videocript', 'test.py', {"type":"text/plain"},function (err,res) {
            if (err) {
            }else{
            cb(null, res);
            }
        });
        	}; 
     
    Video.remoteMethod(
        'greet', 
        {
          accepts: [{arg: 'metadata', type: 'object'}, {arg: 'purpose', type: 'number'}],
          returns: {arg: 'greeting', type: 'string'}
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
