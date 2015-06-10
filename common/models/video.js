module.exports = function(Video) {

// A function to record a video's metadata

    Video.greet = function (msg, cb) {
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
          accepts: {arg: 'msg', type: 'string'},
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
