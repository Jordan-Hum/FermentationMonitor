const functions = require('firebase-functions');

const admin = require('firebase-admin');
const serviceAccount = require('./secrets/ServiceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    databaseURL: "https://fermentationmonitor-f3492.firebaseio.com"
});

const db = admin.database();


//Initialize App
const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/database');
const config = require('./secrets/firebaseConfig.json');
firebase.initializeApp(config);

exports.realtimeTrigger = functions.database.ref('/SensorData/{batchId}/brewData/{brewDataId}').onWrite(async (snapshot, context) => {
   
    const brewData = snapshot.after.val();

    //Fetch Data from BatchId to compare with brewData
    let BatchData;
    await db.ref(`/SensorData/${context.params.batchId}`).once('value').then((snapshot) => {
        return BatchData = snapshot.val();
    }).catch((error) => {
        functions.logger.log('Notification sent failed:', error);
    });

    //Obtain the day 
    const brewDataDate = brewData.date.split("-");
    const BatchDataDate = BatchData.startDate.split("-");

    
    let payload;

    //Send three potential messages
    //1. Measured gravity = ideal fg
    if (parseFloat(brewData.specificGravity) <= parseFloat(BatchData.fg)) {
        payload = {
            notification: {
                title: `${BatchData.batchName} is ready !`,
                body: `${BatchData.batchName} has reached its required gravity of ${BatchData.fg}`
            }
        }

        admin.messaging().sendToTopic(`${context.params.batchId}`, payload)
            .then((response) => {
                functions.logger.log('Final Gravity Notification sent successfully:', response);
                return response;
            })
            .catch((error) => {
                functions.logger.log('Notification sent failed:', error);
            });
    }
    //2. within 24hrs, check if gravity has changed
    const deltaDay = parseInt(brewDataDate[2]) - parseInt(BatchDataDate[2]);
    if( deltaDay  < 2 && deltaDay > 0) {
        if(parseFloat(brewData.specificGravity) >= parseFloat(BatchData.idealSg) ){
            payload = {
                notification: {
                    title: `${BatchData.batchName} is having some difficulties !`,
                    body: `${BatchData.batchName}'s gravity hasn't changed in the first couple of days, add some extra yeast. If it still doesn't change, your batch is done`
                }
            }
            
            admin.messaging().sendToTopic(`${context.params.batchId}`, payload)
            .then((response) => {
                functions.logger.log('Static Gravity Notification sent successfully:', response);
                return response;
            })
            .catch((error) => {
                functions.logger.log('Notification sent failed:', error);
            });
        }
    }
    //3. is the temperature in temperature range
    const deltaTemp =  parseFloat(brewData.tempOfLiquid) - parseFloat(BatchData.idealTemp);
    functions.logger.log(deltaTemp);
    if( deltaTemp > 5.4 || deltaTemp < -5.4){
        payload = {
            notification: {
                title: `${BatchData.batchName}  is too hot/cold!`,
                body: `${BatchData.batchName}'s has fallen out of the +-5.4 fahrenheit range`
            }
        }
        
        admin.messaging().sendToTopic(`${context.params.batchId}`, payload)
        .then((response) => {
            functions.logger.log('Temperature Notification sent successfully:', response);
            return response;
        })
        .catch((error) => {
            functions.logger.log('Notification sent failed:', error);
        });
    
    }

    return;
});