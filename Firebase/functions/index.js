const functions = require('firebase-functions');

const admin = require('firebase-admin');
const serviceAccount = require('./secrets/ServiceAccountKey.json');
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();


//Initialize App
const firebase = require('firebase/app');
require('firebase/auth');
require('firebase/database');
const config = require('./secrets/firebaseConfig.json');
firebase.initializeApp(config);

exports.realtimeTrigger = functions.database.ref('/SensorData/{batchId}/brewData/{brewDataId}').onWrite((snapshot, context) => {
    functions.logger.log("Batch Id" + context.params.batchId);
    functions.logger.log("Batch Id" + context.params.brewDataId);

    const test = snapshot.data.val();

    const payload = {
        notification: {
            title: 'test',
            body: 'test'
        },
        topic: {
            topic: context.params.batchId
        }
    }

    admin.messaging().subscribeToTopic(registrationTokens, topic)

    admin.messaging().send(payload)
        .then((response) => {
            functions.logger.log('Notification sent successfully:', response);
            return response
        }
        ).catch((error) => {
            functions.logger.log('Notification sent failed:', error);
        });

    return
    /*return admin.messaging().sendToTopic(`${context.params.batchId}`, payload)
    .then(function(response){
         console.log('Notification sent successfully:',response);
    }) 
    .catch(function(error){
         console.log('Notification sent failed:',error);
    });
    */


});